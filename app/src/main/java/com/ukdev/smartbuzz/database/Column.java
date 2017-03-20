package com.ukdev.smartbuzz.database;

enum Column {

    ID("_id"),
    TITLE("TITLE"),
    TRIGGER_TIME("TRIGGER_TIME"),
    SNOOZE_DURATION("SNOOZE_DURATION"),
    REPETITION("REPETITION"),
    RINGTONE_TITLE("RINGTONE_TITLE"),
    RINGTONE_URI("RINGTONE_URI"),
    TEXT("ALARM_TEXT"),
    SLEEP_CHECKER_ON("SLEEP_CHECKER_ON"),
    ACTIVE("ACTIVE"),
    VIBRATE("VIBRATE"),
    VOLUME("VOLUME");

    private final String value;

    Column(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
