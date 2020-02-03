package com.effective.canbanan;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.effective.canbanan.backend.ProviderDebug;
import com.effective.canbanan.backend.ProviderPreferences;
import com.effective.canbanan.datamodel.TaskItem;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProviderPreferencesTest extends SuperTest {
    private static final String TAG = ProviderPreferencesTest.class.getSimpleName();
    private static final String FILE_NAME_SUFFIX_FOR_TEST = "_just_forTest_" + TAG;

    @Test
    public void readWriteInstant() {
        readWrite(true);
    }

    @Test
    public void readWriteNotInstant() {
        readWrite(false);
    }

    public void readWrite(boolean instantAction) {
        Log.i(TAG, "useAppContext: instantAction=" + instantAction);
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        ProviderPreferences providerPreferences = new ProviderPreferences(FILE_NAME_SUFFIX_FOR_TEST);
        ProviderDebug providerDebug = new ProviderDebug();

        List<TaskItem> taskItems1 = providerDebug.getItems(appContext);
        providerPreferences.updateServerInfo(appContext, Collections.unmodifiableList(taskItems1), instantAction);
        SystemClock.sleep(100);
        List<TaskItem> taskItems2 = providerPreferences.getItems(appContext);

        assertEquals("size equals", taskItems1.size(), taskItems2.size());
        for (int i = 0; i < taskItems1.size(); i++) {
            TaskItem t1 = taskItems1.get(i);
            TaskItem t2 = taskItems2.get(i);
            assertEquals("equals TaskItem " + i, t1, t2);
        }
    }
}
