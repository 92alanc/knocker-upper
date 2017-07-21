package com.ukdev.smartbuzz.misc;

/**
 * An intent action
 *
 * @author Alan Camargo
 */
public enum IntentAction {

    /**
     * Cancels an alarm
     */
    CANCEL_ALARM("com.ukdev.smartbuzz.ACTION_CANCEL_ALARM"),

    /**
     * Delays an alarm (snooze operation)
     */
    DELAY_ALARM("com.ukdev.smartbuzz.ACTION_DELAY_ALARM"),

    /**
     * Triggers an alarm
     */
    TRIGGER_ALARM("com.ukdev.smartbuzz.ACTION_TRIGGER_ALARM"),

    /**
     * Triggers Sleep Checker
     */
    TRIGGER_SLEEP_CHECKER("com.ukdev.smartbuzz.ACTION_TRIGGER_SLEEP_CHECKER"),

    /**
     * Wakes up the device
     */
    WAKE_UP("com.ukdev.smartbuzz.ACTION_WAKE_UP");

    private final String value;

    /**
     * Default constructor for {@code IntentAction}
     * @param value the string value
     */
    IntentAction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
