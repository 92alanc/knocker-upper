package com.ukdev.smartbuzz.backend.enums;

public enum Action {

    CALL_SLEEP_CHECKER("com.ukdev.smartbuzz.ACTION_CALL_SLEEP_CHECKER"),
    CANCEL_ALARM("com.ukdev.smartbuzz.ACTION_CANCEL_ALARM"),
    DELAY_ALARM("com.ukdev.smartbuzz.ACTION_DELAY_ALARM"),
    SCHEDULE_ALARM("com.ukdev.smartbuzz.ACTION_SCHEDULE_ALARM"),
    TRIGGER_ALARM("com.ukdev.smartbuzz.ACTION_TRIGGER_ALARM");

    private final String value;

    Action(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
