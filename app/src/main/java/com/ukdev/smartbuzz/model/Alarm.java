package com.ukdev.smartbuzz.model;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.extras.AudioFocusChangeListener;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;
import static android.media.AudioAttributes.USAGE_ALARM;

/**
 * Alarm class
 * Created by Alan Camargo - April 2016
 */
public class Alarm
{

    private String title;
    private RingtoneWrapper ringtone;
    private int id, volume, snooze;
    private boolean on, reminder, vibrate, locked;
    private int[] repetition;
    private TimeWrapper triggerTime;
    private TimeZoneWrapper timeZone;
    private MediaPlayer player;
    private Vibrator vibrator;

    /**
     * Instantiates the class
     * @param id - int
     * @param title - String
     * @param triggerTime - TimeWrapper
     * @param ringtone - RingtoneWrapper
     * @param volume - int
     * @param vibrate - boolean
     * @param reminder - boolean
     * @param on - boolean
     * @param timeZone - TimeZoneWrapper
     * @param repetition - int[]
     * @param snooze - int
     */
    public Alarm(int id, String title, TimeWrapper triggerTime,
                 RingtoneWrapper ringtone, int volume, boolean vibrate, boolean reminder, boolean on,
                 TimeZoneWrapper timeZone, int[] repetition, int snooze)
    {
        this.id = id;
        this.title = title;
        this.triggerTime = triggerTime;
        this.ringtone = ringtone;
        this.volume = volume;
        this.vibrate = vibrate;
        this.reminder = reminder;
        this.on = on;
        this.timeZone = timeZone;
        this.repetition = repetition;
        this.snooze = snooze;
        locked = false;
    }

    /**
     * Gets the alarm as String
     * @return alarm
     */
    @Override
    public String toString()
    {
        return title;
    }

    /**
     * Gets the title
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the title
     * @param title - String
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Gets the snooze duration
     * @return snooze
     */
    public int getSnooze()
    {
        return snooze;
    }

    /**
     * Sets the snooze duration
     * @param snooze - int
     */
    public void setSnooze(int snooze)
    {
        this.snooze = snooze;
    }

    /**
     * Gets the trigger time
     * @return trigger time
     */
    public TimeWrapper getTriggerTime()
    {
        return triggerTime;
    }

    /**
     * Sets the trigger time
     * @param triggerTime - TimeWrapper
     */
    public void setTriggerTime(TimeWrapper triggerTime)
    {
        this.triggerTime = triggerTime;
    }

    /**
     * Gets the repetition
     * @return repetition
     */
    public int[] getRepetition()
    {
        return repetition;
    }

    /**
     * Sets the repetition
     * @param repetition - int[]
     */
    public void setRepetition(int[] repetition)
    {
        this.repetition = repetition;
    }

    /**
     * Tells if the alarm repeats
     * @return repeats
     */
    public boolean repeats()
    {
        return this.repetition != null;
    }

    /**
     * Tells if the alarm has a different time zone
     * @return has different time zone
     */
    public boolean hasDifferentTimeZone()
    {
        boolean hoursDoNotMatch = (timeZone.getOffset().getHours() !=
                TimeZoneWrapper.getLocalTimeZoneOffset().getHours());
        boolean minutesDoNotMatch = (timeZone.getOffset().getMinutes() !=
                TimeZoneWrapper.getLocalTimeZoneOffset().getMinutes());
        return hoursDoNotMatch || minutesDoNotMatch;
    }

    /**
     * Gets the time zone
     * @return time zone
     */
    public TimeZoneWrapper getTimeZone()
    {
        return timeZone;
    }

    /**
     * Sets the time zone
     * @param timeZone - TimeZoneWrapper
     */
    public void setTimeZone(TimeZoneWrapper timeZone)
    {
        this.timeZone = timeZone;
    }

    /**
     * Gets the ringtone
     * @return ringtone
     */
    public RingtoneWrapper getRingtone()
    {
        return ringtone;
    }

    /**
     * Sets the ringtone
     * @param ringtone - RingtoneWrapper
     */
    public void setRingtone(RingtoneWrapper ringtone)
    {
        this.ringtone = ringtone;
    }

    /**
     * Gets the volume
     * @return volume
     */
    public int getVolume()
    {
        return volume;
    }

    /**
     * Sets the volume
     * @param volume - the new volume value
     */
    public void setVolume(int volume)
    {
        this.volume = volume;
    }

    /**
     * Tells if the alarm vibrates
     * @return vibrates
     */
    public boolean vibrates()
    {
        return vibrate;
    }

    /**
     * Toggles vibration
     * @param vibrate - boolean
     */
    public void toggleVibration(boolean vibrate)
    {
        this.vibrate = vibrate;
    }

    /**
     * Tells if the alarm is on or off
     * @return isOn
     */
    public boolean isOn()
    {
        return on;
    }

    /**
     * Switches the alarm on/off
     * @param on - boolean
     */
    public void toggle(boolean on)
    {
        this.on = on;
    }

    /**
     * Sets the current alarm as a reminder
     * @param reminder - boolean
     */
    public void setAsReminder(boolean reminder)
    {
        this.reminder = reminder;
    }

    /**
     * Tells if the current alarm is a reminder
     * If true, SleepCheckerActivity will not be triggered
     * @return is reminder
     */
    public boolean isReminder()
    {
        return reminder;
    }

    /**
     * Locks/unlocks the alarm
     * @param locked - boolean
     */
    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    /**
     * Tells if the alarm is locked
     * @return locked
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * Gets the ID
     * @return id
     */
    public int getId() { return id; }

    /**
     * Gets the media player
     * @return player
     */
    public MediaPlayer getPlayer()
    {
        return player;
    }

    /**
     * Gets the vibrator
     * @return vibrator
     */
    public Vibrator getVibrator()
    {
        return vibrator;
    }

    /**
     * Plays the ringtone
     * @param activity - Activity
     * @param context - Context
     */
    public void playRingtone(Activity activity, Context context)
    {
        AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        player = new MediaPlayer();
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        int volume;
        AudioAttributes.Builder attributes = new AudioAttributes.Builder();
        attributes.setUsage(USAGE_ALARM);
        if (activity.getIntent().getAction().equals(AppConstants.ACTION_MAYHEM))
        {
            volume = manager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            vibrator.vibrate(AppConstants.VIBRATION_PATTERN, 0, attributes.build());
        }
        else
        {
            volume = this.getVolume();
            if (this.vibrates())
                vibrator.vibrate(AppConstants.VIBRATION_PATTERN, 0, attributes.build());
        }
        manager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        int requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                        this.getVolume()),
                AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try
            {
                if (this.getTitle().toLowerCase().contains(AppConstants.TITLE_ACADEMIA)
                        || this.getTitle().toLowerCase().contains(AppConstants.TITLE_TREINO))
                    player.setDataSource(context, AppConstants.AUDIO_BIRRR);
                else
                    player.setDataSource(context, this.getRingtone().getUri());
                player.prepare();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            player.start();
        }
    }

}
