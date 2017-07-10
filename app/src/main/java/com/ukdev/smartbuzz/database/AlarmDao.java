package com.ukdev.smartbuzz.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;
import com.ukdev.smartbuzz.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for {@link Alarm} objects
 *
 * @author Alan Camargo
 */
public class AlarmDao extends BaseDao {

    private static final String TABLE_NAME = "ALARMS";
    private static final String WHERE_CLAUSE = String.format("%1$s = ?", Column.ID.toString());
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
    @Override
    public boolean delete(Alarm alarm) {
        String[] whereArgs = new String[] {String.valueOf(alarm.getId())};
        return writer.delete(TABLE_NAME, WHERE_CLAUSE, whereArgs) > 0;
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
        final String nullColumnHack = null;
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        return writer.insert(TABLE_NAME, nullColumnHack, values) > 0;
    }

    /**
     * Gets all active instances of {@link Alarm}.
     * Active can be understood as scheduled.
     * @see Alarm#isActive()
     * @return all active alarms
     */
    @Override
    public List<Alarm> getActiveAlarms() {
        String selection = String.format("%1$s = ?", Column.ACTIVE.toString());
        String[] selectionArgs = new String[] {ACTIVE_STRING};
        final String orderBy = null;
        Cursor cursor = reader.query(TABLE_NAME, COLUMNS, selection, selectionArgs, GROUP_BY, HAVING, orderBy);
        ArrayList<Alarm> alarms = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            alarms = queryAlarms(cursor);
        }
        cursor.close();
        return alarms;
    }

    /**
     * Gets the last {@link Alarm} database ID
     * @return the last ID
     */
    @Override
    public int getLastId() {
        final String selection = null;
        final String[] selectionArgs = null;
        final String groupBy = null;
        final String having = null;
        String orderBy = String.format("%s DESC", Column.ID.toString());
        String limit = String.valueOf(1);
        Cursor cursor = reader.query(TABLE_NAME,
                                     new String[] { Column.ID.toString() },
                                     selection, selectionArgs, groupBy,
                                     having, orderBy, limit);
        cursor.moveToFirst();
        int lastId = 0;
        if (cursor.getCount() > 0)
            lastId = cursor.getInt(cursor.getColumnIndex(Column.ID.toString()));
        cursor.close();
        return lastId;
    }

    /**
     * Gets all instances of {@link Alarm}
     * @return all alarms
     */
    @Override
    public List<Alarm> select() {
        final String selection = null;
        final String[] selectionArgs = null;
        String orderBy = String.format("%1$s ASC", Column.ID.toString());
        Cursor cursor = reader.query(TABLE_NAME, COLUMNS, selection, selectionArgs, GROUP_BY, HAVING, orderBy);
        ArrayList<Alarm> alarms = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            alarms = queryAlarms(cursor);
        }
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
        String selection = String.format("%1$s = ?", Column.ID.toString());
        String[] selectionArgs = new String[] {String.valueOf(id)};
        final String orderBy = null;
        Cursor cursor = reader.query(TABLE_NAME, COLUMNS, selection, selectionArgs,
                                     GROUP_BY, HAVING, orderBy, LIMIT);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title, ringtoneUri, text;
            long trigger, snooze;
            int volume;
            SnoozeDuration snoozeDuration;
            boolean active, sleepCheckerOn, vibrate;
            int[] repetition;

            title = cursor.getString(cursor.getColumnIndex(Column.TITLE.toString()));
            text = cursor.getString(cursor.getColumnIndex(Column.TEXT.toString()));

            trigger = cursor.getLong(cursor.getColumnIndex(Column.TRIGGER_TIME.toString()));
            Time triggerTime = Time.valueOf(trigger);

            active = cursor.getInt(cursor.getColumnIndex(Column.ACTIVE.toString())) == 1;
            sleepCheckerOn = cursor.getInt(cursor.getColumnIndex(Column.SLEEP_CHECKER_ON.toString())) == 1;
            ringtoneUri = cursor.getString(cursor.getColumnIndex(Column.RINGTONE_URI.toString()));
            volume = cursor.getInt(cursor.getColumnIndex(Column.VOLUME.toString()));
            snooze = cursor.getLong(cursor.getColumnIndex(Column.SNOOZE_DURATION.toString()));
            snoozeDuration = SnoozeDuration.valueOf(snooze);
            vibrate = cursor.getInt(cursor.getColumnIndex(Column.VIBRATE.toString())) == 1;

            repetition = Utils.convertStringToIntArray(context,
                                                       cursor.getString(cursor.getColumnIndex(Column.REPETITION.toString())));
            Uri ringtone =  Uri.parse(ringtoneUri);

            AlarmBuilder alarmBuilder = new AlarmBuilder().setTitle(title)
                                                                 .setTriggerTime(triggerTime)
                                                                 .setSnoozeDuration(snoozeDuration)
                                                                 .setRepetition(repetition)
                                                                 .setRingtoneUri(ringtone)
                                                                 .setText(text)
                                                                 .setSleepCheckerOn(sleepCheckerOn)
                                                                 .setVibrate(vibrate)
                                                                 .setVolume(volume)
                                                                 .setActive(active);
            alarmBuilder.setId(id);
            cursor.close();
            return alarmBuilder.build();
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
        String[] whereArgs = new String[] {String.valueOf(alarm.getId())};
        return writer.update(TABLE_NAME, values, WHERE_CLAUSE, whereArgs) > 0;
    }

    private void fillFields(Alarm alarm, ContentValues values) {
        values.put(Column.TITLE.toString(), alarm.getTitle());
        values.put(Column.TRIGGER_TIME.toString(), alarm.getTriggerTime().toCalendar().getTimeInMillis());
        values.put(Column.REPETITION.toString(), Utils.convertIntArrayToString(context, alarm.getRepetition()));
        values.put(Column.SLEEP_CHECKER_ON.toString(), alarm.isSleepCheckerOn() ? 1 : 0);
        values.put(Column.VIBRATE.toString(), alarm.vibrates() ? 1 : 0);
        values.put(Column.RINGTONE_URI.toString(), alarm.getRingtoneUri().toString());
        values.put(Column.VOLUME.toString(), alarm.getVolume());
        values.put(Column.TEXT.toString(), alarm.getText());
        values.put(Column.SNOOZE_DURATION.toString(), alarm.getSnoozeDuration().getValue());
        values.put(Column.ACTIVE.toString(), alarm.isActive() ? 1 : 0);
    }

    private ArrayList<Alarm> queryAlarms(Cursor cursor) {
        ArrayList<Alarm> alarms = new ArrayList<>();
        do {
            String title, ringtoneUri, text;
            long trigger, snooze;
            int volume;
            SnoozeDuration snoozeDuration;
            boolean active, sleepCheckerOn, vibrate;
            int[] repetition;

            title = cursor.getString(cursor.getColumnIndex(Column.TITLE.toString()));
            text = cursor.getString(cursor.getColumnIndex(Column.TEXT.toString()));

            trigger = cursor.getLong(cursor.getColumnIndex(Column.TRIGGER_TIME.toString()));
            Time triggerTime = Time.valueOf(trigger);

            active = cursor.getInt(cursor.getColumnIndex(Column.ACTIVE.toString())) == 1;
            sleepCheckerOn = cursor.getInt(cursor.getColumnIndex(Column.SLEEP_CHECKER_ON.toString())) == 1;
            ringtoneUri = cursor.getString(cursor.getColumnIndex(Column.RINGTONE_URI.toString()));
            volume = cursor.getInt(cursor.getColumnIndex(Column.VOLUME.toString()));
            snooze = cursor.getLong(cursor.getColumnIndex(Column.SNOOZE_DURATION.toString()));
            snoozeDuration = SnoozeDuration.valueOf(snooze);
            vibrate = cursor.getInt(cursor.getColumnIndex(Column.VIBRATE.toString())) == 1;

            repetition = Utils.convertStringToIntArray(context,
                                                       cursor.getString(cursor.getColumnIndex(Column.REPETITION.toString())));
            Uri ringtone = Uri.parse(ringtoneUri);

            AlarmBuilder alarmBuilder = new AlarmBuilder().setTitle(title)
                                                                 .setTriggerTime(triggerTime)
                                                                 .setSnoozeDuration(snoozeDuration)
                                                                 .setRepetition(repetition)
                                                                 .setRingtoneUri(ringtone)
                                                                 .setText(text)
                                                                 .setSleepCheckerOn(sleepCheckerOn)
                                                                 .setVibrate(vibrate)
                                                                 .setVolume(volume)
                                                                 .setActive(active);
            int id = cursor.getInt(cursor.getColumnIndex(Column.ID.toString()));
            alarmBuilder.setId(id);
            alarms.add(alarmBuilder.build());
        } while (cursor.moveToNext());
        return alarms;
    }

}
