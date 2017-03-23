package com.ukdev.smartbuzz.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.ukdev.smartbuzz.backend.Utils;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.RingtoneWrapper;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmRepository extends BaseRepository {

    private static final String TABLE_NAME = "ALARMS";

    @SuppressLint("StaticFieldLeak")
    private static AlarmRepository instance;

    public static AlarmRepository getInstance(Context context) {
        if (instance == null)
            instance = new AlarmRepository(context);
        return instance;
    }

    private AlarmRepository(Context context) {
        super(context);
    }

    @Override
    public void delete(Alarm alarm) {
        writer.delete(TABLE_NAME,
                Column.ID.toString() + " = ?", new String[]{String.valueOf(alarm.getId())});
    }

    @Override
    public void insert(Alarm alarm) {
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        writer.insert(TABLE_NAME, null, values);
    }

    @Override
    public int getLastId() {
        return 0;
    }

    @Override
    public ArrayList<Alarm> select() {
        Cursor cursor = reader.query(TABLE_NAME,
                null, null, null, null, null,
                Column.ID.toString() + " ASC");
        ArrayList<Alarm> alarms = new ArrayList<>();
        if (cursor.getCount() > 0)
            alarms = queryAlarms(cursor);
        cursor.close();
        return alarms;
    }

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

    @Override
    public ArrayList<Alarm> getActiveAlarms() {
        Cursor cursor = reader.query(TABLE_NAME, null, Column.ACTIVE.toString() + " = ?",
                new String[]{"1"}, null, null, null);
        ArrayList<Alarm> alarms = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0)
            alarms = queryAlarms(cursor);
        cursor.close();
        return alarms;
    }

    @Override
    public void update(Alarm alarm) {
        ContentValues values = new ContentValues();
        fillFields(alarm, values);
        writer.update(TABLE_NAME, values,
                Column.ID.toString() + " = ?", new String[]{String.valueOf(alarm.getId())});
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
        RingtoneWrapper ringtone = new RingtoneWrapper(ringtoneTitle, Uri.parse(ringtoneUri));

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
