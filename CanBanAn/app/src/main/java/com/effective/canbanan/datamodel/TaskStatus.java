package com.effective.canbanan.datamodel;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;

import com.effective.canbanan.R;

public enum TaskStatus {
    TO_DO(
            R.color.bg_item_to_do_list,
            R.color.bg_item_to_do_list_blinking, R.id.header_to_do_list),
    IN_PROGRESS(
            R.color.bg_item_in_progress,
            R.color.bg_item_in_progress_blinking, R.id.header_in_progress),
    DONE(
            R.color.bg_item_done,
            R.color.bg_item_done_blinking, R.id.header_well_done);

    private final int colorId;
    private final int blinkingColorId;
    private final int viewId;

    TaskStatus(@ColorRes int colorId, @ColorRes int blinkingColorId, @IdRes int viewId) {
        this.colorId = colorId;
        this.blinkingColorId = blinkingColorId;
        this.viewId = viewId;
    }

    @ColorRes
    public int getColorId() {
        return colorId;
    }

    @IdRes
    public int getViewId() {
        return viewId;
    }

    @IdRes
    public int getBlinkingColorId() {
        return blinkingColorId;
    }
}
