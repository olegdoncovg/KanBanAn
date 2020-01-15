package com.effective.canbanan.backend;

import android.content.Context;

import androidx.annotation.NonNull;

import com.effective.canbanan.datamodel.TaskItem;

import java.util.Collection;
import java.util.List;

public class DataProvider {
    private static final String TAG = DataProvider.class.getSimpleName();

    public interface IProvider {
        List<TaskItem> getItems();

        void updateServerInfo(@NonNull Context context, Collection<TaskItem> values);
    }

    public static IProvider getProvider(ProviderType providerType) {
        if (providerType == ProviderType.DEBUG) {
            return new ProviderDebug();
        }
        //TODO replace with default provider
        return null;
    }

}
