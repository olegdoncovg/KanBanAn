package com.effective.canbanan;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.effective.canbanan.backend.ProviderType;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddTaskInstrumentedTest {
    private static final String TAG = AddTaskInstrumentedTest.class.getSimpleName();

    private static final boolean WATCH_MODE = false;
    private static int debugCounter = 0;

    @Rule
    public ActivityTestRule<MainActivity> activityMainRule =
            new ActivityTestRule(MainActivity.class);
//    @Rule
//    public ActivityTestRule<AddNewTaskActivity> activityAddTaskRule =
//            new ActivityTestRule(AddNewTaskActivity.class);

    @Before
    public void before() {
        Log.i(TAG, "before");
        debugCounter = 0;
        TasksDataModel.init(ProviderType.DEBUG);
    }

    @Test
    public void useAppContext() {
        Log.i(TAG, "useAppContext");
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.effective.canbanan", appContext.getPackageName());
    }

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

    public void removeAll(TaskStatus status) {
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
        addNewTask(TaskStatus.TO_DO);
        SystemClock.sleep(100);
        addNewTask(TaskStatus.IN_PROGRESS);
        SystemClock.sleep(100);
        addNewTask(TaskStatus.DONE);
    }

    public void addNewTask(TaskStatus status) {
        Log.i(TAG, "addNewTask: status=" + status);
        SystemClock.sleep(100);
        final int tasksAmount = TasksDataModel.instance.getTasks(status).size();

        onView(withId(status.getViewId())).perform(click());
        onView(withText(R.string.add_item)).perform(click());
        onView(withId(R.id.enter_task_name)).perform(typeText("taskNew" + debugCounter++));
        SystemClock.sleep(100);
        onView(withId(R.id.enter_task_name)).perform(pressImeActionButton());
        onView(withId(R.id.createTask)).perform(click());

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

    public void createNewTasks(TaskStatus status) {
        Log.i(TAG, "createNewTasks: status=" + status);
        removeAll(status);

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), 0);

        addNewTask(status);

        addNewTask(status);

        SystemClock.sleep(100);
        assertEquals(TasksDataModel.instance.getTasks(status).size(), 2);

        if (WATCH_MODE) SystemClock.sleep(3000);
    }
}
