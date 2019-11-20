package com.effective.canbanan.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TasksDataModel {
    private static final String TAG = TasksDataModel.class.getSimpleName();

    private final List<TaskItem> mTasks = new ArrayList<>();

    public static final TasksDataModel instance = new TasksDataModel();

    public void enumerate() {
        mTasks.add(new TaskItem(100, "Go ot work", 0, 0, TaskStatus.DONE));
        mTasks.add(new TaskItem(101, "Go ot home", 0, 0, TaskStatus.TO_DO));
        mTasks.add(new TaskItem(102, "Lunch", 0, 0, TaskStatus.TO_DO));
        mTasks.add(new TaskItem(103, "Do KanBanAn", 0, 0, TaskStatus.IN_PROGRESS));

    }

    private int generateId() {
        return (int) System.currentTimeMillis();
    }

    public List<TaskItem> getTasks(final TaskStatus status) {
        List<TaskItem> tasks = new ArrayList<>(mTasks);
        tasks.removeIf(new Predicate<TaskItem>() {
            @Override
            public boolean test(TaskItem taskItem) {
                return taskItem.status != status;
            }
        });
        return tasks;
    }
}
