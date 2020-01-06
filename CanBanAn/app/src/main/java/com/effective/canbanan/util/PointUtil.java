package com.effective.canbanan.util;

import android.graphics.PointF;
import android.view.View;

public class PointUtil {
    public static void copyPosition(View positionFrom, PointF startHover) {
        int[] location = new int[2];
        positionFrom.getLocationOnScreen(location);
        startHover.x = location[0];
        startHover.y = location[1];
    }
}
