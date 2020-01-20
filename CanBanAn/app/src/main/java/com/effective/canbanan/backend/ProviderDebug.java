package com.effective.canbanan.backend;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProviderDebug implements DataProvider.IProvider {
    private static final String TAG = ProviderDebug.class.getSimpleName();

    private static final long debugTime1 = 1575561866521L;

    @NonNull
    @Override
    public List<TaskItem> getItems(@NonNull Context context) {
        List<TaskItem> items = new ArrayList<>();
        add(items, 100, "Go ot work", 3000, 0, TaskStatus.DONE);
        add(items, 101, "Go ot home", 2000, 0, TaskStatus.TO_DO);
        add(items, 102, "Lunch", 0, 0, TaskStatus.TO_DO);
        add(items, 103, "Do KanBanAn", 0, debugTime1, TaskStatus.IN_PROGRESS);
        add(items, 104, "Do KanBanAn 2", 0, debugTime1, TaskStatus.IN_PROGRESS);
        return items;
    }

    private void add(@NonNull List<TaskItem> items,
                     int id, String name, long timeTotal, long timeToStart, TaskStatus status) {
        items.add(new TaskItem(id, name, timeTotal, timeToStart, status));
    }

    private void add(@NonNull List<TaskItem> items, TaskItem task, TaskStatus newStatus) {
        items.add(new TaskItem(task, newStatus));
    }

    @Override
    public void updateServerInfo(@NonNull Context context, Collection<TaskItem> values, boolean instantAction) {
        Toast.makeText(context, "ProviderDebug don't have server part!", Toast.LENGTH_LONG).show();
    }
}
