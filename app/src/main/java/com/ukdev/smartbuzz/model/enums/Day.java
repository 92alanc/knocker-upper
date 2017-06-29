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
    SUNDAY(0),

    /**
     * Monday
     */
    MONDAY(1),

    /**
     * Tuesday
     */
    TUESDAY(2),

    /**
     * Wednesday
     */
    WEDNESDAY(3),

    /**
     * Thursday
     */
    THURSDAY(4),

    /**
     * Friday
     */
    FRIDAY(5),

    /**
     * Saturday
     */
    SATURDAY(6);

    private final int value;

    /**
     * Default constructor for {@code Day}
     * @param value the index of the {@code daysOfTheWeek}
     *              string array located at res/array
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
     * @param key the {@code int} key
     * @return the {@code Day}
     */
    public static Day valueOf(int key) {
        return sparseArray.get(key);
    }

}
