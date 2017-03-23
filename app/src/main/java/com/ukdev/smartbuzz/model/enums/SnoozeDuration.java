package com.ukdev.smartbuzz.model.enums;

import android.util.LongSparseArray;

public enum SnoozeDuration {

    OFF(0L),
    FIVE_MINUTES(300000L),
    TEN_MINUTES(600000L),
    FIFTEEN_MINUTES(900000L),
    TWENTY_MINUTES(1200000L),
    TWENTY_FIVE_MINUTES(1500000L),
    THIRTY_MINUTES(1800000L);

    private final long value;

    SnoozeDuration(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    private static LongSparseArray<SnoozeDuration> sparseArray = new LongSparseArray<>();

    static {
        for (SnoozeDuration duration : SnoozeDuration.values())
            sparseArray.append(duration.getValue(), duration);
    }

    public static SnoozeDuration fromLong(long l) {
        return sparseArray.get(l);
    }

}
