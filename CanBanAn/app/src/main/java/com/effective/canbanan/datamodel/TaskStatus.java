package com.effective.canbanan.datamodel;

import com.effective.canbanan.R;

public enum TaskStatus {
    TO_DO(R.color.bg_item_to_do_list),
    IN_PROGRESS(R.color.bg_item_in_progress),
    DONE(R.color.bg_item_done);

    private final int colorId;

    TaskStatus(int colorId) {
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }
}
