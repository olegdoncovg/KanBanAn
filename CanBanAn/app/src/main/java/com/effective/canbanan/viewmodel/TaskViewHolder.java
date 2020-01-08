package com.effective.canbanan.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;

class TaskViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = TaskViewHolder.class.getSimpleName();

    public final TextView textTitle;
    public final TextView textTime;
    @NonNull
    public final View parentView;
    private TaskItem taskItem;
    private boolean isActive;

    public static TaskViewHolder newInstance(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_view_item, parent, false);
        TaskViewHolder vh = new TaskViewHolder(rowView);
        return vh;
    }

    public TaskViewHolder(@NonNull final View rowView) {
        super(rowView);
        textTitle = rowView.findViewById(R.id.title);
        textTime = rowView.findViewById(R.id.time);
        this.parentView = rowView;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActive = !isActive;
                rowView.setBackgroundColor(isActive ?
                        rowView.getContext().getColor(R.color.bg_item_active) :
                        rowView.getContext().getColor(R.color.bg_item_passive));
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
            Dropper.instance.onItemLongClick(TaskViewHolder.this, taskItem);
            return false;
        });
    }

    public void bind(Context context, TaskItem taskItem) {
        this.taskItem = taskItem;

        textTitle.setText(taskItem.name);
        textTime.setText(taskItem.getCurrentTime(context));
        parentView.setBackgroundColor(parentView.getContext().getColor(taskItem.status.getColorId()));
    }

    public void setTransparency(boolean transparent) {
        parentView.setAlpha(transparent ? 0.2f : 1f);
    }

//    public void setParamsAnsRoleFrom(TaskViewHolder roleFrom) {
//        roleFrom.setTransparency(true);
//        parentView.getLayoutParams().width = roleFrom.parentView.getWidth();
//        parentView.getLayoutParams().height = roleFrom.parentView.getHeight();
//    }
//
//    public void moveHolderPosition(PointF position) {
//        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) parentView.getLayoutParams();
//        p.setMargins((int) (position.x), (int) (position.y), p.rightMargin, p.bottomMargin);
//    }
//
//    public TaskItem getTask() {
//        return taskItem;
//    }
}
