package com.ukdev.smartbuzz.model;

import android.media.RingtoneManager;
import android.net.Uri;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

/**
 * An alarm
 *
 * @author Alan Camargo
 */
public class Alarm {

    private int id;
    private String title;
    private Time triggerTime;
    private SnoozeDuration snoozeDuration;
    private int[] repetition;
    private Uri ringtoneUri;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;
    private boolean vibrate;
    private int volume;

    /**
     * Simplified constructor for {@code Alarm}
     */
    Alarm() {
        active = true;
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        vibrate = true;
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * Gets the database ID
     * @return the database ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the database ID
     * @param id the database ID
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * @param title the title
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the trigger time
     * @return the trigger time
     */
    public Time getTriggerTime() {
        return triggerTime;
    }

    /**
     * Sets the trigger time
     * @param triggerTime the trigger time
     */
    void setTriggerTime(Time triggerTime) {
        this.triggerTime = triggerTime;
    }

    /**
     * Gets the snooze duration
     * @return the snooze duration
     */
    public SnoozeDuration getSnoozeDuration() {
        return snoozeDuration;
    }

    /**
     * Sets the snooze duration
     * @param snoozeDuration the snooze duration
     */
    void setSnoozeDuration(SnoozeDuration snoozeDuration) {
        this.snoozeDuration = snoozeDuration;
    }

    /**
     * Gets the repetition
     * @return the repetition
     */
    public int[] getRepetition() {
        return repetition;
    }

    /**
     * Sets the repetition
     * @param repetition the repetition
     */
    void setRepetition(int[] repetition) {
        this.repetition = repetition;
    }

    /**
     * Determines whether the alarm repeats
     * throughout the week
     * @return {@code true} if positive,
     *         otherwise {@code false}
     */
    public boolean repeats() {
        return repetition != null && repetition.length > 0;
    }

    /**
     * Gets the ringtone URI
     * @return the ringtone URI
     */
    public Uri getRingtoneUri() {
        return ringtoneUri;
    }

    /**
     * Sets the ringtone URI
     * @param ringtoneUri the ringtone URI
     */
    void setRingtoneUri(Uri ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    /**
     * Gets the text
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text
     * @param text the text
     */
    void setText(String text) {
        this.text = text;
    }

    /**
     * Determines whether Sleep Checker should be called
     * @return {@code true} if positive
     *         otherwise {@code false}
     */
    public boolean isSleepCheckerOn() {
        return sleepCheckerOn;
    }

    /**
     * Sets the {@code sleepCheckerOn} flag
     * @param sleepCheckerOn the flag value
     */
    void setSleepCheckerOn(boolean sleepCheckerOn) {
        this.sleepCheckerOn = sleepCheckerOn;
    }

    /**
     * Determines whether the alarm is active.
     * <p>Active can be understood as the alarm
     * being scheduled</p>
     * @return {@code true} if positive,
     *         otherwise {@code false}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the {@code active} flag
     * @param active the flag value
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Determines whether the alarm vibrates
     * @return {@code true} if positive,
     *         otherwise {@code false}
     */
    public boolean vibrates() {
        return vibrate;
    }

    /**
     * Sets the {@code vibrate} flag
     * @param vibrate the flag value
     */
    void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    /**
     * Gets the volume
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Sets the volume
     * @param volume the volume
     */
    void setVolume(int volume) {
        this.volume = volume;
    }

}
