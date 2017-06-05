package com.ukdev.smartbuzz.backend.enums;

/**
 * An intent extra
 *
 * @author Alan Camargo
 */
public enum Extra {

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
     * Default constructor for {@code Extra}
     * @param value the string value
     */
    Extra(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
