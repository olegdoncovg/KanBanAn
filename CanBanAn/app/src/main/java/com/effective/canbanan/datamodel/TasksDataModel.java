package com.effective.canbanan.datamodel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksDataModel {
    private static final String TAG = TasksDataModel.class.getSimpleName();
    public static final int NO_TASK_ID = 0;//Can be never applied for task. Used only as marker

    private final Map<Integer, TaskItem> mTasks = new HashMap<>();

    public static final TasksDataModel instance = new TasksDataModel();
    private static final long debugTime1 = 1575561866521L;

    public void enumerate() {

        //TODO - Add dome values for debug - remove after then
        add(100, "Go ot work", 3000, 0, TaskStatus.DONE);
        add(101, "Go ot home", 2000, 0, TaskStatus.TO_DO);
        add(102, "Lunch", 0, 0, TaskStatus.TO_DO);
        add(103, "Do KanBanAn", 0, debugTime1, TaskStatus.IN_PROGRESS);
        add(104, "Do KanBanAn 2", 0, debugTime1, TaskStatus.IN_PROGRESS);
    }

    private TaskItem add(int id, String name, long timeTotal, long timeToStart, TaskStatus status) {
        return mTasks.put(id, new TaskItem(id, name, timeTotal, timeToStart, status));
    }

    private TaskItem add(TaskItem task, TaskStatus newStatus) {
        return mTasks.put(task.id, new TaskItem(task, newStatus));
    }

    private int generateId() {
        return (int) System.currentTimeMillis();
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

    public boolean changeTaskCategory(TaskItem taskItem, TaskStatus status) {
        return add(taskItem, status) != null;
    }

    public void removeTasks(TaskStatus status) {
        List<TaskItem> toRemove = getTasks(status);
        for (TaskItem item : toRemove) {
            mTasks.remove(item.id);
        }
    }
}
