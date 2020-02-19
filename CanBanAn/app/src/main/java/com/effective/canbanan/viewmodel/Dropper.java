package com.effective.canbanan.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import com.effective.canbanan.BuildConfig;
import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.util.DialogUtil;

import java.util.function.Consumer;

public class Dropper {
    private static final String TAG = Dropper.class.getSimpleName();
    public static Dropper instance = new Dropper();

    //    private DropperLayout dropItemSurface;
//    private TaskViewHolder hoverTaskHolder;
    private Runnable updateUI;
    private Consumer<TaskStatus> createNewTask;
    private TaskItem catchedTaskItem;

    //    private PointF startPoint = new PointF();
//    private PointF diffPoint = new PointF();
    private DropState state = DropState.CANCELED;
//    private long lastTouchEvent = 0;
//    private static final long LAUNCH_EVENT_TIME = 50;
//
//    private View.OnTouchListener onTouchListener = (v, event) -> {
//        onItemTouchEvent(v, event, TasksDataModel.NO_TASK_ID);
//        return false;
//    };

    public void init(ViewGroup parentView, Runnable updateUI, Consumer<TaskStatus> createNewTask) {
        clear();
        this.updateUI = updateUI;
        this.createNewTask = createNewTask;
//        this.dropItemSurface = parentView.findViewById(R.id.dropperView);
//        this.dropItemSurface.setOnTouchListener(onTouchListener);

        for (TaskStatus status : TaskStatus.values()) {
            parentView.findViewById(status.getViewId()).setOnClickListener(v -> {
                onClickTaskCategory(v, status);
            });
        }
    }

    private void onClickTaskCategory(View v, TaskStatus status) {
        final Context context = v == null ? null : v.getContext();
        if (context == null) {
            Log.e(TAG, "onClickTaskCategory: v=" + v + ", context=" + context);
            return;
        }
        if (state == DropState.HOVERING) {
            changeTaskCategory(context, status);
        } else {
            showPopupMenu(v, status);
        }
    }


    private void showPopupMenu(@NonNull View v, TaskStatus status) {
        Activity activity = (Activity) v.getContext();
        if (activity == null) {
            Log.e(TAG, "showPopupMenu: activity=null");
            return;
        }

        Context wrapper = new ContextThemeWrapper(activity, R.style.popup_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        popupMenu.inflate(R.menu.header_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.addNewTask) {
                if (createNewTask != null) {
                    createNewTask.accept(status);
                }
                return true;
            }
            if (item.getItemId() == R.id.removeAll) {
                DialogUtil.showYesNoDialog(activity, R.string.remove_all_item, () -> {
                    TasksDataModel.instance.removeTasks(activity, status);
                    updateUI.run();
                }, null);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void changeTaskCategory(@NonNull Context context, TaskStatus status) {
        state = DropState.PUT_IN_NEW_AREA;

        if (TasksDataModel.instance.changeTaskCategory(context, catchedTaskItem, status)) {
            if (updateUI != null) {
                updateUI.run();
            } else {
                Log.e(TAG, "changeTaskCategory: updateUI=" + updateUI);
            }
        } else {
            String mes = "changeTaskCategory: TasksDataModel should contain taskItem=" + catchedTaskItem;
            Log.e(TAG, mes);
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException(mes);
            }
        }

        clear();
    }


    public void onItemSelectToMove(TaskViewHolder viewHolderToHover, TaskItem taskItem) {
        clear();
        state = DropState.LONG_CLICK;
        catchedTaskItem = taskItem;
        viewHolderToHover.setTransparency(true);

//        PointUtil.copyPosition(viewHolderToHover.parentView, startPoint);
//
//        viewHolderToHover.parentView.setOnTouchListener(onTouchListener);
//
//        hoverTaskHolder = TaskViewHolder.newInstance(dropItemSurface, TaskListAdapter.ItemType.TASK.ordinal());
//        hoverTaskHolder.bind(taskItem);
//        hoverTaskHolder.setParamsAnsRoleFrom(viewHolderToHover);
//        dropItemSurface.addView(hoverTaskHolder.parentView);
//        hoverTaskHolder.moveHolderPosition(startPoint);

        state = DropState.HOVERING;
        Log.d(TAG, "onItemSelectToMove: state=" + state);
    }

//    public void onItemTouchEvent(View v, MotionEvent event, int taskId) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//            case MotionEvent.ACTION_CANCEL:
//
//                break;
//            case MotionEvent.ACTION_OUTSIDE:
//
//                break;
//        }
//        Log.d(TAG, "onItemTouchEvent: action=" + event.getAction() + ", taskId=" + taskId + ", v=" + v);
//        if (state == DropState.HOVERING) {
//            if (!skipTouch()) {
//                int[] parentPos = new int[2];
//                dropItemSurface.getLocationInWindow(parentPos);
//
//                diffPoint.x = event.getRawX() - parentPos[0];
//                diffPoint.y = event.getRawY() - parentPos[1];
//
//                hoverTaskHolder.moveHolderPosition(diffPoint);
//                Log.d(TAG, "onItemTouchEvent: diffPoint=" + diffPoint);
//                dropItemSurface.invalidate();
//                dropItemSurface.requestLayout();
//            }
//        }
//    }
//
//    private boolean skipTouch() {
//        long time = TickTimer.currentTimeMillis();
//        long diff = time - lastTouchEvent;
//        if (diff < LAUNCH_EVENT_TIME) {
//            return true;
//        } else {
//            lastTouchEvent = time;
//            return false;
//        }
//    }

    public void clear() {
//        if (hoverTaskHolder != null) {
//            if (dropItemSurface != null) {
//                dropItemSurface.removeView(hoverTaskHolder.parentView);
//            }
//            hoverTaskHolder = null;
//        }
//        if (dropItemSurface != null) {
//            dropItemSurface.removeAllViews();
//            hoverTaskHolder = null;
//        }
        catchedTaskItem = null;
        state = DropState.CANCELED;
    }

    private enum DropState {
        LONG_CLICK,
        HOVERING,
        PUT_IN_NEW_AREA,
        CANCELED
    }
}
