package com.ukdev.smartbuzz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.ukdev.smartbuzz.model.*;
import com.ukdev.smartbuzz.backend.BackEndTools;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Alarm DAO
 * Manages all database operations
 * Created by Alan Camargo - April 2016
 */
public class AlarmRepository
{

    private static AlarmRepository instance;
    private static SQLiteDatabase reader, writer;

    /**
     * Gets an instance of the database
     * @param context - Context
     * @return database instance
     */
    public static AlarmRepository getInstance(Context context)
    {
        if (instance == null)
            instance = new AlarmRepository();
        if (reader == null || !reader.isOpen())
            reader = new DatabaseHelper(context).getReadableDatabase();
        if (writer == null || !writer.isOpen())
            writer = new DatabaseHelper(context).getWritableDatabase();
        return instance;
    }

    /**
     * Inserts an alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public void insert(Context context, Alarm alarm)
    {
        ContentValues values = new ContentValues();
        values.put(AlarmTable.COLUMN_TITLE, alarm.getTitle());
        values.put(AlarmTable.COLUMN_TRIGGER_DAY, alarm.getTriggerTime().get(Calendar.DAY_OF_MONTH));
        values.put(AlarmTable.COLUMN_TRIGGER_HOURS, alarm.getTriggerTime().get(Calendar.HOUR_OF_DAY));
        values.put(AlarmTable.COLUMN_TRIGGER_MINUTES, alarm.getTriggerTime().get(Calendar.MINUTE));
        values.put(AlarmTable.COLUMN_REPETITION, BackEndTools.convertIntArrayToString(context, alarm.getRepetition()));
        values.put(AlarmTable.COLUMN_IS_REMINDER, alarm.isReminder() ? 1 : 0);
        values.put(AlarmTable.COLUMN_VIBRATES, alarm.vibrates() ? 1 : 0);
        values.put(AlarmTable.COLUMN_RINGTONE_URI, alarm.getRingtone().getUri().toString());
        values.put(AlarmTable.COLUMN_RINGTONE_TITLE, alarm.getRingtone().getTitle());
        values.put(AlarmTable.COLUMN_RINGTONE_TYPE, alarm.getRingtone().getType() == RingtoneType.NATIVE ? 0 : 1);
        values.put(AlarmTable.COLUMN_VOLUME, alarm.getVolume());
        values.put(AlarmTable.COLUMN_SNOOZE, alarm.getSnooze());
        values.put(AlarmTable.COLUMN_STATE, alarm.isOn() ? 1 : 0);
        writer.insert(AlarmTable.TABLE_NAME, null, values);
    }

    /**
     * Deletes an alarm
     * @param id - int
     */
    public void delete(int id)
    {
        writer.delete(AlarmTable.TABLE_NAME, AlarmTable.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Updates an alarm
     * @param context - Context
     * @param id - int
     * @param newAlarmValues - Alarm
     */
    public void update(Context context, int id, Alarm newAlarmValues)
    {
        ContentValues values = new ContentValues();
        values.put(AlarmTable.COLUMN_TITLE, newAlarmValues.getTitle());
        values.put(AlarmTable.COLUMN_TRIGGER_DAY, newAlarmValues.getTriggerTime().get(Calendar.DAY_OF_MONTH));
        values.put(AlarmTable.COLUMN_TRIGGER_HOURS, newAlarmValues.getTriggerTime().get(Calendar.HOUR_OF_DAY));
        values.put(AlarmTable.COLUMN_TRIGGER_MINUTES, newAlarmValues.getTriggerTime().get(Calendar.MINUTE));
        values.put(AlarmTable.COLUMN_REPETITION,
                BackEndTools.convertIntArrayToString(context, newAlarmValues.getRepetition()));
        values.put(AlarmTable.COLUMN_IS_REMINDER, newAlarmValues.isReminder() ? 1 : 0);
        values.put(AlarmTable.COLUMN_VIBRATES, newAlarmValues.vibrates() ? 1 : 0);
        values.put(AlarmTable.COLUMN_RINGTONE_URI, newAlarmValues.getRingtone().getUri().toString());
        values.put(AlarmTable.COLUMN_RINGTONE_TITLE, newAlarmValues.getRingtone().getTitle());
        values.put(AlarmTable.COLUMN_RINGTONE_TYPE, newAlarmValues.getRingtone().getType() == RingtoneType.NATIVE ? 0 : 1);
        values.put(AlarmTable.COLUMN_VOLUME, newAlarmValues.getVolume());
        values.put(AlarmTable.COLUMN_SNOOZE, newAlarmValues.getSnooze());
        values.put(AlarmTable.COLUMN_STATE, newAlarmValues.isOn() ? 1 : 0);
        writer.update(AlarmTable.TABLE_NAME, values,
                AlarmTable.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Gets an alarm by its ID
     * @param context - Context
     * @param id - int
     * @return alarm
     */
    public Alarm select(Context context, int id)
    {
        Alarm alarm = null;
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME, null, AlarmTable.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null, "1");
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String alarmTitle, ringtoneTitle, ringtoneUri;
            int day, hours, minutes, volume, snooze;
            boolean isOn, isReminder, vibrates;
            int[] repetition;
            Calendar triggerTime = Calendar.getInstance();
            RingtoneType ringtoneType;

            alarmTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_TITLE));

            id = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_ID));
            day = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_DAY));
            hours = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_HOURS));
            minutes = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_MINUTES));
            triggerTime.set(Calendar.DAY_OF_MONTH, day);
            triggerTime.set(Calendar.HOUR_OF_DAY, hours);
            triggerTime.set(Calendar.MINUTE, minutes);
            triggerTime.set(Calendar.SECOND, 0);

            isOn = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_STATE)) == 1;
            isReminder = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_IS_REMINDER)) == 1;
            ringtoneUri = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_URI));
            ringtoneTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TITLE));
            int type = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TYPE));
            if (type == 0)
                ringtoneType = RingtoneType.NATIVE;
            else
                ringtoneType = RingtoneType.CUSTOM;
            volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
            snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
            vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

            repetition = BackEndTools.convertStringToIntArray(context,
                    cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
            RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle, ringtoneType);

            alarm = new Alarm(id, alarmTitle, triggerTime, ringtone, volume, vibrates,
                    isReminder, isOn, repetition, snooze);
        }
        cursor.close();
        return alarm;
    }

    /**
     * Gets all alarms
     * @param context - Context
     * @param orderBy - String
     * @return alarms
     */
    public Alarm[] selectAll(Context context, String orderBy)
    {
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME,
                null, null, null, null, null, orderBy + " ASC");
        Alarm[] alarms = new Alarm[cursor.getCount()];

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int i = 0;
            do
            {
                String alarmTitle, ringtoneTitle, ringtoneUri;
                int id, day, hours, minutes, volume, snooze;
                boolean isOn, isReminder, vibrates;
                int[] repetition;
                Calendar triggerTime = Calendar.getInstance();
                RingtoneType ringtoneType;

                alarmTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_TITLE));

                id = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_ID));
                day = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_DAY));
                hours = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_HOURS));
                minutes = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_MINUTES));
                triggerTime.set(Calendar.DAY_OF_MONTH, day);
                triggerTime.set(Calendar.HOUR_OF_DAY, hours);
                triggerTime.set(Calendar.MINUTE, minutes);
                triggerTime.set(Calendar.SECOND, 0);

                isOn = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_STATE)) == 1;
                isReminder = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_IS_REMINDER)) == 1;
                ringtoneUri = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_URI));
                ringtoneTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TITLE));
                int type = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TYPE));
                if (type == 0)
                    ringtoneType = RingtoneType.NATIVE;
                else
                    ringtoneType = RingtoneType.CUSTOM;
                volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
                snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
                vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

                repetition = BackEndTools.convertStringToIntArray(context,
                        cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
                RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle, ringtoneType);

                alarms[i] = new Alarm(id, alarmTitle, triggerTime, ringtone, volume, vibrates,
                        isReminder, isOn, repetition, snooze);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }

    /**
     * Gets the active alarms
     * @param context - Context
     * @return active alarms
     */
    public ArrayList<Alarm> getActiveAlarms(Context context)
    {
        ArrayList<Alarm> activeAlarms = new ArrayList<>();
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME, null, AlarmTable.COLUMN_STATE + " = ?",
                new String[] { "1" }, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                String alarmTitle, ringtoneTitle, ringtoneUri;
                int id, day, hours, minutes, volume, snooze;
                boolean isOn, isReminder, vibrates;
                int[] repetition;
                Calendar triggerTime = Calendar.getInstance();
                RingtoneType ringtoneType;

                alarmTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_TITLE));

                id = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_ID));
                day = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_DAY));
                hours = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_HOURS));
                minutes = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_TRIGGER_MINUTES));
                triggerTime.set(Calendar.DAY_OF_MONTH, day);
                triggerTime.set(Calendar.HOUR_OF_DAY, hours);
                triggerTime.set(Calendar.MINUTE, minutes);
                triggerTime.set(Calendar.SECOND, 0);

                isOn = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_STATE)) == 1;
                isReminder = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_IS_REMINDER)) == 1;
                ringtoneUri = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_URI));
                ringtoneTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TITLE));
                int type = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_RINGTONE_TYPE));
                if (type == 0)
                    ringtoneType = RingtoneType.NATIVE;
                else
                    ringtoneType = RingtoneType.CUSTOM;
                volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
                snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
                vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

                repetition = BackEndTools.convertStringToIntArray(context,
                        cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
                RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle, ringtoneType);

                Alarm alarm = new Alarm(id, alarmTitle, triggerTime, ringtone, volume, vibrates,
                        isReminder, isOn, repetition, snooze);
                activeAlarms.add(alarm);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return activeAlarms;
    }

    /**
     * Gets the last ID registered in the database
     * @return last ID
     */
    public int getLastId()
    {
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME, new String[] { AlarmTable.COLUMN_ID }, null, null,
                null, null, AlarmTable.COLUMN_ID + " DESC", "1");
        int lastId = 0;
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            lastId = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_ID));
        cursor.close();
        return lastId;
    }

    /**
     * Gets the number of rows
     * @return row count
     */
    public int getRowCount()
    {
        return (int) DatabaseUtils.queryNumEntries(reader, AlarmTable.TABLE_NAME);
    }

}
