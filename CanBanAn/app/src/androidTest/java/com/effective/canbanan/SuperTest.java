package com.effective.canbanan;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.effective.canbanan.backend.ProviderType;
import com.effective.canbanan.datamodel.TasksDataModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SuperTest {
    private static final String TAG = SuperTest.class.getSimpleName();

    protected static final boolean WATCH_MODE = false;
    protected static int debugCounter = 0;

    @Rule
    public ActivityTestRule<MainActivity> activityMainRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void before() {
        Log.i(TAG, "before");
        debugCounter = 0;
        TasksDataModel.initDebugOnly(getContext(), ProviderType.DEBUG, true);
    }

    protected static Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void useAppContext() {
        Log.i(TAG, "useAppContext");
        assertEquals("com.effective.canbanan", getContext().getPackageName());
    }
}
