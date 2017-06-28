package com.ukdev.smartbuzz.misc;

/**
 * An intent extra
 *
 * @author Alan Camargo
 */
public enum IntentExtra {

    /**
     * The ID of an alarm
     */
    ID("ID"),

    /**
     * Whether Sleep Checker should be turned on
     */
    SLEEP_CHECKER_ON("SLEEP_CHECKER_ON");

    private final String value;

    /**
     * Default constructor for {@code IntentExtra}
     * @param value the string value
     */
    IntentExtra(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}