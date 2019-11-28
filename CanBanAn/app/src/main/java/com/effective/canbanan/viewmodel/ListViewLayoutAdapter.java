package com.effective.canbanan.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class ListViewLayoutAdapter extends ArrayAdapter<TaskItem> {
    private static final String TAG = ListViewLayoutAdapter.class.getSimpleName();
    private final Context context;
    private final List<TaskItem> values;
    private final TaskStatus taskStatus;

    public ListViewLayoutAdapter(Context context, List<TaskItem> values, TaskStatus taskStatus) {
        super(context, -1, values);
        this.context = context;
        this.values = new ArrayList<>(values);
        this.taskStatus = taskStatus;
    }

    public TaskItem getValue(int position) {
        return values.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            Log.e(TAG, "getView: inflater=null, taskStatus=" + taskStatus + ", position=" + position);
            return new View(context);
        }
        View rowView = inflater.inflate(R.layout.list_view_item, parent, false);
        TextView textTitle = (TextView) rowView.findViewById(R.id.title);
        TextView textTime = (TextView) rowView.findViewById(R.id.time);

        TaskItem dataItem = getValue(position);

        textTitle.setText(dataItem.name);
        textTime.setText(dataItem.getCurrentTime());

        return rowView;
    }
}
