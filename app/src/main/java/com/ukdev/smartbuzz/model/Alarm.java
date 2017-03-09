package com.ukdev.smartbuzz.model;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;
import com.ukdev.smartbuzz.backend.AudioFocusChangeListener;
import com.ukdev.smartbuzz.backend.Utils;
import com.ukdev.smartbuzz.backend.enums.Action;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.io.IOException;
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
    private MediaPlayer player;

    private static final long[] VIBRATION_PATTERN = {};

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

    @Override
    public String toString() {
        return title;
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

    public void playRingtone(Activity activity) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        player = new MediaPlayer();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        int volume;
        if (activity.getIntent().getAction().equals(Action.WAKE_UP.toString())) {
            volume = Utils.getMaxVolume(context);
            vibrator.vibrate(VIBRATION_PATTERN, 0);
        }
        else {
            volume = this.getVolume();
            if (this.vibrates())
                vibrator.vibrate(VIBRATION_PATTERN, 0);
        }
        manager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        int requestResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                            this.getVolume()),
                    AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        }
        else {
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                            this.getVolume()),
                    AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.setDataSource(context, this.getRingtone().getUri());
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
        }
    }

    public void stopRingtone() {
        player.release();
    }

    public void startVibration() {

    }

    public void stopVibration() {
        vibrator.cancel();
    }

}
