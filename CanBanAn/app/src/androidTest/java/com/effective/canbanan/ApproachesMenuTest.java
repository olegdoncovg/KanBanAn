package com.effective.canbanan;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApproachesMenuTest extends SuperTest {
    private static final String TAG = ApproachesMenuTest.class.getSimpleName();

    @Test
    public void applyApproaches() {
        Log.i(TAG, "applyApproaches");

        //Stop timer
        long time = System.currentTimeMillis();
        TickTimer.setCurrentTimeMillisDebugOnly(time);

        //Create task to do
        SystemClock.sleep(100);
        final String textName = "taskNew" + debugCounter++;
        AddTaskTest.addNewTask(TaskStatus.IN_PROGRESS, textName, false);
        final TaskItem createdTask = TasksDataModel.getLastCreatedTaskItemDebugOnly();
        final long createdTaskTimeCreation = time;
        assertFalse("!isBlinking", createdTask.isBlinking());

        //Spend some time
        final long dimeDif1 = TimeUtil.getTimeMillis(0, 1, 0);
        time += dimeDif1;
        TickTimer.setCurrentTimeMillisDebugOnly(time);
        SystemClock.sleep(100);
        assertFalse("!isBlinking", createdTask.isBlinking());

        //Spend some time
        final long dimeDif2 = TaskItem.TIME_TO_START_BLINKING;
        time += dimeDif2;
        TickTimer.setCurrentTimeMillisDebugOnly(time);
        SystemClock.sleep(100);
        final long approachTime = time;
        final long finalStaticTime = dimeDif1 + dimeDif2;

        {
            final long dimeDif = dimeDif1 + dimeDif2;
            assertEquals("timeStartActive is createdTaskTimeCreation", createdTask.timeStartActive, createdTaskTimeCreation);//No changed after creation
            assertEquals("getDynamicPartTime=dimeDif", createdTask.getDynamicPartTime(), dimeDif);
            assertEquals("getStaticPartTime==0", createdTask.getStaticPartTime(), 0);
            assertTrue("isBlinking", createdTask.isBlinking());
        }

        //Make approach
        AddTaskTest.showContextMenuForTask(getActivity(), createdTask);
        SystemClock.sleep(100);
        onView(withText(R.string.approaches_to_fast_resolution)).perform(click());
        SystemClock.sleep(100);
        onView(withText(R.string.approach_walking)).perform(click());

        {
            //check time total and time active
            final TaskItem finalTask = TasksDataModel.instance.getTask(createdTask.id);
            assertEquals("timeStartActive is finalTaskTimeCreation", finalTask.timeStartActive, approachTime);//No changed after approach
            assertEquals("getDynamicPartTime=0", finalTask.getDynamicPartTime(), 0);//No spend time after approach
            assertEquals("getStaticPartTime==dimeDif", finalTask.getStaticPartTime(), finalStaticTime);
            assertFalse("!isBlinking", finalTask.isBlinking());//getDynamicPartTime < TIME_TO_START_BLINKING
        }

        //Spend some time
        final long dimeDif3 = TaskItem.TIME_TO_START_BLINKING + TimeUtil.getTimeMillis(0, 1, 0);
        ;
        time += dimeDif3;
        TickTimer.setCurrentTimeMillisDebugOnly(time);
        SystemClock.sleep(100);

        {
            //check time total and time active
            final TaskItem finalTask = TasksDataModel.instance.getTask(createdTask.id);
            assertEquals("timeStartActive is finalTaskTimeCreation", finalTask.timeStartActive, approachTime);//No changed after approach
            assertEquals("getDynamicPartTime=dimeDif3", finalTask.getDynamicPartTime(), dimeDif3);
            assertEquals("getStaticPartTime==dimeDif", finalTask.getStaticPartTime(), finalStaticTime);
            assertTrue("isBlinking", finalTask.isBlinking());
        }

        SystemClock.sleep(WATCH_MODE ? 2000 : 100);
    }
}
