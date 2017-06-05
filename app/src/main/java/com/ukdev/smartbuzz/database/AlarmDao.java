package com.ukdev.smartbuzz.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.ukdev.smartbuzz.backend.Utils;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Ringtone;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Data access object for {@link Alarm} objects
 *
 * @author Alan Camargo
 */
public class AlarmDao extends BaseDao {

    private static final String TABLE_NAME = "ALARMS";

    @SuppressLint("StaticFieldLeak")
    private static AlarmDao instance;

    /**
     * Default constructor for {@code AlarmDao}
     * @param context the Android context
     * @return the singleton instance of {@code AlarmDao}
     */
    public static AlarmDao getInstance(Context context) {
        if (instance == null)
            instance = new AlarmDao(context);
        return instance;
    }

    private AlarmDao(Context context) {
        super(context);
    }

    /**
     * Deletes an instance of {@link Alarm}
     * @param alarm the alarm to delete
     * @return {@code true} if the operation
     *         has been successful, otherwise
     *         {@code false}
     */
    @Override
    public boolean delete(Alarm alarm) {
        return writer.delete(TABLE_NAME,
                Column.ID.toString() + " = ?", new String[]{String.valueOf(alarm.getId())}) > 0;
    }

    /**
     * Inserts a new instance of {@link Alarm}
     * @param alarm the alarm to insert
     * @return {@code true} if the operation
     *         has been successful, otherwise
     *         {@code false}
     */
    @Override
    public boolean insert(Alarm alarm) {
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        return writer.insert(TABLE_NAME, null, values) > 0;
    }

    /**
     * Gets all active instances of {@link Alarm}.
     * Active can be understood as scheduled.
     * @see Alarm#isActive()
     * @return all active alarms
     */
    @Override
    public List<Alarm> getActiveAlarms() {
        Cursor cursor = reader.query(TABLE_NAME, null, Column.ACTIVE.toString() + " = ?",
                                     new String[]{"1"}, null, null, null);
        ArrayList<Alarm> alarms = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0)
            alarms = queryAlarms(cursor);
        cursor.close();
        return alarms;
    }

    /**
     * Gets the last {@link Alarm} database ID
     * @return the last ID
     */
    @Override
    public int getLastId() {
        return 0;
    }

    /**
     * Gets all instances of {@link Alarm}
     * @return all alarms
     */
    @Override
    public List<Alarm> select() {
        Cursor cursor = reader.query(TABLE_NAME,
                null, null, null, null, null,
                Column.ID.toString() + " ASC");
        ArrayList<Alarm> alarms = new ArrayList<>();
        if (cursor.getCount() > 0)
            alarms = queryAlarms(cursor);
        cursor.close();
        return alarms;
    }

    /**
     * Gets an instance of {@link Alarm} by
     * its database ID
     * @param id the ID
     * @return the alarm found
     */
    @Override
    public Alarm select(int id) {
        Cursor cursor = reader.query(TABLE_NAME, null, Column.ID.toString() + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1");
        if (cursor.getCount() > 0) {
            Alarm alarm = assembleAlarm(cursor);
            alarm.setId(id);
            cursor.close();
            return alarm;
        } else
            return null;
    }

    /**
     * Updates an instance of {@link Alarm}
     * @param alarm the alarm to be updated
     * @return {@code true} if the operation
     *         has been successful, otherwise
     *         {@code false}
     */
    @Override
    public boolean update(Alarm alarm) {
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        return writer.update(TABLE_NAME, values,
                Column.ID.toString() + " = ?", new String[]{String.valueOf(alarm.getId())}) > 0;
    }

    private Alarm assembleAlarm(Cursor cursor) {
        cursor.moveToFirst();
        String title, ringtoneTitle, ringtoneUri, text;
        long trigger, snooze;
        int volume;
        SnoozeDuration snoozeDuration;
        boolean active, sleepCheckerOn, vibrate;
        Day[] repetition;
        Calendar triggerTime = Calendar.getInstance();

        title = cursor.getString(cursor.getColumnIndex(Column.TITLE.toString()));
        text = cursor.getString(cursor.getColumnIndex(Column.TEXT.toString()));

        trigger = cursor.getLong(cursor.getColumnIndex(Column.TRIGGER_TIME.toString()));
        triggerTime.setTimeInMillis(trigger);

        active = cursor.getInt(cursor.getColumnIndex(Column.ACTIVE.toString())) == 1;
        sleepCheckerOn = cursor.getInt(cursor.getColumnIndex(Column.SLEEP_CHECKER_ON.toString())) == 1;
        ringtoneUri = cursor.getString(cursor.getColumnIndex(Column.RINGTONE_URI.toString()));
        ringtoneTitle = cursor.getString(cursor.getColumnIndex(Column.RINGTONE_TITLE.toString()));
        volume = cursor.getInt(cursor.getColumnIndex(Column.VOLUME.toString()));
        snooze = cursor.getLong(cursor.getColumnIndex(Column.SNOOZE_DURATION.toString()));
        snoozeDuration = SnoozeDuration.fromLong(snooze);
        vibrate = cursor.getInt(cursor.getColumnIndex(Column.VIBRATE.toString())) == 1;

        repetition = Utils.convertStringToDayArray(context,
                cursor.getString(cursor.getColumnIndex(Column.REPETITION.toString())));
        Ringtone ringtone = new Ringtone(ringtoneTitle, Uri.parse(ringtoneUri));

        return new Alarm(context, 0, title, triggerTime, snoozeDuration, repetition, ringtone,
                text, sleepCheckerOn, vibrate, volume, active);
    }

    private void fillFields(Alarm alarm, ContentValues values) {
        values.put(Column.TITLE.toString(), alarm.getTitle());
        values.put(Column.TRIGGER_TIME.toString(), alarm.getTriggerTime().getTimeInMillis());
        values.put(Column.REPETITION.toString(), Utils.convertDayArrayToString(context, alarm.getRepetition()));
        values.put(Column.SLEEP_CHECKER_ON.toString(), alarm.isSleepCheckerOn() ? 1 : 0);
        values.put(Column.VIBRATE.toString(), alarm.vibrates() ? 1 : 0);
        values.put(Column.RINGTONE_URI.toString(), alarm.getRingtone().getUri().toString());
        values.put(Column.RINGTONE_TITLE.toString(), alarm.getRingtone().getTitle());
        values.put(Column.VOLUME.toString(), alarm.getVolume());
        values.put(Column.TEXT.toString(), alarm.getText());
        values.put(Column.SNOOZE_DURATION.toString(), alarm.getSnoozeDuration().getValue());
        values.put(Column.ACTIVE.toString(), alarm.isActive() ? 1 : 0);
    }

    private ArrayList<Alarm> queryAlarms(Cursor cursor) {
        ArrayList<Alarm> alarms = new ArrayList<>();
        do {
            Alarm alarm = assembleAlarm(cursor);
            int id = cursor.getInt(cursor.getColumnIndex(Column.ID.toString()));
            alarm.setId(id);
            alarms.add(alarm);
        } while (cursor.moveToNext());
        return alarms;
    }

}
