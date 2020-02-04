package com.effective.canbanan.datamodel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.effective.canbanan.BuildConfig;
import com.effective.canbanan.backend.DataProvider;
import com.effective.canbanan.backend.ProviderType;
import com.effective.canbanan.backend.StatisticItem;
import com.effective.canbanan.backend.StatisticType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TasksDataModel {
    private static final String TAG = TasksDataModel.class.getSimpleName();
    public static final int NO_TASK_ID = 0;//Can be never applied for task. Used only as marker

    private DataProvider.IProvider dataProvider = DataProvider.getProvider(ProviderType.SHARED_PREFERENCES);
    private final Map<Integer, TaskItem> mTasks = new HashMap<>();

    public static final TasksDataModel instance = new TasksDataModel();

    //For test ONLY/////////////////
    private static Integer lastCreatedTaskId_DEBUG;

    public static void init(Context context, ProviderType providerType, boolean clearStatistic) {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException("Tray apply providerType=" + providerType + " in DEBUG mode");
        }
        instance.dataProvider = DataProvider.getProvider(providerType);
        if (clearStatistic && instance.dataProvider != null) {
            Log.w(TAG, "init: clearStatistic");
            instance.dataProvider.clearStatistic(context);
        }
    }

    public static TaskItem getLastCreatedTaskItem() {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException("Tray getLastCreatedTaskItem in DEBUG mode");
        }
        return instance.getTask(lastCreatedTaskId_DEBUG);
    }
    //////////////////////////////////

    public void enumerate(@NonNull Context context) {
        mTasks.clear();
        List<TaskItem> tasks = dataProvider.getItems(context);
        for (TaskItem task : tasks) {
            mTasks.put(task.id, task);
        }
    }

    private TaskItem getTask(int taskId) {
        return mTasks.get(taskId);
    }

    @NonNull
    public List<TaskItem> getTasks(final TaskStatus status) {
        List<TaskItem> tasks = new ArrayList<>();
        for (TaskItem item : mTasks.values()) {
            if (item.status == status) {
                tasks.add(item);
            }
        }
        return tasks;
    }

    public boolean changeTaskCategory(@NonNull Context context, TaskItem task, TaskStatus newStatus) {
        TaskItem removed = mTasks.put(task.id, new TaskItem(task, newStatus));
        dataProvider.updateServerInfo(context, mTasks.values(), false);
        return removed != null;
    }

    public void removeTasks(@NonNull Context context, TaskStatus status) {
        List<TaskItem> toRemove = getTasks(status);
        for (TaskItem item : toRemove) {
            mTasks.remove(item.id);
        }
        dataProvider.updateServerInfo(context, mTasks.values(), false);
    }

    private int generateId() {
        return (int) System.currentTimeMillis();
    }

    public void addNewTask(@NonNull Context context, String taskName, TaskStatus status) {
        final int id = generateId();
        if (mTasks.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate task ID");
        }
        lastCreatedTaskId_DEBUG = id;

        final TaskItem newTask = new TaskItem(id, taskName, status);
        mTasks.put(id, newTask);
        dataProvider.postStatisticInfo(context, StatisticType.CREATE_NEW_TASK, newTask, false);

        dataProvider.updateServerInfo(context, mTasks.values(), false);
    }

    public void removeTask(@NonNull Context context, TaskItem item) {
        mTasks.remove(item.id);
        dataProvider.updateServerInfo(context, mTasks.values(), false);
    }

    @NonNull
    public List<String> getStatisticNames(@NonNull Context context, @NonNull SortOption option,
                                          int countMax) {
        final List<StatisticItem> items = new ArrayList<>(dataProvider.getStatisticInfo(context));

        //Todo - replace by sort methods in SortOption
        switch (option) {
            case POPULAR:
                Map<String, Integer> popularity = new TreeMap<>();
                for (StatisticItem item : items) {
                    String name = item.taskItem.name;
                    int amountToWrite = 1;
                    Integer amount = popularity.get(name);
                    if (amount != null) {
                        amountToWrite += amount;
                    }
                    popularity.put(name, amountToWrite);
                }

                final List<String> sortedList = new ArrayList<>(popularity.keySet());
                Collections.reverse(sortedList);

                final List<String> retList = new ArrayList<>();
                for (String name : sortedList) {
                    retList.add(name);
                    if (retList.size() >= countMax) {
                        break;
                    }
                }

                return retList;
            case RECENT:
                items.sort((o1, o2) -> (int) (o2.timeToCollect - o1.timeToCollect));
                final List<String> recentList = new ArrayList<>();

                for (StatisticItem item : items) {
                    String name = item.taskItem.name;
                    if (!recentList.contains(name)) {
                        recentList.add(name);
                    }
                    if (recentList.size() >= countMax) {
                        break;
                    }
                }
                return recentList;
            default:
                return new ArrayList<>();
        }
    }
}
