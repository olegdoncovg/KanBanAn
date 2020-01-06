package com.effective.canbanan.viewmodel;

import android.util.Log;
import android.view.ViewGroup;

import com.effective.canbanan.BuildConfig;
import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

public class Dropper {
    private static final String TAG = Dropper.class.getSimpleName();
    public static Dropper instance = new Dropper();

    //    private DropperLayout dropItemSurface;
//    private TaskViewHolder hoverTaskHolder;
    private Runnable updateUI;
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

    public void init(ViewGroup parentView, Runnable updateUI) {
        clear();
        this.updateUI = updateUI;
//        this.dropItemSurface = parentView.findViewById(R.id.dropperView);
//        this.dropItemSurface.setOnTouchListener(onTouchListener);

        parentView.findViewById(R.id.header_to_do_list).setOnClickListener(v -> {
            changeTaskCategory(TaskStatus.TO_DO);
        });

        parentView.findViewById(R.id.header_in_progress).setOnClickListener(v -> {
            changeTaskCategory(TaskStatus.IN_PROGRESS);
        });

        parentView.findViewById(R.id.header_well_done).setOnClickListener(v -> {
            changeTaskCategory(TaskStatus.DONE);
        });
    }

    private void changeTaskCategory(TaskStatus status) {
        if (state != DropState.HOVERING) {
            return;
        }
        state = DropState.PUT_IN_NEW_AREA;

        if (TasksDataModel.instance.changeTaskCategory(catchedTaskItem, status)) {
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


    public void onItemLongClick(TaskViewHolder viewHolderToHover, TaskItem taskItem) {
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
        Log.d(TAG, "onItemLongClick: state=" + state);
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
//        long time = System.currentTimeMillis();
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