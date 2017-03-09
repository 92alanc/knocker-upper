package com.ukdev.smartbuzz.model.enums;

import android.util.SparseArray;

public enum SnoozeDuration {

    OFF(0),
    FIVE_MINUTES(5 * 60 * 1000),
    TEN_MINUTES(10 * 60 * 1000),
    FIFTEEN_MINUTES(15 * 60 * 1000),
    THIRTY_MINUTES(30 * 60 * 1000);

    private final long value;

    SnoozeDuration(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    private static SparseArray<SnoozeDuration> sparseArray = new SparseArray<>();

    static {
        for (SnoozeDuration duration : SnoozeDuration.values())
            sparseArray.append((int)duration.getValue(), duration);
    }

    public static SnoozeDuration fromInt(long i) {
        return sparseArray.get((int)i);
    }

}
