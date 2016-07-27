package com.ukdev.smartbuzz.database;

/**
 * Alarms table
 * Stores all alarms
 * Created by Alan Camargo - April 2016
 */
public class AlarmTable
{

    static final String TABLE_NAME = "ALARMS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_TRIGGER_HOURS = "TRIGGER_HOURS";
    static final String COLUMN_TRIGGER_MINUTES = "TRIGGER_MINUTES";
    static final String COLUMN_REPETITION = "REPETITION";
    static final String COLUMN_TIME_ZONE_TITLE = "TIME_ZONE_TITLE";
    static final String COLUMN_TIME_ZONE_OFFSET_HOURS = "TIME_ZONE_OFFSET_HOURS";
    static final String COLUMN_TIME_ZONE_OFFSET_MINUTES = "TIME_ZONE_OFFSET_MINUTES";
    static final String COLUMN_IS_REMINDER = "IS_REMINDER";
    static final String COLUMN_VIBRATES = "VIBRATES";
    static final String COLUMN_RINGTONE_URI = "RINGTONE_URI";
    static final String COLUMN_RINGTONE_TITLE = "RINGTONE_TITLE";
    static final String COLUMN_VOLUME = "VOLUME";
    static final String COLUMN_SNOOZE = "SNOOZE";
    static final String COLUMN_STATE = "STATE";
    static final String COLUMN_LOCKED = "LOCKED";

    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMN_ID + " INTEGER , "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_TRIGGER_HOURS + " INTEGER, "
            + COLUMN_TRIGGER_MINUTES + " INTEGER, "
            + COLUMN_REPETITION + " TEXT, "
            + COLUMN_TIME_ZONE_TITLE + " TEXT, "
            + COLUMN_TIME_ZONE_OFFSET_HOURS + " INTEGER, "
            + COLUMN_TIME_ZONE_OFFSET_MINUTES + " INTEGER, "
            + COLUMN_IS_REMINDER + " INTEGER, "
            + COLUMN_VIBRATES + " INTEGER, "
            + COLUMN_RINGTONE_URI + " TEXT, "
            + COLUMN_RINGTONE_TITLE + " TEXT, "
            + COLUMN_VOLUME + " INTEGER, "
            + COLUMN_SNOOZE + " INTEGER, "
            + COLUMN_STATE + " INTEGER, "
            + COLUMN_LOCKED + " INTEGER)";

}
