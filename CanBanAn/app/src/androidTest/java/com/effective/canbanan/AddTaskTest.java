package com.effective.canbanan;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.util.TimeUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddTaskTest extends SuperTest {
    private static final String TAG = AddTaskTest.class.getSimpleName();

    @Test
    public void removeAll() {
        Log.i(TAG, "removeAll");
        SystemClock.sleep(100);
        removeAll(TaskStatus.TO_DO);
        SystemClock.sleep(100);
        removeAll(TaskStatus.IN_PROGRESS);
        SystemClock.sleep(100);
        removeAll(TaskStatus.DONE);
    }

    public static void removeAll(TaskStatus status) {
        Log.i(TAG, "removeAll: status=" + status);
        onView(withId(status.getViewId())).perform(click());
        onView(withText(R.string.remove_all_item)).perform(click());
        onView(withText(R.string.ok)).perform(click());

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), 0);

        if (WATCH_MODE) SystemClock.sleep(3000);
    }

    @Test
    public void addBlinkingTask() {
        Log.i(TAG, "addBlinkingTask");
        SystemClock.sleep(100);

        String taskNameBlinking = "taskBlinkingBefore_" + debugCounter++;
        String taskName = "taskBlinkingAfter_" + debugCounter++;
        long time = TickTimer.currentTimeMillis();
        long timeTotal = time - TimeUtil.getTimeMillis(99, 0, 0);
        long timeActive_21min_BeforeNow = time - TimeUtil.getTimeMillis(0, 21, 0);
        long timeActive_9min_BeforeNow = time - TimeUtil.getTimeMillis(0, 9, 0);

        TasksDataModel.addNewTaskDebugOnly(getContext(),
                taskName, timeTotal, timeActive_9min_BeforeNow, TaskStatus.IN_PROGRESS);
        SystemClock.sleep(100);
        final TaskItem task_9min_BeforeNow = TasksDataModel.getLastCreatedTaskItemDebugOnly();

        TasksDataModel.addNewTaskDebugOnly(getContext(),
                taskName, timeTotal, 0, TaskStatus.DONE);
        SystemClock.sleep(100);
        final TaskItem task_21min_BeforeNow_DONE = TasksDataModel.getLastCreatedTaskItemDebugOnly();

        TasksDataModel.addNewTaskDebugOnly(getContext(),
                taskNameBlinking, timeTotal, timeActive_21min_BeforeNow, TaskStatus.IN_PROGRESS);
        SystemClock.sleep(100);
        final TaskItem task_21min_BeforeNow = TasksDataModel.getLastCreatedTaskItemDebugOnly();

        assertFalse("task_21min_BeforeNow_DONE", task_21min_BeforeNow_DONE.isBlinking());
        assertTrue("task_21min_BeforeNow IN_PROGRESS", task_21min_BeforeNow.isBlinking());
        assertFalse("task_9min_BeforeNow IN_PROGRESS", task_9min_BeforeNow.isBlinking());
    }


    @Test
    public void addNewTask() {
        Log.i(TAG, "addNewTask");
        SystemClock.sleep(100);
        addNewTask(TaskStatus.TO_DO, "taskNew" + debugCounter++);
        checkIfTimerStartedString(TaskStatus.TO_DO);
        SystemClock.sleep(100);
        addNewTask(TaskStatus.IN_PROGRESS, "taskNew" + debugCounter++);
        checkIfTimerStartedString(TaskStatus.IN_PROGRESS);
        SystemClock.sleep(100);
        addNewTask(TaskStatus.DONE, "taskNew" + debugCounter++);
        checkIfTimerStartedString(TaskStatus.DONE);
    }

    //test for timer started (Only after add new task)
    //Long period: 1100 ms
    static void checkIfTimerStartedLong() {
        TaskItem taskItem = TasksDataModel.getLastCreatedTaskItemDebugOnly();
        TaskStatus status = taskItem.status;
        String timeS1 = taskItem.getCurrentTime(getContext());
        SystemClock.sleep(1100);
        String timeS2 = taskItem.getCurrentTime(getContext());

        if (status == TaskStatus.IN_PROGRESS) {
            assertNotEquals("Timer for task not started!!! " + timeS1 + " != " + timeS1, timeS1, timeS2);
        } else {
            assertEquals("Timer for task WRONGLY stated!!! " + timeS1 + " == " + timeS1, timeS1, timeS2);
        }
    }

    //test for timer started (Only after add new task)
    static void checkIfTimerStartedString(TaskStatus status) {
        TaskItem taskItem = TasksDataModel.getLastCreatedTaskItemDebugOnly();
        long time1 = taskItem.getCurrentTimeInLong();
        SystemClock.sleep(100);
        long time2 = taskItem.getCurrentTimeInLong();

        assertEquals("Task status " + status + "!=" + taskItem.status, status, taskItem.status);

        if (status == TaskStatus.IN_PROGRESS) {
            assertNotEquals("Timer for task not started!!! " + time1 + " != " + time2, time1, time2);
        } else {
            assertEquals("Timer for task WRONGLY stated!!! " + time1 + " == " + time2, time1, time2);
        }
    }


    static void addNewTask(TaskStatus status, String taskName) {
        addNewTask(status, taskName, true);
    }

    /**
     * @param checkTimerForStart false only if we use TickTimer.setCurrentTimeMillisDebugOnly
     */
    static void addNewTask(TaskStatus status, String taskName, boolean checkTimerForStart) {
        Log.i(TAG, "addNewTask: status=" + status);
        SystemClock.sleep(100);
        final int tasksAmount = TasksDataModel.instance.getTasks(status).size();

        onView(withId(status.getViewId())).perform(click());
        onView(withText(R.string.add_item)).perform(click());
        onView(withId(R.id.enter_task_name)).perform(typeText(taskName));
        SystemClock.sleep(100);
        onView(withId(R.id.enter_task_name)).perform(pressImeActionButton());
        onView(withId(R.id.createTask)).perform(click());

        if (checkTimerForStart) {
            checkIfTimerStartedLong();
        }

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), tasksAmount + 1);
    }

    public static void showContextMenuForTask(MainActivity activity, final TaskItem createdTask) {
        new Handler(Looper.getMainLooper()).post(() -> {
            activity.performClickOnTask(createdTask);
        });
        SystemClock.sleep(200);
    }

    @Test
    public void createNewTasks() {
        Log.i(TAG, "createNewTasks");
        SystemClock.sleep(100);
        createNewTasks(TaskStatus.TO_DO);
        SystemClock.sleep(100);
        createNewTasks(TaskStatus.IN_PROGRESS);
        SystemClock.sleep(100);
        createNewTasks(TaskStatus.DONE);

        //Clear time for all test
        SystemClock.sleep(100);
        clearTimeForAll(TaskStatus.TO_DO);
        clearTimeForAll(TaskStatus.IN_PROGRESS);
        clearTimeForAll(TaskStatus.DONE);
    }

    private static void clearTimeForAll(TaskStatus status) {
        Log.i(TAG, "removeAll: status=" + status);
        final int countBefore = TasksDataModel.instance.getTasks(status).size();
        onView(withId(status.getViewId())).perform(click());
        onView(withText(R.string.clear_time_for_all)).perform(click());
        onView(withText(R.string.ok)).perform(click());

        SystemClock.sleep(100);
        final List<TaskItem> tasks = TasksDataModel.instance.getTasks(status);
        assertEquals(countBefore, tasks.size());

        long time = TickTimer.currentTimeMillis();
        for (int i = 0; i < tasks.size(); i++) {
            final TaskItem item = tasks.get(i);
            if (status == TaskStatus.IN_PROGRESS) {
                assertTrue("clearTimeForAll: after clear DynamicPartTime for i=" + i + ", " + item,
                        item.getDynamicPartTime() > (TickTimer.currentTimeMillis() - time));
            } else {
                assertEquals("clearTimeForAll: after clear DynamicPartTime for i=" + i + ", " + item,
                        item.getDynamicPartTime(), 0);
            }
            assertEquals("clearTimeForAll: after clear StaticPartTime for i=" + i + ", " + item,
                    item.getStaticPartTime(), 0);
        }
        SystemClock.sleep(100);
    }

    static void createNewTasks(TaskStatus status) {
        Log.i(TAG, "createNewTasks: status=" + status);
        removeAll(status);

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), 0);

        addNewTask(status, "taskNew" + debugCounter++);

        addNewTask(status, "taskNew" + debugCounter++);

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), 2);

        if (WATCH_MODE) SystemClock.sleep(3000);
    }
}
