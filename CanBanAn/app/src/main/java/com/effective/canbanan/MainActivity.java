package com.effective.canbanan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.viewmodel.Dropper;
import com.effective.canbanan.viewmodel.TaskListAdapter;

import static com.effective.canbanan.AddNewTaskActivity.EXTRA_TASK_NAME;
import static com.effective.canbanan.AddNewTaskActivity.EXTRA_TASK_STATUS;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_REQUEST_NEW_TASK = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateData();
    }

    private final TickTimer tickTimer = new TickTimer();

    private void updateData() {
        //Clear
        Dropper.instance.init(findViewById(R.id.parentView), this::updateUI, this::createNewTask);
        TasksDataModel.instance.enumerate();
        tickTimer.clear();

        //Update UI
        updateUI();

        //Timer
        tickTimer.start(this);
    }

    private void createNewTask(TaskStatus status) {
        Intent intent = new Intent(MainActivity.this, AddNewTaskActivity.class);
        intent.putExtra(EXTRA_TASK_STATUS, status.name());
        startActivityForResult(intent, CODE_REQUEST_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == CODE_REQUEST_NEW_TASK) {
            TasksDataModel.instance.addNewTask(data.getStringExtra(EXTRA_TASK_NAME),
                    TaskStatus.valueOf(data.getStringExtra(EXTRA_TASK_STATUS)));
            updateUI();
        }
    }

    private void updateUI() {
        RecyclerView listTodo = findViewById(R.id.todo_task_list);
        addItemsByLayout(listTodo, TaskStatus.TO_DO);

        RecyclerView listProgress = findViewById(R.id.progress_task_list);
        addItemsByLayout(listProgress, TaskStatus.IN_PROGRESS);

        RecyclerView listDone = findViewById(R.id.done_task_list);
        addItemsByLayout(listDone, TaskStatus.DONE);
    }

    private void addItemsByLayout(RecyclerView recyclerView, TaskStatus taskStatus) {
        TaskListAdapter adapter = new TaskListAdapter(taskStatus);
        if (taskStatus == TaskStatus.IN_PROGRESS) {
            tickTimer.addTickListener(adapter.getTimeUpdateListener());
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.updateData();
    }
}
