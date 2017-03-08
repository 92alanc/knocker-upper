package com.ukdev.smartbuzz.model;

import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.util.Calendar;

public class Alarm {

    private int id;
    private String title;
    private Calendar triggerTime;
    private SnoozeDuration snoozeDuration;
    private Day[] repetition;
    private RingtoneWrapper ringtone;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;

    public Alarm() {
        this(0, "", Calendar.getInstance(), SnoozeDuration.FIVE_MINUTES, null, null, "", true);
    }

    public Alarm(int id, String title, Calendar triggerTime, SnoozeDuration snoozeDuration, Day[] repetition,
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

    public SnoozeDuration getSnoozeDuration() {
        return snoozeDuration;
    }

    public void setSnoozeDuration(SnoozeDuration snoozeDuration) {
        this.snoozeDuration = snoozeDuration;
    }

    public Day[] getRepetition() {
        return repetition;
    }

    public void setRepetition(Day[] repetition) {
        this.repetition = repetition;
    }

    public boolean repeats() {
        return repetition != null && repetition.length > 0;
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
