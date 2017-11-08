package com.ukdev.smartbuzz.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for {@link Alarm} objects
 *
 * @author Alan Camargo
 */
public class AlarmDao extends Dao {

    private static final String TABLE_NAME = "ALARMS";
    private static final String WHERE_CLAUSE = String.format("%1$s = ?", Column.ID);
    private static final String[] COLUMNS = null;
    private static final String GROUP_BY = null;
    private static final String HAVING = null;
    private static final String ACTIVE_STRING = "1";
    private static final String LIMIT = "1";

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
    public boolean delete(Alarm alarm) {
        if (!writer.isOpen())
            openDatabase();
        String[] whereArgs = new String[] {String.valueOf(alarm.getId())};
        boolean success = writer.delete(TABLE_NAME, WHERE_CLAUSE, whereArgs) > 0;
        writer.close();
        return success;
    }

    /**
     * Inserts a new instance of {@link Alarm}
     * @param alarm the alarm to insert
     * @return the ID of the newly created alarm
     */
    public long insert(Alarm alarm) {
        if (!writer.isOpen())
            openDatabase();
        final String nullColumnHack = null;
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        return writer.insert(TABLE_NAME, nullColumnHack, values);
    }

    /**
     * Gets all active instances of {@link Alarm}.
     * Active can be understood as scheduled.
     * @see Alarm#isActive()
     * @return all active alarms
     */
    public List<Alarm> getActiveAlarms() {
        if (!reader.isOpen())
            openDatabase();
        String selection = String.format("%1$s = ?", Column.ACTIVE);
        String[] selectionArgs = new String[] {ACTIVE_STRING};
        final String orderBy = null;
        Cursor cursor = reader.query(TABLE_NAME, COLUMNS, selection, selectionArgs, GROUP_BY, HAVING, orderBy);
        ArrayList<Alarm> alarms = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            alarms = queryAlarms(cursor);
        }
        cursor.close();
        reader.close();
        return alarms;
    }

    /**
     * Gets all instances of {@link Alarm}
     * @return all alarms
     */
    public List<Alarm> select() {
        if (!reader.isOpen())
            openDatabase();
        final String selection = null;
        final String[] selectionArgs = null;
        String orderBy = String.format("%1$s ASC", Column.ID);
        Cursor cursor = reader.query(TABLE_NAME, COLUMNS, selection, selectionArgs, GROUP_BY, HAVING, orderBy);
        ArrayList<Alarm> alarms = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            alarms = queryAlarms(cursor);
        }
        cursor.close();
        reader.close();
        return alarms;
    }

    /**
     * Gets an instance of {@link Alarm} by
     * its database ID
     * @param id the ID
     * @return the alarm found
     */
    public Alarm select(int id) {
        if (!reader.isOpen())
            openDatabase();
        String selection = String.format("%1$s = ?", Column.ID);
        String[] selectionArgs = new String[] {String.valueOf(id)};
        final String orderBy = null;
        Cursor cursor = reader.query(TABLE_NAME, COLUMNS, selection, selectionArgs,
                                     GROUP_BY, HAVING, orderBy, LIMIT);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name, ringtoneUri, wallpaperUri, text;
            long triggerTime, snoozeDuration;
            int volume;
            boolean active, sleepCheckerOn, vibrate;
            Integer[] repetition;

            name = cursor.getString(cursor.getColumnIndex(Column.NAME));
            text = cursor.getString(cursor.getColumnIndex(Column.TEXT));

            triggerTime = cursor.getLong(cursor.getColumnIndex(Column.TRIGGER_TIME));

            active = cursor.getInt(cursor.getColumnIndex(Column.ACTIVE)) == 1;
            sleepCheckerOn = cursor.getInt(cursor.getColumnIndex(Column.SLEEP_CHECKER_ON)) == 1;
            ringtoneUri = cursor.getString(cursor.getColumnIndex(Column.RINGTONE_URI));
            wallpaperUri = cursor.getString(cursor.getColumnIndex(Column.WALLPAPER));
            volume = cursor.getInt(cursor.getColumnIndex(Column.VOLUME));
            snoozeDuration = cursor.getLong(cursor.getColumnIndex(Column.SNOOZE_DURATION));
            vibrate = cursor.getInt(cursor.getColumnIndex(Column.VIBRATE)) == 1;

            repetition = Utils.convertStringToIntArray(context,
                                                       cursor.getString(cursor.getColumnIndex(
                                                               Column.REPETITION)));

            AlarmBuilder alarmBuilder = new AlarmBuilder().setName(name)
                                                                 .setTriggerTime(triggerTime)
                                                                 .setSnoozeDuration(snoozeDuration)
                                                                 .setRepetition(repetition)
                                                                 .setText(text)
                                                                 .setSleepCheckerOn(sleepCheckerOn)
                                                                 .setVibrate(vibrate)
                                                                 .setVolume(volume)
                                                                 .setActive(active);
            if (ringtoneUri != null) {
                Uri ringtone =  Uri.parse(ringtoneUri);
                alarmBuilder.setRingtoneUri(ringtone);
            } else
                alarmBuilder.setRingtoneUri(null);
            if (wallpaperUri != null) {
                Uri wallpaper = Uri.parse(wallpaperUri);
                alarmBuilder.setWallpaperUri(wallpaper);
            } else
                alarmBuilder.setWallpaperUri(null);
            alarmBuilder.setId(id);
            cursor.close();
            reader.close();
            return alarmBuilder.build();
        } else {
            reader.close();
            return null;
        }
    }

    /**
     * Updates an instance of {@link Alarm}
     * @param alarm the alarm to be updated
     * @return {@code true} if the operation
     *         has been successful, otherwise
     *         {@code false}
     */
    public boolean update(Alarm alarm) {
        if (!writer.isOpen())
            openDatabase();
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        String[] whereArgs = new String[] {String.valueOf(alarm.getId())};
        boolean success = writer.update(TABLE_NAME, values, WHERE_CLAUSE, whereArgs) > 0;
        writer.close();
        return success;
    }

    private void fillFields(Alarm alarm, ContentValues values) {
        values.put(Column.NAME, alarm.getName());
        values.put(Column.TRIGGER_TIME, alarm.getTriggerTime());
        values.put(Column.REPETITION, Utils.convertIntArrayToString(context, alarm.getRepetition()));
        values.put(Column.SLEEP_CHECKER_ON, alarm.isSleepCheckerOn() ? 1 : 0);
        values.put(Column.VIBRATE, alarm.vibrates() ? 1 : 0);
        if (alarm.getRingtoneUri() != null)
            values.put(Column.RINGTONE_URI, alarm.getRingtoneUri().toString());
        else
            values.put(Column.RINGTONE_URI, (String) null);
        values.put(Column.VOLUME, alarm.getVolume());
        if (alarm.getWallpaperUri() != null)
            values.put(Column.WALLPAPER, alarm.getWallpaperUri().toString());
        else
            values.put(Column.WALLPAPER, (String) null);
        values.put(Column.TEXT, alarm.getText());
        values.put(Column.SNOOZE_DURATION, alarm.getSnoozeDuration());
        values.put(Column.ACTIVE, alarm.isActive() ? 1 : 0);
    }

    private ArrayList<Alarm> queryAlarms(Cursor cursor) {
        ArrayList<Alarm> alarms = new ArrayList<>();
        do {
            String name, ringtoneUri, wallpaperUri, text;
            long triggerTime;
            int volume;
            long snoozeDuration;
            boolean active, sleepCheckerOn, vibrate;
            Integer[] repetition;

            name = cursor.getString(cursor.getColumnIndex(Column.NAME));
            text = cursor.getString(cursor.getColumnIndex(Column.TEXT));

            triggerTime = cursor.getLong(cursor.getColumnIndex(Column.TRIGGER_TIME));

            active = cursor.getInt(cursor.getColumnIndex(Column.ACTIVE)) == 1;
            sleepCheckerOn = cursor.getInt(cursor.getColumnIndex(Column.SLEEP_CHECKER_ON)) == 1;
            ringtoneUri = cursor.getString(cursor.getColumnIndex(Column.RINGTONE_URI));
            volume = cursor.getInt(cursor.getColumnIndex(Column.VOLUME));
            snoozeDuration = cursor.getLong(cursor.getColumnIndex(Column.SNOOZE_DURATION));
            vibrate = cursor.getInt(cursor.getColumnIndex(Column.VIBRATE)) == 1;
            wallpaperUri = cursor.getString(cursor.getColumnIndex(Column.WALLPAPER));

            repetition = Utils.convertStringToIntArray(context,
                                                       cursor.getString(cursor.getColumnIndex(
                                                               Column.REPETITION)));

            AlarmBuilder alarmBuilder = new AlarmBuilder().setName(name)
                                                                 .setTriggerTime(triggerTime)
                                                                 .setSnoozeDuration(snoozeDuration)
                                                                 .setRepetition(repetition)
                                                                 .setText(text)
                                                                 .setSleepCheckerOn(sleepCheckerOn)
                                                                 .setVibrate(vibrate)
                                                                 .setVolume(volume)
                                                                 .setActive(active);
            if (ringtoneUri != null) {
                Uri ringtone = Uri.parse(ringtoneUri);
                alarmBuilder.setRingtoneUri(ringtone);
            } else
                alarmBuilder.setRingtoneUri(null);
            if (wallpaperUri != null) {
                Uri wallpaper = Uri.parse(wallpaperUri);
                alarmBuilder.setWallpaperUri(wallpaper);
            } else
                alarmBuilder.setWallpaperUri(null);
            int id = cursor.getInt(cursor.getColumnIndex(Column.ID));
            alarmBuilder.setId(id);
            alarms.add(alarmBuilder.build());
        } while (cursor.moveToNext());
        return alarms;
    }

}
