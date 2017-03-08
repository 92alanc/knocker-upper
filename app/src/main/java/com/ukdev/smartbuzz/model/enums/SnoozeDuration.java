package com.ukdev.smartbuzz.model.enums;

import android.util.SparseArray;

public enum SnoozeDuration {

    FIVE_MINUTES(5),
    TEN_MINUTES(10),
    FIFTEEN_MINUTES(15),
    THIRTY_MINUTES(30);

    private final int value;

    SnoozeDuration(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static SparseArray<SnoozeDuration> sparseArray = new SparseArray<>();

    static {
        for (SnoozeDuration duration : SnoozeDuration.values())
            sparseArray.append(duration.getValue(), duration);
    }

    public static SnoozeDuration fromInt(int i) {
        return sparseArray.get(i);
    }

}
