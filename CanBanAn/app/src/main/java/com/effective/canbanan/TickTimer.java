package com.effective.canbanan;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TickTimer {
    private static final String TAG = TickTimer.class.getSimpleName();

    private static long currentTimeMillis = 0;

    public static void setCurrentTimeMillisDebugOnly(long currentTimeMillis) {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException("setCurrentTimeMillis: Tray apply currentTimeMillis=" + currentTimeMillis + " in DEBUG mode");
        }
        TickTimer.currentTimeMillis = currentTimeMillis;
    }

    public static long currentTimeMillis() {
        if (currentTimeMillis != 0 && !BuildConfig.DEBUG) {
            throw new IllegalStateException("setCurrentTimeMillis: Tray apply currentTimeMillis=" + currentTimeMillis + " in DEBUG mode");
        }
        return currentTimeMillis != 0 ? currentTimeMillis : System.currentTimeMillis();
    }

    private final List<Consumer<Long>> timeUpdateListeners = new ArrayList<>();

    public void addTickListener(Consumer<Long> timeUpdateListener) {
        timeUpdateListeners.add(timeUpdateListener);
    }

    public void clear() {
        timeUpdateListeners.clear();
    }

    public void start(final Activity activity) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    final long time = TickTimer.currentTimeMillis();
                    for (Consumer<Long> longConsumer : timeUpdateListeners) {
                        if (longConsumer == null) {
                            continue;
                        }
                        longConsumer.accept(time);
                    }
                });
            }
        }, 0, 1000);
    }
}
