package com.effective.canbanan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.util.DialogUtil;
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

    public void showItemContextMenu(@NonNull View v, TaskItem taskItem) {
        Activity activity = this;

        Context wrapper = new ContextThemeWrapper(activity, R.style.popup_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        popupMenu.inflate(R.menu.item_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.removeItem) {
                DialogUtil.showYesNoDialog(activity, R.string.remove_all_item, () -> {
                    TasksDataModel.instance.removeTask(taskItem);
                    updateUI();
                }, null);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

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
        TaskListAdapter adapter = new TaskListAdapter(taskStatus, this::showItemContextMenu);
        if (taskStatus == TaskStatus.IN_PROGRESS) {
            tickTimer.addTickListener(adapter.getTimeUpdateListener());
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.updateData();
    }
}
