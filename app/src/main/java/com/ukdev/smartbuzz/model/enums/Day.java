package com.ukdev.smartbuzz.model.enums;

import android.util.SparseArray;

/**
 * A day of the week
 *
 * @author Alan Camargo
 */
public enum Day {

    /**
     * Sunday
     */
    SUNDAY(1),

    /**
     * Monday
     */
    MONDAY(2),

    /**
     * Tuesday
     */
    TUESDAY(3),

    /**
     * Wednesday
     */
    WEDNESDAY(4),

    /**
     * Thursday
     */
    THURSDAY(5),

    /**
     * Friday
     */
    FRIDAY(6),

    /**
     * Saturday
     */
    SATURDAY(7);

    private final int value;

    /**
     * Default constructor for {@code Day}
     * @param value the {@code int} value
     */
    Day(int value) {
        this.value = value;
    }

    /**
     * Gets the {@code int} value
     * @return the {@code int} value
     */
    public int getValue() {
        return value;
    }

    private static SparseArray<Day> sparseArray = new SparseArray<>();

    static {
        for (Day day : Day.values())
            sparseArray.append(day.getValue(), day);
    }

    /**
     * Gets the {@code Day} value from an {@code int} key
     * @param i the {@code int} key
     * @return the {@code Day}
     */
    public static Day fromInt(int i) {
        return sparseArray.get(i);
    }

}
