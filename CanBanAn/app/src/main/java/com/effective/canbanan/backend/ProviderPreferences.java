package com.effective.canbanan.backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.effective.canbanan.BuildConfig;
import com.effective.canbanan.datamodel.TaskItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProviderPreferences implements DataProvider.IProvider {
    private static final String TAG = ProviderPreferences.class.getSimpleName();

    private static final String SHARED_PREF_PATH = "ProviderPreferences";
    private static final String SHARED_PREF_COUNT = "COUNT";
    private static final String SHARED_PREF_ITEM_PS = "ITEM_";

    private final String fileNapeSuffix;

    public ProviderPreferences() {
        this.fileNapeSuffix = "";
    }

    public ProviderPreferences(String fileNapeSuffix) {
        this.fileNapeSuffix = fileNapeSuffix;
    }

    @NonNull
    @Override
    public List<TaskItem> getItems(@NonNull Context context) {
        return getItems(context, TaskItem.class, TaskItem::fromJSON);
    }

    @Override
    public void updateServerInfo(@NonNull Context context, @NonNull Collection<TaskItem> items,
                                 boolean instantAction) {
        List<TaskItem> itemsList = new ArrayList<>(items);
        updateServerInfo(context, instantAction, itemsList.size(), TaskItem.class,
                position -> itemsList.get(position).toJSON());
    }

    @NonNull
    @Override
    public List<StatisticItem> getStatisticInfo(@NonNull Context context) {
        return getItems(context, StatisticItem.class, StatisticItem::fromJSON);
    }

    @Override
    public void postStatisticInfo(@NonNull Context context, @NonNull StatisticType statisticType,
                                  TaskItem taskItem, boolean instantAction) {
        List<StatisticItem> existingItems = getStatisticInfo(context);
        existingItems.add(new StatisticItem(statisticType, taskItem));
        updateServerInfo(context, instantAction, existingItems.size(), StatisticItem.class,
                position -> existingItems.get(position).toJSON());
    }

    @Override
    public void clearStatistic(@NonNull Context context) {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException("Tray clearStatistic not in DEBUG mode");
        }
        updateServerInfo(context, true, 0, StatisticItem.class,
                position -> "");
    }

    @SuppressLint("ApplySharedPref")
    private void updateServerInfo(@NonNull Context context, boolean instantAction,
                                  int count, Class aClass, ToJson supplier) {
        final SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                getSharedPrefPath(aClass), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        editor.putInt(SHARED_PREF_COUNT, count);
        for (int i = 0; i < count; i++) {
            String stringTask = supplier.convert(i);
            editor.putString(SHARED_PREF_ITEM_PS + i, stringTask);
        }

        if (instantAction) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    @NonNull
    private <T> List<T> getItems(@NonNull Context context, Class aClass, FromJson<T> fromJson) {
        final SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                getSharedPrefPath(aClass), Context.MODE_PRIVATE);
        final List<T> items = new ArrayList<>();
        final int count = pref.getInt(SHARED_PREF_COUNT, 0);
        for (int i = 0; i < count; i++) {
            String stringTask = pref.getString(SHARED_PREF_ITEM_PS + i, null);
            if (TextUtils.isEmpty(stringTask)) {
                Log.e(TAG, "getItems: count=" + count + "(i=" + i + "), stringTask=" + stringTask);
                continue;
            }
            items.add(fromJson.convert(stringTask));
        }
        if (items.size() != count) {
            Log.e(TAG, "getItems: Not all Task read,  count=" + count + ", size=" + items.size());
        }

        return items;
    }

    private String getSharedPrefPath(Class aClass) {
        return SHARED_PREF_PATH + "_" + aClass.getSimpleName() + "_" + fileNapeSuffix;
    }

    private interface ToJson {
        String convert(int position);
    }

    private interface FromJson<T> {
        T convert(String s);
    }
}
