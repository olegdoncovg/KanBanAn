package com.effective.canbanan.datamodel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.effective.canbanan.R;

public class TaskItem {
    private static final String TAG = TaskItem.class.getSimpleName();
    public final int id;
    public final String name;
    /**
     * Time summarise with timeStartActive=0
     */
    public final long timeTotal;
    /**
     * Dynamic part of time = System.currentTimeMillis() - timeStartActive
     * timeStartActive=0 if status!= IN_PROGRESS
     */
    public final long timeStartActive;
    public final TaskStatus status;

    public TaskItem(int id, String name, long timeTotal, long timeToStart, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.timeTotal = timeTotal;
        this.timeStartActive = timeToStart;
        this.status = status;
    }

    public TaskItem(TaskItem task, TaskStatus newStatus) {
        long timeTotal = task.timeTotal;
        long timeToStart = task.timeStartActive;

        if (task.status != newStatus) {
            if (timeToStart != 0) {
                long timeDiff = System.currentTimeMillis() - timeToStart;
                timeTotal += timeDiff;
                Log.d(TAG, "construct: timeTotal=" + longToTime(null, timeTotal) +
                        ", timeDiff=" + longToTime(null, timeDiff));
            }
            timeToStart = newStatus == TaskStatus.IN_PROGRESS ? System.currentTimeMillis() : 0;
        }

        this.id = task.id;
        this.name = task.name;
        this.timeTotal = timeTotal;
        this.timeStartActive = timeToStart;
        this.status = newStatus;
    }

    public String getCurrentTime(Context context) {
        if (timeStartActive == 0) {
            return longToTime(context, timeTotal);
        }
        final long time = System.currentTimeMillis() - timeStartActive;
        return longToTime(context, timeTotal) + "\n" + longToTime(context, time);
    }

    private String longToTime(@Nullable Context context, long time) {
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
            final String daysText = context == null ? "dd" : context.getString(R.string.days);
            sb.append(days).append(' ').append(daysText).append('\n');
        }
        sb.append(hours).append(':').append(min).append(':').append(sec);
        return sb.toString();
    }
}
