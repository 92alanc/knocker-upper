package com.ukdev.smartbuzz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.model.*;
import com.ukdev.smartbuzz.extras.BackEndTools;

import java.util.ArrayList;

import static com.ukdev.smartbuzz.database.AlarmTable.TABLE_NAME;

/**
 * Alarm DAO
 * Manages all database operations
 * Created by Alan Camargo - April 2016
 */
public class AlarmDAO
{

    /**
     * Inserts an alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void insert(Context context, Alarm alarm)
    {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AlarmTable.COLUMN_ID, alarm.getId());
        values.put(AlarmTable.COLUMN_TITLE, alarm.getTitle());
        values.put(AlarmTable.COLUMN_TRIGGER_HOURS, alarm.getTriggerTime().getHours());
        values.put(AlarmTable.COLUMN_TRIGGER_MINUTES, alarm.getTriggerTime().getMinutes());
        values.put(AlarmTable.COLUMN_REPETITION, BackEndTools.convertIntArrayToString(context, alarm.getRepetition()));
        values.put(AlarmTable.COLUMN_TIME_ZONE_TITLE, alarm.getTimeZone().getTitle());
        int timeZoneGmtHours = alarm.getTimeZone().getOffset().getHours();
        values.put(AlarmTable.COLUMN_TIME_ZONE_OFFSET_HOURS, timeZoneGmtHours);
        int timeZoneGmtMinutes = alarm.getTimeZone().getOffset().getMinutes();
        values.put(AlarmTable.COLUMN_TIME_ZONE_OFFSET_MINUTES, timeZoneGmtMinutes);
        values.put(AlarmTable.COLUMN_IS_REMINDER, alarm.isReminder() ? 1 : 0);
        values.put(AlarmTable.COLUMN_VIBRATES, alarm.vibrates() ? 1 : 0);
        values.put(AlarmTable.COLUMN_RINGTONE_URI, alarm.getRingtone().getUri().toString());
        values.put(AlarmTable.COLUMN_RINGTONE_TITLE, alarm.getRingtone().getTitle());
        values.put(AlarmTable.COLUMN_RINGTONE_TYPE, alarm.getRingtone().getType() == RingtoneType.Native ? 0 : 1);
        values.put(AlarmTable.COLUMN_VOLUME, alarm.getVolume());
        values.put(AlarmTable.COLUMN_SNOOZE, alarm.getSnooze());
        values.put(AlarmTable.COLUMN_STATE, alarm.isOn() ? 1 : 0);
        values.put(AlarmTable.COLUMN_LOCKED, alarm.isLocked() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Deletes an alarm
     * @param context - Context
     * @param id - int
     */
    public static void delete(Context context, int id)
    {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.delete(TABLE_NAME, AlarmTable.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    /**
     * Updates an alarm
     * @param context - Context
     * @param id - int
     * @param newAlarmValues - Alarm
     */
    public static void update(Context context, int id, Alarm newAlarmValues)
    {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AlarmTable.COLUMN_ID, newAlarmValues.getId());
        values.put(AlarmTable.COLUMN_TITLE, newAlarmValues.getTitle());
        values.put(AlarmTable.COLUMN_TRIGGER_HOURS, newAlarmValues.getTriggerTime().getHours());
        values.put(AlarmTable.COLUMN_TRIGGER_MINUTES, newAlarmValues.getTriggerTime().getMinutes());
        values.put(AlarmTable.COLUMN_REPETITION,
                BackEndTools.convertIntArrayToString(context, newAlarmValues.getRepetition()));
        values.put(AlarmTable.COLUMN_TIME_ZONE_TITLE, newAlarmValues.getTimeZone().getTitle());
        int timeZoneGmtHours = newAlarmValues.getTimeZone().getOffset().getHours();
        values.put(AlarmTable.COLUMN_TIME_ZONE_OFFSET_HOURS, timeZoneGmtHours);
        int timeZoneGmtMinutes = newAlarmValues.getTimeZone().getOffset().getMinutes();
        values.put(AlarmTable.COLUMN_TIME_ZONE_OFFSET_MINUTES, timeZoneGmtMinutes);
        values.put(AlarmTable.COLUMN_IS_REMINDER, newAlarmValues.isReminder() ? 1 : 0);
        values.put(AlarmTable.COLUMN_VIBRATES, newAlarmValues.vibrates() ? 1 : 0);
        values.put(AlarmTable.COLUMN_RINGTONE_URI, newAlarmValues.getRingtone().getUri().toString());
        values.put(AlarmTable.COLUMN_RINGTONE_TITLE, newAlarmValues.getRingtone().getTitle());
        values.put(AlarmTable.COLUMN_RINGTONE_TYPE, newAlarmValues.getRingtone().getType() == RingtoneType.Native ? 0 : 1);
        values.put(AlarmTable.COLUMN_VOLUME, newAlarmValues.getVolume());
        values.put(AlarmTable.COLUMN_SNOOZE, newAlarmValues.getSnooze());
        values.put(AlarmTable.COLUMN_STATE, newAlarmValues.isOn() ? 1 : 0);
        values.put(AlarmTable.COLUMN_LOCKED, newAlarmValues.isLocked() ? 1 : 0);
        db.update(TABLE_NAME, values,
                AlarmTable.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    /**
     * Gets an alarm by its ID
     * @param context - Context
     * @param id - int
     * @return alarm
     */
    public static Alarm select(Context context, int id)
    {
        Alarm alarm = null;
        for (Alarm a : selectAll(context, AppConstants.ORDER_BY_ID))
        {
            if (a.getId() == id)
            {
                alarm = a;
                break;
            }
        }
        return alarm;
    }

    /**
     * Gets all alarms
     * @param context - Context
     * @param orderBy - String
     * @return alarms
     */
    public static Alarm[] selectAll(Context context, String orderBy)
    {
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                    null, null, null, null, null, orderBy + " ASC");
        Alarm[] alarms = new Alarm[cursor.getCount()];

        if (cursor.getCount() > 0)
        {
            int i = 0;
            while (cursor.moveToNext())
            {
                String alarmTitle, ringtoneTitle, ringtoneUri;
                int id, hours, minutes, volume, snooze;
                TimeZoneWrapper timeZone;
                boolean isOn, isReminder, vibrates, isLocked;
                int[] repetition;
                TimeWrapper triggerTime;
                RingtoneType ringtoneType;

                alarmTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_TITLE));

                String timeZoneTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_TIME_ZONE_TITLE));
                int timeZoneOffsetHours = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TIME_ZONE_OFFSET_HOURS));
                int timeZoneOffsetMinutes = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TIME_ZONE_OFFSET_MINUTES));
                TimeWrapper timeZoneOffset = new TimeWrapper(timeZoneOffsetHours, timeZoneOffsetMinutes);
                timeZone = new TimeZoneWrapper(timeZoneTitle, timeZoneOffset);

                id = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_ID));
                hours = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_HOURS));
                minutes = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_MINUTES));
                triggerTime = new TimeWrapper(hours, minutes);

                isOn = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_STATE)) == 1;
                isReminder = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_IS_REMINDER)) == 1;
                ringtoneUri = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_URI));
                ringtoneTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TITLE));
                int type = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TYPE));
                if (type == 0)
                    ringtoneType = RingtoneType.Native;
                else
                    ringtoneType = RingtoneType.Custom;
                volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
                snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
                vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

                repetition = BackEndTools.convertStringToIntArray(context,
                        cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
                RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle, ringtoneType);
                isLocked = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_LOCKED)) == 1;

                alarms[i] = new Alarm(id, alarmTitle, triggerTime, ringtone, volume, vibrates,
                        isReminder, isOn, timeZone, repetition, snooze);
                alarms[i].setLocked(isLocked);
                i++;
            }
        }
        cursor.close();
        db.close();
        return alarms;
    }

    /**
     * Gets the active alarms
     * @param context - Context
     * @return active alarms
     */
    public static ArrayList<Alarm> getActiveAlarms(Context context)
    {
        ArrayList<Alarm> activeAlarms = new ArrayList<>();
        for (Alarm alarm : selectAll(context, AppConstants.ORDER_BY_ID))
        {
            if (alarm.isOn())
                activeAlarms.add(alarm);
        }
        return activeAlarms;
    }

    /**
     * Tells if there are no duplicated alarm titles
     * @param context - Context
     * @param title - String
     * @return has duplicates
     */
    public static boolean hasDuplicates(Context context, String title)
    {
        boolean has = false;
        for (Alarm alarm : selectAll(context, AppConstants.ORDER_BY_ID))
        {
            if (title.equalsIgnoreCase(alarm.getTitle()))
            {
                has = true;
                break;
            }
        }
        return has;
    }

    /**
     * Gets the number of alarms with the title beginning with "New Alarm"
     * @param context - Context
     * @return New Alarm count
     */
    public static int getNewAlarmCount(Context context)
    {
        int count = 0;
        for (Alarm alarm : selectAll(context, AppConstants.ORDER_BY_ID))
        {
            if (alarm.getTitle().startsWith(context.getString(R.string.new_alarm)))
                count++;
        }
        return count;
    }

}
