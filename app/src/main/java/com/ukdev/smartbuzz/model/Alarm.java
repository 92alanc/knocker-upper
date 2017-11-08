package com.ukdev.smartbuzz.model;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import com.ukdev.smartbuzz.annotations.SnoozeDuration;
import com.ukdev.smartbuzz.listeners.AudioFocusChangeListener;
import com.ukdev.smartbuzz.util.Utils;

import java.io.IOException;
import java.util.Calendar;

/**
 * An alarm
 *
 * @author Alan Camargo
 */
public class Alarm {

    private int id;
    private String name;
    private long triggerTime;

    @SnoozeDuration
    private long snoozeDuration;

    private Integer[] repetition;
    private Uri ringtoneUri;
    private Uri wallpaperUri;
    private String text;
    private boolean sleepCheckerOn;
    private boolean active;
    private boolean vibrate;
    private int volume;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    /**
     * Simplified constructor for {@code Alarm}
     */
    Alarm() {
        active = true;
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        snoozeDuration = Time.FIVE_MINUTES;
        triggerTime = Calendar.getInstance().getTimeInMillis();
        vibrate = true;
    }

    @Override
    public String toString() {
        return name;
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
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * @param name the name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the trigger time
     * @return the trigger time
     */
    public long getTriggerTime() {
        return triggerTime;
    }

    /**
     * Sets the trigger time
     * @param triggerTime the trigger time
     */
    void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
    }

    /**
     * Gets the snooze duration
     * @return the snooze duration
     */
    @SnoozeDuration
    public long getSnoozeDuration() {
        return snoozeDuration;
    }

    /**
     * Sets the snooze duration
     * @param snoozeDuration the snooze duration
     */
    void setSnoozeDuration(@SnoozeDuration long snoozeDuration) {
        this.snoozeDuration = snoozeDuration;
    }

    /**
     * Gets the repetition
     * @return the repetition
     */
    public Integer[] getRepetition() {
        return repetition;
    }

    /**
     * Sets the repetition
     * @param repetition the repetition
     */
    void setRepetition(Integer[] repetition) {
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
     * Gets the wallpaper URI
     * @return the wallpaper URI
     */
    public Uri getWallpaperUri() {
        return wallpaperUri;
    }

    /**
     * Sets the wallpaper URI
     * @param wallpaperUri the wallpaper URI
     */
    void setWallpaperUri(Uri wallpaperUri) {
        this.wallpaperUri = wallpaperUri;
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
     * Gets the media player
     * @return the media player
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Gets the vibrator
     * @return the vibrator
     */
    public Vibrator getVibrator() {
        return vibrator;
    }

    /**
     * Plays the ringtone
     * @param activity the activity
     * @param hellMode whether the ringtone should
     *                 be played at the maximum volume
     */
    public void playRingtone(Activity activity, boolean hellMode) {
        Context context = activity.getApplicationContext();
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        int volume;
        final long[] vibrationPattern = {Time.ONE_SECOND, Time.ONE_SECOND};
        if (hellMode) {
            volume = Utils.getMaxVolume(context);
            vibrator.vibrate(vibrationPattern, 0);
        } else {
            volume = this.volume;
            if (vibrate && vibrator != null)
                vibrator.vibrate(vibrationPattern, 0);
        }
        if (ringtoneUri == null && !hellMode)
            return;
        if (manager == null)
            return;
        manager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        int requestResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager, volume),
                                                      AudioManager.STREAM_ALARM,
                                                      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        else
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager, volume),
                                                      AudioManager.STREAM_ALARM,
                                                      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            try {
                if (ringtoneUri == null)
                    ringtoneUri = RingtoneManager.getValidRingtoneUri(context);
                mediaPlayer.setDataSource(context, ringtoneUri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
    }

}
