package com.effective.canbanan.datamodel;

import android.content.Context;

import androidx.annotation.NonNull;

import com.effective.canbanan.BuildConfig;
import com.effective.canbanan.backend.DataProvider;
import com.effective.canbanan.backend.ProviderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksDataModel {
    private static final String TAG = TasksDataModel.class.getSimpleName();
    public static final int NO_TASK_ID = 0;//Can be never applied for task. Used only as marker

    private DataProvider.IProvider dataProvider = DataProvider.getProvider(ProviderType.SHARED_PREFERENCES);
    private final Map<Integer, TaskItem> mTasks = new HashMap<>();

    public static final TasksDataModel instance = new TasksDataModel();

    //For test ONLY
    public static void init(ProviderType providerType) {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException("Tray apply providerType=" + providerType + " in DEBUG mode");
        }
        instance.dataProvider = DataProvider.getProvider(providerType);
    }

    public void enumerate(@NonNull Context context) {
        mTasks.clear();
        List<TaskItem> tasks = dataProvider.getItems(context);
        for (TaskItem task : tasks) {
            mTasks.put(task.id, task);
        }
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
        mTasks.put(id, new TaskItem(id, taskName, 0, 0, status));
        dataProvider.updateServerInfo(context, mTasks.values(), false);
    }

    public void removeTask(@NonNull Context context, TaskItem item) {
        mTasks.remove(item.id);
        dataProvider.updateServerInfo(context, mTasks.values(), false);
    }
}
