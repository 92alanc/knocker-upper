package com.ukdev.smartbuzz.model;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.extras.AudioFocusChangeListener;

import java.io.IOException;
import java.util.Calendar;

import static android.content.Context.AUDIO_SERVICE;

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
    private Calendar triggerTime;
    private MediaPlayer player;
    private Vibrator vibrator;

    /**
     * Instantiates the class
     * @param id - int
     * @param title - String
     * @param triggerTime - Calendar
     * @param ringtone - RingtoneWrapper
     * @param volume - int
     * @param vibrate - boolean
     * @param reminder - boolean
     * @param on - boolean
     * @param repetition - int[]
     * @param snooze - int
     */
    public Alarm(int id, String title, Calendar triggerTime,
                 RingtoneWrapper ringtone, int volume, boolean vibrate,
                 boolean reminder, boolean on, int[] repetition, int snooze)
    {
        this.id = id;
        this.title = title;
        this.triggerTime = triggerTime;
        this.ringtone = ringtone;
        this.volume = volume;
        this.vibrate = vibrate;
        this.reminder = reminder;
        this.on = on;
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
    public Calendar getTriggerTime()
    {
        return triggerTime;
    }

    /**
     * Sets the trigger time
     * @param triggerTime - TimeWrapper
     */
    public void setTriggerTime(Calendar triggerTime)
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
        if (activity.getIntent().getAction().equals(AppConstants.ACTION_MAYHEM))
        {
            volume = manager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            vibrator.vibrate(AppConstants.VIBRATION_PATTERN, 0);
        }
        else
        {
            volume = this.getVolume();
            if (this.vibrates())
                vibrator.vibrate(AppConstants.VIBRATION_PATTERN, 0);
        }
        manager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        int requestResult;
        if ( AppConstants.OS_VERSION >= Build.VERSION_CODES.KITKAT)
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                                                                                   this.getVolume()),
                                                      AudioManager.STREAM_ALARM,
                                                      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        else
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager,
                                                                                   this.getVolume()),
                                                      AudioManager.STREAM_ALARM,
                                                      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try
            {
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
