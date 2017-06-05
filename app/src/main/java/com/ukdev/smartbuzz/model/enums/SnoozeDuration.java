package com.ukdev.smartbuzz.model.enums;

import android.util.LongSparseArray;

/**
 * A snooze duration
 *
 * @author Alan Camargo
 */
public enum SnoozeDuration {

    /**
     * No snooze duration
     */
    OFF(0L),

    /**
     * 5 minutes
     */
    FIVE_MINUTES(300000L),

    /**
     * 10 minutes
     */
    TEN_MINUTES(600000L),

    /**
     * 15 minutes
     */
    FIFTEEN_MINUTES(900000L),

    /**
     * 20 minutes
     */
    TWENTY_MINUTES(1200000L),

    /**
     * 25 minutes
     */
    TWENTY_FIVE_MINUTES(1500000L),

    /**
     * 30 minutes
     */
    THIRTY_MINUTES(1800000L);

    private final long value;

    /**
     * Default constructor for {@code SnoozeDuration}
     * @param value the {@code long} value
     */
    SnoozeDuration(long value) {
        this.value = value;
    }

    /**
     * Gets the {@code long} value
     * @return the {@code long} value
     */
    public long getValue() {
        return value;
    }

    private static LongSparseArray<SnoozeDuration> sparseArray = new LongSparseArray<>();

    static {
        for (SnoozeDuration duration : SnoozeDuration.values())
            sparseArray.append(duration.getValue(), duration);
    }

    /**
     * Gets the {@code SnoozeDuration} value from an {@code long} key
     * @param l the {@code long} key
     * @return the {@code SnoozeDuration}
     */
    public static SnoozeDuration fromLong(long l) {
        return sparseArray.get(l);
    }

}
