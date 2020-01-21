package com.effective.canbanan.backend;

import android.content.Context;

import androidx.annotation.NonNull;

import com.effective.canbanan.datamodel.TaskItem;

import java.util.Collection;
import java.util.List;

public class DataProvider {
    private static final String TAG = DataProvider.class.getSimpleName();

    public interface IProvider {
        @NonNull
        List<TaskItem> getItems(@NonNull Context context);

        void updateServerInfo(@NonNull Context context, Collection<TaskItem> values, boolean instantAction);
    }

    public static IProvider getProvider(ProviderType providerType) {
        switch (providerType) {
            case DEBUG:
                return new ProviderDebug();
            case SHARED_PREFERENCES:
                return new ProviderPreferences();
        }
        //TODO replace with default provider
        return null;
    }

}
