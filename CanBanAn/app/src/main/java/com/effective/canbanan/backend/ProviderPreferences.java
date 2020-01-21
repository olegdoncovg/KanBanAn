package com.effective.canbanan.backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.effective.canbanan.datamodel.TaskItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProviderPreferences implements DataProvider.IProvider {
    private static final String TAG = ProviderPreferences.class.getSimpleName();

    private static final String SHARED_PREF_PATH = "ProviderPreferences";
    private static final String SHARED_PREF_COUNT = "COUNT";
    private static final String SHARED_PREF_TASK_PS = "TASK_";

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
        final SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                getSharedPrefPath(), Context.MODE_PRIVATE);
        final List<TaskItem> items = new ArrayList<>();
        final int count = pref.getInt(SHARED_PREF_COUNT, 0);
        for (int i = 0; i < count; i++) {
            String stringTask = pref.getString(SHARED_PREF_TASK_PS + i, null);
            if (TextUtils.isEmpty(stringTask)) {
                Log.e(TAG, "getItems: count=" + count + "(i=" + i + "), stringTask=" + stringTask);
                continue;
            }
            items.add(TaskItem.fromJSON(stringTask));
        }
        if (items.size() != count) {
            Log.e(TAG, "getItems: Not all Task read,  count=" + count + ", size=" + items.size());
        }

        return items;
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void updateServerInfo(@NonNull Context context, @NonNull Collection<TaskItem> items,
                                 boolean instantAction) {
        final List<TaskItem> values = new ArrayList<>(items);
        final SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                getSharedPrefPath(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        editor.putInt(SHARED_PREF_COUNT, values.size());
        for (int i = 0; i < values.size(); i++) {
            String stringTask = values.get(i).toJSON();
            editor.putString(SHARED_PREF_TASK_PS + i, stringTask);
        }

        if (instantAction) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    private String getSharedPrefPath() {
        return SHARED_PREF_PATH + fileNapeSuffix;
    }
}
