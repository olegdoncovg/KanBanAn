package com.effective.canbanan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.util.DialogUtil;
import com.effective.canbanan.viewmodel.Dropper;
import com.effective.canbanan.viewmodel.IOnItemActions;
import com.effective.canbanan.viewmodel.SwipeController;
import com.effective.canbanan.viewmodel.UiTaskList;

import java.util.ArrayList;
import java.util.List;

import static com.effective.canbanan.AddNewTaskActivity.EXTRA_TASK_NAME;
import static com.effective.canbanan.AddNewTaskActivity.EXTRA_TASK_STATUS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int CODE_REQUEST_NEW_TASK = 111;

    private List<UiTaskList> uiTaskList = new ArrayList<>();
    private PopupMenu contextMenu;

    private final IOnItemActions onItemActions = new IOnItemActions() {
        @Override
        public void onContextMenuShown(@NonNull View view, TaskItem task) {
            showItemContextMenu(view, task);
        }

        @Override
        public void onSwiped(TaskItem viewHolder, SwipeController.Direction direction) {
            TaskStatus newStatus;
            if (viewHolder.status == TaskStatus.TO_DO) {
                newStatus = direction == SwipeController.Direction.LEFT ?
                        TaskStatus.DONE : TaskStatus.IN_PROGRESS;
            } else if (viewHolder.status == TaskStatus.IN_PROGRESS) {
                newStatus = direction == SwipeController.Direction.LEFT ?
                        TaskStatus.TO_DO : TaskStatus.DONE;
            } else if (viewHolder.status == TaskStatus.DONE) {
                newStatus = direction == SwipeController.Direction.LEFT ?
                        TaskStatus.IN_PROGRESS : TaskStatus.TO_DO;
            } else {
                Log.e(TAG, "onSwiped: No correct status in task=" + viewHolder);
                return;
            }
            TasksDataModel.instance.changeTaskCategory(MainActivity.this, viewHolder, newStatus);
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (contextMenu != null) {
            contextMenu.dismiss();
        }
    }

    private final TickTimer tickTimer = new TickTimer();

    public void showItemContextMenu(@NonNull View v, TaskItem taskItem) {
        Activity activity = this;
        if (contextMenu != null) {
            contextMenu.dismiss();
            contextMenu = null;
        }

        Context wrapper = new ContextThemeWrapper(activity, R.style.popup_menu_style);
        contextMenu = new PopupMenu(wrapper, v);
        contextMenu.inflate(R.menu.item_menu);
        contextMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.removeItem) {
                DialogUtil.showYesNoDialog(activity, R.string.remove_all_item, () -> {
                    TasksDataModel.instance.removeTask(this, taskItem);
                    updateUI();
                }, null);
                return true;
            }
            if (item.getItemId() == R.id.approaches) {
                DialogUtil.showTaskOptionDialog(MainActivity.this, this::updateUI, taskItem);
                return true;
            }
            return false;
        });
        contextMenu.getMenu().findItem(R.id.approaches).setVisible(taskItem.status == TaskStatus.IN_PROGRESS);

        contextMenu.show();
    }

    private void updateData() {
        //Clear
        Dropper.instance.init(findViewById(R.id.parentView), this::updateUI, this::createNewTask);
        TasksDataModel.instance.enumerate(this);
        tickTimer.clear();

        //Update UI
        createUI();

        //Timer
        tickTimer.start(this);
    }

    private void createNewTask(TaskStatus status) {
        Intent intent = new Intent(MainActivity.this, AddNewTaskActivity.class);
        intent.putExtra(EXTRA_TASK_STATUS, status.name());
        startActivityForResult(intent, CODE_REQUEST_NEW_TASK);

//        EnterTextAlertDialog enterTextAlertDialog = new EnterTextAlertDialog(this, text -> {
//            if (!TextUtils.isEmpty(text)) {
//                TasksDataModel.instance.addNewTask(MainActivity.this, text, status);
//                updateUI();
//            } else {
//                DialogUtil.showMessage(MainActivity.this, R.string.empty_string);
//            }
//        });
//        enterTextAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == CODE_REQUEST_NEW_TASK) {
            TasksDataModel.instance.addNewTask(this, data.getStringExtra(EXTRA_TASK_NAME),
                    TaskStatus.valueOf(data.getStringExtra(EXTRA_TASK_STATUS)));
            updateUI();
        }
    }

    private void updateUI() {
        uiTaskList.forEach(UiTaskList::updateUI);
    }

    private void createUI() {
        uiTaskList.clear();
        uiTaskList.add(new UiTaskList(this, TaskStatus.TO_DO, R.id.todo_task_list,
                onItemActions, tickTimer));
        uiTaskList.add(new UiTaskList(this, TaskStatus.IN_PROGRESS, R.id.progress_task_list,
                onItemActions, tickTimer));
        uiTaskList.add(new UiTaskList(this, TaskStatus.DONE, R.id.done_task_list,
                onItemActions, tickTimer));
    }

    public void performClickOnTask(TaskItem taskItem) {
        for (UiTaskList taskList : uiTaskList) {
            if (taskList.performClick(taskItem)) {
                break;
            }
        }
    }
}
