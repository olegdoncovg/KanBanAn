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

    private static int blinkingStatus = 0;

    public static void setBlinkingStatus(int status) {
        blinkingStatus = status;
    }

    public final TextView textTitle;
    public final TextView textTime;
    @NonNull
    public final View mainView;
    private TaskItem taskItem;
//    private boolean isActive;

    public static TaskViewHolder newInstance(@NonNull ViewGroup parent, int viewType,
                                             @NonNull IOnItemActions onItemAction) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_view_item, parent, false);
        return new TaskViewHolder(rowView, onItemAction);
    }

    public TaskViewHolder(@NonNull final View rowView, @NonNull final IOnItemActions onItemAction) {
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
                onItemAction.onContextMenuShown(rowView, taskItem);
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
        mainView.setBackgroundColor(mainView.getContext().getColor(
                taskItem.isBlinking() && (blinkingStatus % 2 == 1) ?
                        taskItem.status.getBlinkingColorId() : taskItem.status.getColorId()));
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

    public TaskItem getTask() {
        return taskItem;
    }
}
