package com.ukdev.smartbuzz.model;

import android.content.Context;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.util.Calendar;

/**
 * Alarm builder
 *
 * @author Alan Camargo
 */
public class AlarmBuilder {

    private Alarm alarm;

    /**
     * Default constructor for {@code AlarmBuilder}
     * @param context the Android context
     */
    public AlarmBuilder(Context context) {
        alarm = new Alarm(context);
    }

    /**
     * Sets the ID
     * @param id the ID
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setId(int id) {
        alarm.setId(id);
        return this;
    }

    /**
     * Sets the title
     * @param title the title
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setTitle(String title) {
        alarm.setTitle(title);
        return this;
    }

    /**
     * Sets the trigger time
     * @param triggerTime the trigger time
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setTriggerTime(Calendar triggerTime) {
        alarm.setTriggerTime(triggerTime);
        return this;
    }

    /**
     * Sets the snooze duration
     * @param snoozeDuration the snooze duration
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setSnoozeDuration(SnoozeDuration snoozeDuration) {
        alarm.setSnoozeDuration(snoozeDuration);
        return this;
    }

    /**
     * Sets the repetition
     * @param repetition the repetition
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setRepetition(Day[] repetition) {
        alarm.setRepetition(repetition);
        return this;
    }

    /**
     * Sets the ringtone
     * @param ringtone the ringtone
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setRingtone(Ringtone ringtone) {
        alarm.setRingtone(ringtone);
        return this;
    }

    /**
     * Sets the text
     * @param text the text
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setText(String text) {
        alarm.setText(text);
        return this;
    }

    /**
     * Sets the {@code sleepCheckerOn} flag
     * @param isSleepCheckerOn the flag value
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setSleepCheckerOn(boolean isSleepCheckerOn) {
        alarm.setSleepCheckerOn(isSleepCheckerOn);
        return this;
    }

    /**
     * Sets the {@code active} flag
     * @param isActive the flag value
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setActive(boolean isActive) {
        alarm.setActive(isActive);
        return this;
    }

    /**
     * Sets the {@code vibrate} flag
     * @param vibrate the flag value
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setVibrate(boolean vibrate) {
        alarm.setVibrate(vibrate);
        return this;
    }

    /**
     * Sets the volume
     * @param volume the volume
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setVolume(int volume) {
        alarm.setVolume(volume);
        return this;
    }

    /**
     * Builds the {@link Alarm} object
     * @return the built object
     */
    public Alarm build() {
        return alarm;
    }

}
