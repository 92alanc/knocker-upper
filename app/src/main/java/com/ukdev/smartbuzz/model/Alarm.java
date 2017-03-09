package com.ukdev.smartbuzz.model;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.util.Calendar;

public class Alarm {

    private Context context;
    private int id;
    private String title;
    private Calendar triggerTime;
    private SnoozeDuration snoozeDuration;
    private Day[] repetition;
    private RingtoneWrapper ringtone;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;
    private boolean vibrate;
    private Vibrator vibrator;
    private int volume;
    private MediaPlayer mediaPlayer;

    public Alarm(Context context) {
        this(context, 0, "", Calendar.getInstance(), SnoozeDuration.FIVE_MINUTES, null, null,
                "", true, true, 4);
    }

    public Alarm(Context context, int id, String title, Calendar triggerTime, SnoozeDuration snoozeDuration,
                 Day[] repetition, RingtoneWrapper ringtone, String text, boolean sleepCheckerOn,
                 boolean vibrate, int volume) {
        this.context = context;
        this.id = id;
        this.title = title;
        this.triggerTime = triggerTime;
        this.snoozeDuration = snoozeDuration;
        this.repetition = repetition;
        this.ringtone = ringtone;
        this.text = text;
        this.sleepCheckerOn = sleepCheckerOn;
        this.vibrate = vibrate;
        this.volume = volume;
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
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

    public boolean vibrates() {
        return vibrate;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Vibrator getVibrator() {
        return vibrator;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
