package com.ukdev.smartbuzz.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.ukdev.smartbuzz.backend.BackEndTools;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.RingtoneWrapper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Alarm repository class
 * Manages all database operations
 * Created by Alan Camargo - April 2016
 */
public class AlarmRepository
{

    @SuppressLint("StaticFieldLeak")
    private static AlarmRepository instance;
    private Context context;
    private SQLiteDatabase reader, writer;

    private AlarmRepository(Context context)
    {
        this.context = context;
    }

    /**
     * Gets an instance of the database
     *
     * @param context - Context
     * @return database instance
     */
    public static AlarmRepository getInstance(Context context)
    {
        if (instance == null)
            instance = new AlarmRepository(context);
        return instance;
    }

    /**
     * Inserts an alarm
     *
     * @param alarm - Alarm
     */
    public void insert(Alarm alarm)
    {
        if (writer == null)
            writer = new DatabaseHelper(context).getWritableDatabase();
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
        values.put(AlarmTable.COLUMN_VOLUME, alarm.getVolume());
        values.put(AlarmTable.COLUMN_SNOOZE, alarm.getSnooze());
        values.put(AlarmTable.COLUMN_STATE, alarm.isActive() ? 1 : 0);
        writer.insert(AlarmTable.TABLE_NAME, null, values);
    }

    /**
     * Deletes an alarm
     *
     * @param id - int
     */
    public void delete(int id)
    {
        if (writer == null)
            writer = new DatabaseHelper(context).getWritableDatabase();
        writer.delete(AlarmTable.TABLE_NAME, AlarmTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Updates an alarm
     *
     * @param id             - int
     * @param newAlarmValues - Alarm
     */
    public void update(int id, Alarm newAlarmValues)
    {
        if (writer == null)
            writer = new DatabaseHelper(context).getWritableDatabase();
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
        values.put(AlarmTable.COLUMN_VOLUME, newAlarmValues.getVolume());
        values.put(AlarmTable.COLUMN_SNOOZE, newAlarmValues.getSnooze());
        values.put(AlarmTable.COLUMN_STATE, newAlarmValues.isActive() ? 1 : 0);
        writer.update(AlarmTable.TABLE_NAME, values,
                AlarmTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Gets an alarm by its ID
     *
     * @param id - int
     * @return alarm
     */
    public Alarm select(int id)
    {
        if (reader == null)
            reader = new DatabaseHelper(context).getReadableDatabase();
        Alarm alarm = null;
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME, null, AlarmTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1");
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
        {
            String alarmTitle, ringtoneTitle, ringtoneUri;
            int day, hours, minutes, volume, snooze;
            boolean isOn, isReminder, vibrates;
            int[] repetition;
            Calendar triggerTime = Calendar.getInstance();

            alarmTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_TITLE));

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
            volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
            snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
            vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

            repetition = BackEndTools.convertStringToIntArray(context,
                    cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
            RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle);

            alarm = new Alarm(id, alarmTitle, triggerTime, ringtone, volume, vibrates,
                    isReminder, isOn, repetition, snooze);
        }
        cursor.close();
        return alarm;
    }

    /**
     * Gets all alarms
     *
     * @param orderBy - String
     * @return alarms
     */
    public Alarm[] selectAll(String orderBy)
    {
        if (reader == null)
            reader = new DatabaseHelper(context).getReadableDatabase();
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
                volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
                snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
                vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

                repetition = BackEndTools.convertStringToIntArray(context,
                        cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
                RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle);

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
     *
     * @return active alarms
     */
    public ArrayList<Alarm> getActiveAlarms()
    {
        if (reader == null)
            reader = new DatabaseHelper(context).getReadableDatabase();
        ArrayList<Alarm> activeAlarms = new ArrayList<>();
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME, null, AlarmTable.COLUMN_STATE + " = ?",
                new String[]{"1"}, null, null, null);
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
                volume = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VOLUME));
                snooze = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_SNOOZE));
                vibrates = cursor.getInt(cursor.getColumnIndex(AlarmTable.COLUMN_VIBRATES)) == 1;

                repetition = BackEndTools.convertStringToIntArray(context,
                        cursor.getString(cursor.getColumnIndex(AlarmTable.COLUMN_REPETITION)));
                RingtoneWrapper ringtone = new RingtoneWrapper(Uri.parse(ringtoneUri), ringtoneTitle);

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
     *
     * @return last ID
     */
    public int getLastId()
    {
        if (reader == null)
            reader = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor = reader.query(AlarmTable.TABLE_NAME, new String[]{AlarmTable.COLUMN_ID}, null, null,
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
     *
     * @return row count
     */
    public int getRowCount()
    {
        if (reader == null)
            reader = new DatabaseHelper(context).getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(reader, AlarmTable.TABLE_NAME);
    }

}
