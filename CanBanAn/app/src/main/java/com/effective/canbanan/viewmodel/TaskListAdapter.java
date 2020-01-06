package com.effective.canbanan.viewmodel;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return TaskViewHolder.newInstance(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(values.get(position));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return ItemType.TASK.ordinal();
//    }

    //Probably will be applied only for TaskStatus.IN_PROGRESS
    public Consumer<Long> getTimeUpdateListener() {
        return currentTime -> notifyDataSetChanged();
    }

//    public enum ItemType {
//        TASK
//    }
}
