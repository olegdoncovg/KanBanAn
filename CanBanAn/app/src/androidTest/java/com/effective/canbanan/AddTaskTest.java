package com.effective.canbanan;

import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        TaskItem taskItem = TasksDataModel.getLastCreatedTaskItem();
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
        TaskItem taskItem = TasksDataModel.getLastCreatedTaskItem();
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
        Log.i(TAG, "addNewTask: status=" + status);
        SystemClock.sleep(100);
        final int tasksAmount = TasksDataModel.instance.getTasks(status).size();

        onView(withId(status.getViewId())).perform(click());
        onView(withText(R.string.add_item)).perform(click());
        onView(withId(R.id.enter_task_name)).perform(typeText(taskName));
        SystemClock.sleep(100);
        onView(withId(R.id.enter_task_name)).perform(pressImeActionButton());
        onView(withId(R.id.createTask)).perform(click());

        checkIfTimerStartedLong();

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), tasksAmount + 1);
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
