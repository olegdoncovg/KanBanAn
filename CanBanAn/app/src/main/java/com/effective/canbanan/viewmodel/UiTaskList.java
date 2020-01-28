package com.effective.canbanan.viewmodel;

import android.app.Activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.TickTimer;
import com.effective.canbanan.datamodel.TaskStatus;

public class UiTaskList {
    private static final String TAG = UiTaskList.class.getSimpleName();

    private final RecyclerView recyclerView;
    private final TaskStatus taskStatus;
    private final TaskListAdapter adapter;


    public UiTaskList(@NonNull Activity activity, TaskStatus taskStatus,
                      @IdRes int recycleViewId,
                      @NonNull IOnItemActions onItemActions,
                      @NonNull TickTimer tickTimer) {
        this.taskStatus = taskStatus;
        this.recyclerView = activity.findViewById(recycleViewId);

        adapter = new TaskListAdapter(taskStatus, onItemActions);
        if (taskStatus == TaskStatus.IN_PROGRESS) {
            tickTimer.addTickListener(adapter.getTimeUpdateListener());
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        SwipeController swipeController = new SwipeController(onItemActions);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        adapter.updateData();
    }

    public void updateUI() {
        adapter.updateData();
    }
}
