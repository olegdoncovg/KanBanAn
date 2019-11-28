package com.effective.canbanan.viewmodel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;

class TaskViewHolder extends RecyclerView.ViewHolder {
    public final TextView textTitle;
    public final TextView textTime;
    public final View rowView;
    private boolean isActive;

    public TaskViewHolder(@NonNull final View rowView) {
        super(rowView);
        textTitle = rowView.findViewById(R.id.title);
        textTime = rowView.findViewById(R.id.time);
        this.rowView = rowView;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActive = !isActive;
                rowView.setBackgroundColor(isActive ?
                        rowView.getContext().getColor(R.color.bg_item_active) :
                        rowView.getContext().getColor(R.color.bg_item_passive));
            }
        });
    }

    public void bind(TaskItem taskItem) {
        textTitle.setText(taskItem.name);
        textTime.setText(taskItem.getCurrentTime());
    }
}
