package com.ukdev.smartbuzz.model;

import java.util.Calendar;

public class Alarm {

    private int id;
    private String title;
    private Calendar triggerTime;
    private int snoozeDuration;
    private Day[] repetition;
    private RingtoneWrapper ringtone;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;

    public Alarm() {
        this(0, "", Calendar.getInstance(), 0, null, null, "", true);
    }

    public Alarm(int id, String title, Calendar triggerTime, int snoozeDuration, Day[] repetition,
                 RingtoneWrapper ringtone, String text, boolean sleepCheckerOn) {
        this.id = id;
        this.title = title;
        this.triggerTime = triggerTime;
        this.snoozeDuration = snoozeDuration;
        this.repetition = repetition;
        this.ringtone = ringtone;
        this.text = text;
        this.sleepCheckerOn = sleepCheckerOn;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Calendar triggerTime) {
        this.triggerTime = triggerTime;
    }

    public int getSnoozeDuration() {
        return snoozeDuration;
    }

    public void setSnoozeDuration(int snoozeDuration) {
        this.snoozeDuration = snoozeDuration;
    }

    public Day[] getRepetition() {
        return repetition;
    }

    public void setRepetition(Day[] repetition) {
        this.repetition = repetition;
    }

    public RingtoneWrapper getRingtone() {
        return ringtone;
    }

    public void setRingtone(RingtoneWrapper ringtone) {
        this.ringtone = ringtone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSleepCheckerOn() {
        return sleepCheckerOn;
    }

    public void setSleepCheckerOn(boolean sleepCheckerOn) {
        this.sleepCheckerOn = sleepCheckerOn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
