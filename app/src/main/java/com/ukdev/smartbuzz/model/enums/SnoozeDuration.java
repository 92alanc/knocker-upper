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

    /**
     * Gets the {@code long} values
     * @return the values
     */
    public static long[] getValues() {
        return values;
    }

    private static LongSparseArray<SnoozeDuration> sparseArray = new LongSparseArray<>();
    private static long[] values = new long[values().length];

    static {
        SnoozeDuration[] enumValues = SnoozeDuration.values();
        for (int i = 0; i < enumValues.length; i++) {
            SnoozeDuration duration = enumValues[i];
            values[i] = enumValues[i].getValue();
            sparseArray.append(duration.getValue(), duration);
        }
    }

    /**
     * Gets the {@code SnoozeDuration} value from a {@code long} key
     * @param key the {@code long} key
     * @return the {@code SnoozeDuration}
     */
    public static SnoozeDuration valueOf(long key) {
        return sparseArray.get(key);
    }

}
