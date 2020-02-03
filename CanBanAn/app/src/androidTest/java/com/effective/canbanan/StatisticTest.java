package com.effective.canbanan;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StatisticTest extends SuperTest {
    private static final String TAG = StatisticTest.class.getSimpleName();

    @Test
    public void addNewTask() {
        TaskStatus status = TaskStatus.TO_DO;
        Log.i(TAG, "addNewTask: status=" + status);
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SystemClock.sleep(100);
        String taskName1 = "taskNew" + debugCounter++;
        String taskName2 = "taskNew" + debugCounter++;
        String taskName3 = "taskNew" + debugCounter++;
        AddTaskTest.addNewTask(status, taskName1);
        AddTaskTest.addNewTask(status, taskName2);
        AddTaskTest.addNewTask(status, taskName2);
        AddTaskTest.addNewTask(status, taskName3);
        AddTaskTest.addNewTask(status, taskName3);
        AddTaskTest.addNewTask(status, taskName3);
        List<String> namesBeforeRemove = TasksDataModel.instance.getPopularNames(appContext);

        AddTaskTest.removeAll(status);

        List<String> names = TasksDataModel.instance.getPopularNames(appContext);
        Log.i(TAG, "addNewTask: status=" + names);

        assertEquals("namesBeforeRemove!=names after remove", namesBeforeRemove, names);
        assertEquals(3, names.size());

        assertEquals("names.size()", names.size(), 3);

        assertEquals("Wrong name=" + taskName3, taskName3, names.get(0));
        assertEquals("Wrong name=" + taskName2, taskName2, names.get(1));
        assertEquals("Wrong name=" + taskName1, taskName1, names.get(2));
    }
}
