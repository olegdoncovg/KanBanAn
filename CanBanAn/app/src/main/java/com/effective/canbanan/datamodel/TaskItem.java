package com.effective.canbanan.datamodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.effective.canbanan.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TaskItem {
    private static final String TAG = TaskItem.class.getSimpleName();

    private static long TIME_TO_START_BLINKING = 1000 * 60 * 20;//10 minutes

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

    @NonNull
    @Override
    public String toString() {
        return TAG + " id=" + id + ", name=" + name + ", status=" + status +
                ", timeTotal=" + timeTotal + ", timeStartActive=" + timeStartActive;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (!(obj instanceof TaskItem)) {
            return false;
        }
        TaskItem o = (TaskItem) obj;
        return id == o.id && TextUtils.equals(o.name, name) && status == o.status &&
                timeTotal == o.timeTotal && timeStartActive == o.timeStartActive;
    }

    @Override
    public int hashCode() {
        return (int) (id + name.hashCode() + timeTotal + timeStartActive + status.hashCode());
    }

    public TaskItem(int id, String taskName, TaskStatus status) {
        this(id, taskName, 0, status == TaskStatus.IN_PROGRESS ? System.currentTimeMillis() : 0, status);
    }

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

    public long getCurrentTimeInLong() {
        if (timeStartActive == 0) {
            return timeTotal;
        }
        return timeTotal + System.currentTimeMillis() - timeStartActive;
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

    public boolean isBlinking() {
        if (timeStartActive == 0) {
            return false;
        }
        return timeStartActive + TIME_TO_START_BLINKING < System.currentTimeMillis();
    }

    //Internal class just to work with JSON
    private static class Json {
        public int id;
        public String name;
        public long timeTotal;
        public long timeStartActive;
        public TaskStatus status;

        public Json(TaskItem task) {
            id = task.id;
            name = task.name;
            timeTotal = task.timeTotal;
            timeStartActive = task.timeStartActive;
            status = task.status;
        }

        @NonNull
        @Override
        public String toString() {
            return TAG + " id=" + id + ", name=" + name + ", status=" + status +
                    ", timeTotal=" + timeTotal + ", timeStartActive=" + timeStartActive;
        }
    }

    @Nullable
    public String toJSON() {
        return toJSON(this);
    }

    @Nullable
    private static String toJSON(TaskItem task) {
        Json value = new Json(task);
        final Gson gson = new Gson();
        String strJson = gson.toJson(value);
        if (TextUtils.isEmpty(strJson)) {
            Log.e(TAG, "toJSON: strJson=" + strJson + ", task=" + task);
            return null;
        }
        return strJson;
    }

    @Nullable
    public static TaskItem fromJSON(String strJson) {
        if (TextUtils.isEmpty(strJson)) {
            Log.e(TAG, "fromJSON: isEmpty strJson=" + strJson);
            return null;
        }
        if (!strJson.startsWith("{\"")) {
            Log.e(TAG, "fromJSON: Wrong strJson=" + strJson);
        }
        final GsonBuilder gBuilder = new GsonBuilder();
        final Gson gson = gBuilder.create();
        Json json = gson.fromJson(strJson, Json.class);
        if (json == null) {
            Log.e(TAG, "fromJSON: strJson=" + strJson + ", json=" + json);
            return null;
        }
        return new TaskItem(json.id, json.name, json.timeTotal, json.timeStartActive, json.status);
    }
}
