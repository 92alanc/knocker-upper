package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.os.Vibrator;
import android.util.SparseBooleanArray;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.AudioFocusChangeListener;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.LogTool;
import com.ukdev.smartbuzz.model.Alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * General utilities
 *
 * @author Alan Camargo
 */
public class Utils {

    private static final int DAY_OF_MONTH = 1;
    private static final int LENGTH_WEEK_DAYS = 5;
    private static final int LENGTH_WEEKEND = 2;
    private static final int LENGTH_WHOLE_WEEK = 7;
    private static final int MILLISECOND = 0;
    private static final int SECOND = 0;
    private static final int STATUS_SUCCESS = 0;

    /**
     * Converts a {@code int} array to string
     * @param context the Android context
     * @param array the array
     * @return the array as a string
     */
    public static String convertIntArrayToString(Context context, int[] array) {
        if (array == null || array.length == 0)
            return null;
        else {
            if (array.length == LENGTH_WHOLE_WEEK)
                return context.getString(R.string.every_day);
            else {
                boolean weekDays = true;
                boolean weekends = true;
                if (array.length != LENGTH_WEEK_DAYS)
                    weekDays = false;
                if (array.length != LENGTH_WEEKEND)
                    weekends = false;
                if (array.length == LENGTH_WEEK_DAYS || array.length == LENGTH_WEEKEND) {
                    for (int day : array) {
                        if (day != Calendar.SUNDAY && day != Calendar.SATURDAY)
                            weekends = false;
                        if (day != Calendar.MONDAY
                                && day != Calendar.TUESDAY
                                && day != Calendar.WEDNESDAY
                                && day != Calendar.THURSDAY
                                && day != Calendar.FRIDAY) {
                            weekDays = false;
                        }
                    }
                }
                if (weekDays)
                    return context.getString(R.string.week_days);
                else if (weekends)
                    return context.getString(R.string.weekends);
                else {
                    String[] texts = context.getResources()
                                            .getStringArray(R.array.days_of_the_week_short);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < array.length; i++) {
                        for (int j = 0; j < texts.length; j++) {
                            if (j == (array[i] - 1)) {
                                sb.append(texts[j]);
                                if (i < array.length - 1)
                                    sb.append(", ");
                                break;
                            }
                        }
                    }
                    return sb.toString();
                }
            }
        }
    }

    /**
     * Converts a string to an {@code int} array
     * @param context the Android context
     * @param string the string
     * @return the string as an {@code int} array
     */
    public static int[] convertStringToIntArray(Context context, String string) {
        if (string == null || string.equals(""))
            return null;
        else {
            int[] values;
            if (string.equals(context.getString(R.string.every_day))) {
                values = new int[LENGTH_WHOLE_WEEK];
                for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
                    values[i - 1] = i;
            } else if (string.equals(context.getString(R.string.week_days))) {
                values = new int[LENGTH_WEEK_DAYS];
                int j = 0;
                for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
                    values[j] = i;
                    j++;
                }
            } else if (string.equals(context.getString(R.string.weekends))) {
                values = new int[LENGTH_WEEKEND];
                values[0] = Calendar.SUNDAY;
                values[1] = Calendar.SATURDAY;
            } else {
                String[] texts = context.getResources()
                                        .getStringArray(R.array.days_of_the_week_short);
                String[] split = string.split(", ");
                values = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    for (int j = 0; j < texts.length; j++) {
                        if (split[i].equalsIgnoreCase(texts[j])) {
                            values[i] = j + 1;
                            break;
                        }
                    }
                }
            }
            return values;
        }
    }

    /**
     * Converts an {@code int} array to a
     * {@code SparseBooleanArray}
     * @param array the {@code int} array
     * @return the converted array
     */
    public static SparseBooleanArray convertIntArrayToSparseBooleanArray(int[] array) {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        if (array != null) {
            for (int i : array)
                sparseBooleanArray.put(i, true);
        }
        return sparseBooleanArray;
    }

    /**
     * Converts a {@code SparseBooleanArray} to
     * an {@code int} array
     * @param sparseBooleanArray the {@code SparseBooleanArray}
     * @return the converted array
     */
    public static int[] convertSparseBooleanArrayToIntArray(SparseBooleanArray sparseBooleanArray) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            if (sparseBooleanArray.get(i)) {
                int index = i - 1;
                integerList.add(sparseBooleanArray.keyAt(index));
            }
        }
        int[] intArray = new int[integerList.size()];
        for (int i = 0; i < intArray.length; i++)
            intArray[i] = integerList.get(i);
        return intArray;
    }

    /**
     * Gets the next valid trigger time for an alarm.
     *
     * <p>Case 1: if the current time is 16:00 and the
     * alarm's trigger time is 17:00 without a specific
     * day, then the next valid trigger time will be
     * 17:00.</p>
     *
     * <p>Case 2: if the current time is 16:00 and the
     * alarm's trigger time is 15:00 without a specific
     * day, then the next valid trigger time will be
     * 15:00 of the following day.</p>
     * @param alarm the alarm
     * @return the next valid trigger time
     */
    public static long getNextValidTriggerTime(Alarm alarm) {
        int hours = alarm.getTriggerTime().getHour();
        int minutes = alarm.getTriggerTime().getMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, SECOND);
        calendar.set(Calendar.MILLISECOND, MILLISECOND);
        Calendar now = Calendar.getInstance();

        if ((hours <= now.get(Calendar.HOUR_OF_DAY)
                || minutes > now.get(Calendar.MINUTE))
                && (hours < now.get(Calendar.HOUR_OF_DAY)
                || minutes <= now.get(Calendar.MINUTE))) {
            calendar.add(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
        }
        return calendar.getTimeInMillis();
    }

    /**
     * Gets the default volume, which is a
     * half of the maximum volume
     * @param context the Android context
     * @return the default volume
     */
    public static int getDefaultVolume(Context context) {
        return getMaxVolume(context) / 2;
    }

    /**
     * Gets the device's maximum volume
     * @param context the Android context
     * @return the device's maximum volume
     */
    public static int getMaxVolume(Context context) {
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    /**
     * Kills the application
     * @param activity the running activity
     */
    public static void killApp(Activity activity) {
        final boolean nonRoot = true;
        activity.moveTaskToBack(nonRoot);
        activity.finish();
        Process.killProcess(Process.myPid());
        System.exit(STATUS_SUCCESS);
    }

    /**
     * Gets a formatted time string.
     * i.e.: if an hour value is 6 (less than 10,
     * the return will be 06, otherwise the return
     * is the value itself as string.
     * @param time the time
     * @return the formatted time string
     */
    public static String getFormattedTimeString(int time) {
        String formatted = String.valueOf(time);
        if (time < 10)
            formatted = "0" + formatted;
        return formatted;
    }

    /**
     * Plays a ringtone
     * @param activity the activity
     * @param player the media player
     * @param volume the volume
     * @param ringtoneUri the ringtone URI
     */
    public static void playRingtone(Activity activity, MediaPlayer player,
                                    int volume, Uri ringtoneUri) {
        Context context = activity.getBaseContext();
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (activity.getIntent().getAction().equals(IntentAction.WAKE_UP.toString()))
            volume = getMaxVolume(context);
        final int flags = 0;
        manager.setStreamVolume(AudioManager.STREAM_ALARM, volume, flags);
        int requestResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager, volume),
                                                      AudioManager.STREAM_ALARM,
                                                      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        } else {
            requestResult = manager.requestAudioFocus(new AudioFocusChangeListener(manager, volume),
                                                      AudioManager.STREAM_ALARM,
                                                      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.setDataSource(context, ringtoneUri);
                player.prepare();
            } catch (IOException e) {
                LogTool log = new LogTool(context);
                log.exception(e);
            }
            player.start();
        }
    }

    /**
     * Stops a ringtone
     * @param player the media player
     */
    public static void stopRingtone(MediaPlayer player) {
        player.release();
    }

    /**
     * Starts vibration
     * @param vibrator the device's vibrator
     */
    public static void startVibration(Vibrator vibrator) {
        long[] pattern = { 1000, 2000 };
        final int repeat = 0;
        vibrator.vibrate(pattern, repeat);
    }

    /**
     * Stops a vibration
     * @param vibrator the device's vibrator
     */
    public static void stopVibration(Vibrator vibrator) {
        vibrator.cancel();
    }

}
