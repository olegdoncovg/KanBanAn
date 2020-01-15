package com.effective.canbanan.datamodel;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;

import com.effective.canbanan.R;

public enum TaskStatus {
    TO_DO(R.color.bg_item_to_do_list, R.id.header_to_do_list),
    IN_PROGRESS(R.color.bg_item_in_progress, R.id.header_in_progress),
    DONE(R.color.bg_item_done, R.id.header_well_done);

    private final int colorId;
    private final int viewId;

    TaskStatus(@ColorRes int colorId, @IdRes int viewId) {
        this.colorId = colorId;
        this.viewId = viewId;
    }

    public @ColorRes
    int getColorId() {
        return colorId;
    }

    public @IdRes
    int getViewId() {
        return viewId;
    }
}
