package com.effective.canbanan.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;

import com.effective.canbanan.datamodel.TaskItem;

public interface IOnItemActions {
    void onContextMenuShown(@NonNull View view, TaskItem task);

    void onSwiped(TaskItem viewHolder, SwipeController.Direction direction);
}
