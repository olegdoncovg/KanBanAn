package com.effective.canbanan.util;

public class TimeUtil {
    private static final int SEC_MS = 1000;
    private static final int MIN_MS = SEC_MS * 60;
    private static final int HOUR_MS = MIN_MS * 60;

    public static long getTimeMillis(int hour, int min, int sec) {
        return HOUR_MS * hour + min * MIN_MS + sec * SEC_MS;
    }
}
