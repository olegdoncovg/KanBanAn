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
    @NonNull
    private final IOnItemActions onItemAction;

    public TaskListAdapter(TaskStatus taskStatus, @NonNull IOnItemActions onItemAction) {
        this.taskStatus = taskStatus;
        this.onItemAction = onItemAction;
    }

    public void updateData() {
        values.clear();
        values.addAll(TasksDataModel.instance.getTasks(taskStatus));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaskViewHolder.newInstance(parent, viewType, onItemAction);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(holder.mainView.getContext(), values.get(position));
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
        return currentTime -> {
            TaskViewHolder.setBlinkingStatus((int) ((currentTime / 1000) % 2));
            TaskListAdapter.this.notifyDataSetChanged();
        };
    }

    public boolean contain(TaskItem taskItem) {
        for (TaskItem item : values) {
            if (item.id == taskItem.id) {
                return true;
            }
        }
        return false;
    }

    public boolean performClick(RecyclerView ownerRecyclerView, TaskItem taskItem) {
        for (int pos = 0; pos < values.size(); pos++) {
            TaskItem item = values.get(pos);
            if (item.id == taskItem.id) {
                ownerRecyclerView.scrollToPosition(pos);
                RecyclerView.ViewHolder holder = ownerRecyclerView.findViewHolderForAdapterPosition(pos);
                if (!(holder instanceof TaskViewHolder)) {
                    return false;
                }

                ((TaskViewHolder) holder).performClick(onItemAction);
                return true;
            }
        }
        return false;
    }

//    public enum ItemType {
//        TASK
//    }
}
