package com.effective.canbanan;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.effective.canbanan.datamodel.SortOption;
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
        String taskName0 = "taskNew" + debugCounter++;
        String taskName1 = "taskNew" + debugCounter++;
        String taskName2 = "taskNew" + debugCounter++;
        AddTaskTest.addNewTask(status, taskName2);
        AddTaskTest.addNewTask(status, taskName2);
        AddTaskTest.addNewTask(status, taskName2);
        AddTaskTest.addNewTask(status, taskName0);
        AddTaskTest.addNewTask(status, taskName1);
        AddTaskTest.addNewTask(status, taskName1);

        List<String> namesBeforeRecentRemove =
                TasksDataModel.instance.getStatisticNames(appContext, SortOption.RECENT, 3);
        List<String> namesBeforePopularRemove =
                TasksDataModel.instance.getStatisticNames(appContext, SortOption.POPULAR, 3);

        AddTaskTest.removeAll(status);

        ///////////////POPULAR
        {
            List<String> namesCount3 =
                    TasksDataModel.instance.getStatisticNames(appContext, SortOption.POPULAR, 3);
            Log.i(TAG, "addNewTask: status=" + namesCount3);

            assertEquals("namesBeforeRemove!=names after remove", namesBeforePopularRemove, namesCount3);
            assertEquals(3, namesCount3.size());

            assertEquals("names.size()", namesCount3.size(), 3);

            assertEqualsName(taskName2, namesCount3, 0);
            assertEqualsName(taskName1, namesCount3, 1);
            assertEqualsName(taskName0, namesCount3, 2);
        }

        {
            List<String> namesCount1 =
                    TasksDataModel.instance.getStatisticNames(appContext, SortOption.POPULAR, 1);
            assertEquals(1, namesCount1.size());
            assertEqualsName(taskName2, namesCount1, 0);
        }

        {
            List<String> namesCount2 =
                    TasksDataModel.instance.getStatisticNames(appContext, SortOption.POPULAR, 2);
            assertEquals(2, namesCount2.size());
            assertEqualsName(taskName2, namesCount2, 0);
            assertEqualsName(taskName1, namesCount2, 1);
        }

        ///////////////RECENT

        {
            List<String> namesCount3 =
                    TasksDataModel.instance.getStatisticNames(appContext, SortOption.RECENT, 3);
            Log.i(TAG, "addNewTask: status=" + namesCount3);

            assertEquals("namesBeforeRemove!=names after remove", namesBeforeRecentRemove, namesCount3);

            assertEquals(3, namesCount3.size());
            assertEqualsName(taskName1, namesCount3, 0);
            assertEqualsName(taskName0, namesCount3, 1);
            assertEqualsName(taskName2, namesCount3, 2);
        }

        {
            List<String> namesCount2 =
                    TasksDataModel.instance.getStatisticNames(appContext, SortOption.RECENT, 2);
            assertEquals(2, namesCount2.size());
            assertEqualsName(taskName1, namesCount2, 0);
            assertEqualsName(taskName0, namesCount2, 1);
        }
    }

    private void assertEqualsName(String name1, @NonNull List<String> list, int pos) {
        String name2 = list.get(pos);
        assertEquals("Wrong name=" + name1 + " !=" + name2 + ", pos=" + pos, name1, name2);
    }
}
