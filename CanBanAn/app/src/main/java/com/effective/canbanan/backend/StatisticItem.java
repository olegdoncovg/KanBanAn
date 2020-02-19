package com.effective.canbanan.backend;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.effective.canbanan.TickTimer;
import com.effective.canbanan.datamodel.TaskItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StatisticItem {
    private static final String TAG = StatisticItem.class.getSimpleName();

    public final StatisticType statisticType;
    public final long timeToCollect;
    public final TaskItem taskItem;

    public StatisticItem(StatisticType statisticType, TaskItem taskItem) {
        this(statisticType, taskItem, TickTimer.currentTimeMillis());
    }

    private StatisticItem(StatisticType statisticType, TaskItem taskItem, long timeToCollect) {
        this.statisticType = statisticType;
        this.taskItem = taskItem;
        this.timeToCollect = timeToCollect;
    }

    //Internal class just to work with JSON
    private static class Json {
        private StatisticType statisticType;
        private long timeToCollect;
        private String taskItemJson;//TaskItem

        public Json(StatisticItem statistic) {
            statisticType = statistic.statisticType;
            timeToCollect = statistic.timeToCollect;
            taskItemJson = statistic.taskItem.toJSON();
        }

        @NonNull
        @Override
        public String toString() {
            return TAG + " statisticType=" + statisticType + ", timeToCollect=" + timeToCollect +
                    ", taskItem=" + taskItemJson;
        }
    }

    @Nullable
    public String toJSON() {
        return toJSON(this);
    }

    @Nullable
    private static String toJSON(StatisticItem task) {
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
    public static StatisticItem fromJSON(String strJson) {
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
        return new StatisticItem(json.statisticType, TaskItem.fromJSON(json.taskItemJson), json.timeToCollect);
    }
}
