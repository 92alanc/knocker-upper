package com.ukdev.smartbuzz.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Process;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * General utilities
 *
 * @author Alan Camargo
 */
public class Utils {

    private static final int LENGTH_WEEK_DAYS = 5;
    private static final int LENGTH_WEEKEND = 2;
    private static final int LENGTH_WHOLE_WEEK = 7;

    /**
     * Converts a {@code int} array to string
     * @param context the Android context
     * @param array the array
     * @return the array as a string
     */
    public static String convertIntArrayToString(Context context, Integer[] array) {
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
    public static Integer[] convertStringToIntArray(Context context, String string) {
        if (string == null || string.equals(""))
            return null;
        else {
            Integer[] values;
            if (string.equals(context.getString(R.string.every_day))) {
                values = new Integer[LENGTH_WHOLE_WEEK];
                for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
                    values[i - 1] = i;
            } else if (string.equals(context.getString(R.string.week_days))) {
                values = new Integer[LENGTH_WEEK_DAYS];
                int j = 0;
                for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
                    values[j] = i;
                    j++;
                }
            } else if (string.equals(context.getString(R.string.weekends))) {
                values = new Integer[LENGTH_WEEKEND];
                values[0] = Calendar.SUNDAY;
                values[1] = Calendar.SATURDAY;
            } else {
                String[] texts = context.getResources()
                                        .getStringArray(R.array.days_of_the_week_short);
                String[] split = string.split(", ");
                values = new Integer[split.length];
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
    public static SparseBooleanArray convertIntArrayToSparseBooleanArray(Integer[] array) {
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
    public static Integer[] convertSparseBooleanArrayToIntArray(SparseBooleanArray sparseBooleanArray) {
        List<Integer> integerList = new ArrayList<>();
        int j = 0;
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            if (sparseBooleanArray.get(i)) {
                integerList.add(sparseBooleanArray.keyAt(j));
                j++;
            }
        }
        Integer[] intArray = new Integer[integerList.size()];
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
        Time time = Time.valueOf(alarm.getTriggerTime());
        int hours = time.getHour();
        int minutes = time.getMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar now = Calendar.getInstance();

        if (calendar.compareTo(now) <= 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);
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
        System.exit(0);
    }

    /**
     * Determines whether the user has granted the read storage permission
     * @param activity the activity
     * @return {@code true} if positive, otherwise {@code false}
     */
    public static boolean hasStoragePermission(Activity activity) {
        Context context = activity.getBaseContext();
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests the read storage permission
     */
    public static void requestStoragePermission(Activity activity) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        final int requestCode = 123;
        String[] permissions = {permission};
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * Starts vibration
     * @param vibrator the device's vibrator
     */
    public static void startVibration(Vibrator vibrator) {
        long[] pattern = { Time.ONE_SECOND, Time.ONE_SECOND };
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
