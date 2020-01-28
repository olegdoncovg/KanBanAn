package com.effective.canbanan.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeController extends Callback {
    private static final String TAG = SwipeController.class.getSimpleName();

    public enum Direction {
        LEFT,
        RIGHT
    }

    @NonNull
    private final IOnItemActions onItemActions;

    public SwipeController(IOnItemActions onItemActions) {
        this.onItemActions = onItemActions;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (!(viewHolder instanceof TaskViewHolder)) {
            Log.e(TAG, "onSwiped: not correct type viewHolder=" + viewHolder + ", direction=" + direction);
            return;
        }
        onItemActions.onSwiped(((TaskViewHolder) viewHolder).getTask(),
                direction == ItemTouchHelper.LEFT ? Direction.LEFT : Direction.RIGHT);
    }
}