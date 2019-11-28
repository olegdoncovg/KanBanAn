package com.effective.canbanan.datamodel;

public class TaskItem {
    public final int id;
    public final String name;
    public final long timeTotal;
    public final long tomeToStart;
    public final TaskStatus status;

    public TaskItem(int id, String name, long timeTotal, long tomeToStart, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.timeTotal = timeTotal;
        this.tomeToStart = tomeToStart;
        this.status = status;
    }

    public String getCurrentTime() {
        return "Time 0:0";
    }
}
