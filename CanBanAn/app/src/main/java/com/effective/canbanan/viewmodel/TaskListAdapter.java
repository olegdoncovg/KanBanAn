package com.effective.canbanan.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private static final String TAG = TaskListAdapter.class.getSimpleName();
    private final List<TaskItem> values = new ArrayList<>();
    private final TaskStatus taskStatus;

    public TaskListAdapter(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void updateData() {
        values.clear();
        values.addAll(TasksDataModel.instance.getTasks(taskStatus));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_view_item, parent, false);
        TaskViewHolder vh = new TaskViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(values.get(position));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    //Probably will be applied only for TaskStatus.IN_PROGRESS
    public Consumer<Long> getTimeUpdateListener() {
        return new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                notifyDataSetChanged();
            }
        };
    }
}
