package com.effective.canbanan.datamodel;

public class TaskItem {
    public final int id;
    public final String name;
    public final long timeTotal;//Time summarise with timeToStart=0
    public final long timeToStart;//Dynamic part of time = System.currentTimeMillis() - timeToStart
    public final TaskStatus status;

    public TaskItem(int id, String name, long timeTotal, long timeToStart, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.timeTotal = timeTotal;
        this.timeToStart = timeToStart;
        this.status = status;
    }

    public String getCurrentTime() {
        if (timeToStart == 0) {
            return longToTime(timeTotal);
        }
        final long time = System.currentTimeMillis() - timeToStart;
        return longToTime(time);
    }

    private String longToTime(long time) {
        long ms = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60;
        time /= 60;
        long hours = time % 24;
        time /= 24;
        long days = time;

        StringBuilder sb = new StringBuilder();
        if (days != 0) {
            sb.append(days).append('!');
        }
        sb.append(hours).append(':').append(min).append(':').append(sec);
        return sb.toString();
    }
}
