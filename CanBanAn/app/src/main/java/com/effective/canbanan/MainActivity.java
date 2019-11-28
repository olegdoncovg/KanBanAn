package com.effective.canbanan;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.viewmodel.ListViewLayoutAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateData();
    }

    private void updateData() {
        TasksDataModel.instance.enumerate();

        //TODO fill layout
        ListView listTodo = findViewById(R.id.todo_task_list);
        addItemsByLayout(listTodo, TaskStatus.TO_DO);

        ListView listProgress = findViewById(R.id.progress_task_list);
        addItemsByLayout(listProgress, TaskStatus.IN_PROGRESS);

        ListView listDone = findViewById(R.id.done_task_list);
        addItemsByLayout(listDone, TaskStatus.DONE);
    }

    private void addItemsByLayout(ListView todo, TaskStatus taskStatus) {
        List<TaskItem> tasks = TasksDataModel.instance.getTasks(taskStatus);
        ArrayAdapter<TaskItem> arrayAdapter = new ListViewLayoutAdapter(this, tasks, taskStatus);
        todo.setAdapter(arrayAdapter);
    }
}
