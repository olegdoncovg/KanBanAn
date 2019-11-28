package com.effective.canbanan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.viewmodel.TaskListAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateData();
    }

    private void updateData() {
        TasksDataModel.instance.enumerate();

        RecyclerView listTodo = findViewById(R.id.todo_task_list);
        addItemsByLayout(listTodo, TaskStatus.TO_DO);

        RecyclerView listProgress = findViewById(R.id.progress_task_list);
        addItemsByLayout(listProgress, TaskStatus.IN_PROGRESS);

        RecyclerView listDone = findViewById(R.id.done_task_list);
        addItemsByLayout(listDone, TaskStatus.DONE);
    }

    private void addItemsByLayout(RecyclerView recyclerView, TaskStatus taskStatus) {
        TaskListAdapter adapter = new TaskListAdapter(taskStatus);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.updateData();
    }
}
