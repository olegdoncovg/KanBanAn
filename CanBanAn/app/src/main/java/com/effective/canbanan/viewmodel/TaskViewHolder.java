package com.effective.canbanan.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;

import java.util.function.BiConsumer;

class TaskViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = TaskViewHolder.class.getSimpleName();

    public final TextView textTitle;
    public final TextView textTime;
    @NonNull
    public final View mainView;
    private TaskItem taskItem;
//    private boolean isActive;

    public static TaskViewHolder newInstance(@NonNull ViewGroup parent, int viewType,
                                             BiConsumer<View, TaskItem> showItemContextMenu) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_view_item, parent, false);
        return new TaskViewHolder(rowView, showItemContextMenu);
    }

    public TaskViewHolder(@NonNull final View rowView, BiConsumer<View, TaskItem> showItemContextMenu) {
        super(rowView);
        textTitle = rowView.findViewById(R.id.title);
        textTime = rowView.findViewById(R.id.time);
        this.mainView = rowView;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isActive = !isActive;
//                rowView.setBackgroundColor(isActive ?
//                        rowView.getContext().getColor(R.color.bg_item_active) :
//                        rowView.getContext().getColor(R.color.bg_item_passive));
                if (showItemContextMenu != null) {
                    showItemContextMenu.accept(rowView, taskItem);
                } else {
                    Log.e(TAG, "TaskViewHolder: showItemContextMenu=null");
                }
            }
        });
//        rowView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Dropper.instance.onItemTouchEvent(v, event, taskItem.id);
//                return false;
//            }
//        });
        rowView.setOnLongClickListener(v -> {
            Dropper.instance.onItemSelectToMove(TaskViewHolder.this, taskItem);
            return false;
        });
    }

    public void bind(Context context, TaskItem taskItem) {
        this.taskItem = taskItem;

        textTitle.setText(taskItem.name);
        textTime.setText(taskItem.getCurrentTime(context));
        mainView.setBackgroundColor(mainView.getContext().getColor(taskItem.status.getColorId()));
    }

    public void setTransparency(boolean transparent) {
        mainView.setAlpha(transparent ? 0.2f : 1f);
    }

//    public void setParamsAnsRoleFrom(TaskViewHolder roleFrom) {
//        roleFrom.setTransparency(true);
//        mainView.getLayoutParams().width = roleFrom.mainView.getWidth();
//        mainView.getLayoutParams().height = roleFrom.mainView.getHeight();
//    }
//
//    public void moveHolderPosition(PointF position) {
//        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mainView.getLayoutParams();
//        p.setMargins((int) (position.x), (int) (position.y), p.rightMargin, p.bottomMargin);
//    }
//
//    public TaskItem getTask() {
//        return taskItem;
//    }
}
