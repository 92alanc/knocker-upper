package com.ukdev.smartbuzz.database;

/**
 * A database column
 *
 * @author Alan Camargo
 */
enum Column {

    /**
     * The {@code _id} column
     */
    ID("_id"),

    /**
     * The {@code TITLE} column
     */
    TITLE("TITLE"),

    /**
     * The {@code TRIGGER_TIME} column
     */
    TRIGGER_TIME("TRIGGER_TIME"),

    /**
     * The {@code SNOOZE_DURATION} column
     */
    SNOOZE_DURATION("SNOOZE_DURATION"),

    /**
     * The {@code REPETITION} column
     */
    REPETITION("REPETITION"),

    /**
     * The {@code RINGTONE_TITLE} column
     */
    RINGTONE_TITLE("RINGTONE_TITLE"),

    /**
     * The {@code RINGTONE_URI} column
     */
    RINGTONE_URI("RINGTONE_URI"),

    /**
     * The {@code ALARM_TEXT} column
     */
    TEXT("ALARM_TEXT"),

    /**
     * The {@code SLEEP_CHECKER_ON} column
     */
    SLEEP_CHECKER_ON("SLEEP_CHECKER_ON"),

    /**
     * The {@code ACTIVE} column
     */
    ACTIVE("ACTIVE"),

    /**
     * The {@code VIBRATE}
     */
    VIBRATE("VIBRATE"),

    /**
     * The {@code VOLUME}
     */
    VOLUME("VOLUME"),

    /**
     * The {@code WALLPAPER}
     */
    WALLPAPER("WALLPAPER");

    private final String value;

    /**
     * Default constructor for {@code Column}
     * @param value the string value
     */
    Column(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
