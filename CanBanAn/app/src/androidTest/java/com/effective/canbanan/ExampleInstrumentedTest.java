package com.effective.canbanan;

import android.content.Context;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityMainRule =
            new ActivityTestRule(MainActivity.class);
//    @Rule
//    public ActivityTestRule<AddNewTaskActivity> activityAddTaskRule =
//            new ActivityTestRule(AddNewTaskActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.effective.canbanan", appContext.getPackageName());
    }


    @Test
    public void removeAllAndCreateNewTasks() {
        onView(withId(R.id.header_to_do_list)).perform(click());
        onView(withText(R.string.remove_all_item)).perform(click());
        onView(withText(R.string.ok)).perform(click());

        assertEquals(TasksDataModel.instance.getTasks(TaskStatus.TO_DO).size(), 0);

        onView(withId(R.id.header_to_do_list)).perform(click());
        onView(withText(R.string.add_item)).perform(click());
        onView(withId(R.id.enter_task_name)).perform(typeText("taskNew1"));
//        onView(withId(R.id.createTask)).perform(click());
//
//        onView(withId(R.id.header_to_do_list)).perform(click());
//        onView(withId(R.id.addNewTask)).perform(click());
//        onView(withId(R.id.enter_task_name)).perform(typeText("taskNew2"));
//        onView(withId(R.id.createTask)).perform(click());

        SystemClock.sleep(3000);
    }
}
