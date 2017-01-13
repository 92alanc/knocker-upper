package com.ukdev.smartbuzz.database;

/**
 * Alarms table
 * Stores all alarms
 * Created by Alan Camargo - April 2016
 */
public class AlarmTable
{

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_TRIGGER_HOURS = "TRIGGER_HOURS";
    static final String TABLE_NAME = "ALARMS";
    static final String COLUMN_TRIGGER_DAY = "TRIGGER_DAY";
    static final String COLUMN_TRIGGER_MINUTES = "TRIGGER_MINUTES";
    static final String COLUMN_REPETITION = "REPETITION";
    static final String COLUMN_IS_REMINDER = "IS_REMINDER";
    static final String COLUMN_VIBRATES = "VIBRATES";
    static final String COLUMN_RINGTONE_URI = "RINGTONE_URI";
    static final String COLUMN_RINGTONE_TITLE = "RINGTONE_TITLE";
    static final String COLUMN_VOLUME = "VOLUME";
    static final String COLUMN_SNOOZE = "SNOOZE";
    static final String COLUMN_STATE = "STATE";

    /**
     * Creates the table
     *
     * @return query to create the table
     */
    static String createTable()
    {
        return String.format(prepareQuery(),
                TABLE_NAME,
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_TRIGGER_DAY,
                COLUMN_TRIGGER_HOURS,
                COLUMN_TRIGGER_MINUTES,
                COLUMN_REPETITION,
                COLUMN_IS_REMINDER,
                COLUMN_VIBRATES,
                COLUMN_RINGTONE_URI,
                COLUMN_RINGTONE_TITLE,
                COLUMN_VOLUME,
                COLUMN_SNOOZE,
                COLUMN_STATE);
    }

    /**
     * Prepares the structure of the query to create the table
     *
     * @return query structure
     */
    private static String prepareQuery()
    {
        return "CREATE TABLE %1$s" + "(%2$s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%3$s NVARCHAR(20), " +
                "%4$s INTEGER, " +
                "%5$s INTEGER, " +
                "%6$s INTEGER, " +
                "%7$s VARCHAR(20), " +
                "%8$S INTEGER, " +
                "%9$s INTEGER, " +
                "%10$s VARCHAR(30), " +
                "%11$s NVARCHAR(50), " +
                "%12$s INTEGER, " +
                "%13$s INTEGER, " +
                "%14$s INTEGER)";
    }

}
