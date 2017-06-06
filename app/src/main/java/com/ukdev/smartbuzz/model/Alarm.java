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
import com.ukdev.smartbuzz.misc.LogTool;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

import java.io.IOException;
import java.util.Calendar;

/**
 * An alarm
 *
 * @author Alan Camargo
 */
public class Alarm {

    private Context context;
    private int id;
    private String title;
    private Calendar triggerTime;
    private SnoozeDuration snoozeDuration;
    private Day[] repetition;
    private Ringtone ringtone;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;
    private boolean vibrate;
    private int volume;
    private Vibrator vibrator;
    private MediaPlayer player;

    /**
     * Simplified constructor for {@code Alarm}
     * @param context the Android context
     */
    Alarm(Context context) {
        this.context = context;
        player = new MediaPlayer();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
    public Calendar getTriggerTime() {
        return triggerTime;
    }

    /**
     * Sets the trigger time
     * @param triggerTime the trigger time
     */
    void setTriggerTime(Calendar triggerTime) {
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
    public Day[] getRepetition() {
        return repetition;
    }

    /**
     * Sets the repetition
     * @param repetition the repetition
     */
    void setRepetition(Day[] repetition) {
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
     * Gets the ringtone
     * @return the ringtone
     */
    public Ringtone getRingtone() {
        return ringtone;
    }

    /**
     * Sets the ringtone
     * @param ringtone the ringtone
     */
    void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
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

    /**
     * Plays the ringtone
     * @param activity the target activity
     */
    public void playRingtone(Activity activity) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume;
        if (activity.getIntent().getAction().equals(Action.WAKE_UP.toString())) {
            volume = Utils.getMaxVolume(context);
            startVibration();
        }
        else {
            volume = getVolume();
            if (vibrates())
                startVibration();
        }
        manager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        int requestResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                            getVolume()),
                    AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        }
        else {
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                            getVolume()),
                    AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.setDataSource(context, getRingtone().getUri());
                player.prepare();
            } catch (IOException e) {
                LogTool log = new LogTool(context);
                log.exception(e);
            }
            player.start();
        }
    }

    /**
     * Stops playing the ringtone
     */
    public void stopRingtone() {
        player.release();
        if (vibrates())
            stopVibration();
    }

    /**
     * Starts the vibration
     */
    public void startVibration() {
        long[] pattern = { 1000, 2000 };
        vibrator.vibrate(pattern, 0);
    }

    /**
     * Stops the vibration
     */
    public void stopVibration() {
        vibrator.cancel();
    }

}
