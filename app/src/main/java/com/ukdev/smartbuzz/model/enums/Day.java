package com.ukdev.smartbuzz.model.enums;

import android.util.SparseArray;

public enum Day {

    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    private final int value;

    Day(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static SparseArray<Day> sparseArray = new SparseArray<>();

    static {
        for (Day day : Day.values())
            sparseArray.append(day.getValue(), day);
    }

    public static Day fromInt(int i) {
        return sparseArray.get(i);
    }

}
