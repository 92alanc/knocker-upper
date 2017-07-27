package com.ukdev.smartbuzz.model;

import android.net.Uri;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

/**
 * Alarm builder
 *
 * @author Alan Camargo
 */
public class AlarmBuilder {

    private Alarm alarm;

    /**
     * Default constructor for {@code AlarmBuilder}
     */
    public AlarmBuilder() {
        alarm = new Alarm();
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
    public AlarmBuilder setTriggerTime(long triggerTime) {
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
    public AlarmBuilder setRepetition(Integer[] repetition) {
        alarm.setRepetition(repetition);
        return this;
    }

    /**
     * Sets the ringtone URI
     * @param ringtoneUri the ringtone URI
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setRingtoneUri(Uri ringtoneUri) {
        alarm.setRingtoneUri(ringtoneUri);
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
     * Sets the wallpaper URI
     * @param wallpaperUri the wallpaper URI
     * @return the alarm builder after being updated
     */
    public AlarmBuilder setWallpaperUri(Uri wallpaperUri) {
        alarm.setWallpaperUri(wallpaperUri);
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
