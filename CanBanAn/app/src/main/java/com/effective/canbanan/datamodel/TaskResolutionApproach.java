package com.effective.canbanan.datamodel;

import androidx.annotation.StringRes;

import com.effective.canbanan.R;

public enum TaskResolutionApproach {
    SPECIAL_BOOK_READING(R.string.approach_special_book_reading),
    WALKING(R.string.approach_walking),
    APPROACH_JUSTIFICATION(R.string.approach_justification),
    TONNY_STEPS(R.string.approach_tony_steps);

    @StringRes
    private final int itemTextId;

    TaskResolutionApproach(@StringRes int itemTextId) {
        this.itemTextId = itemTextId;
    }
}
