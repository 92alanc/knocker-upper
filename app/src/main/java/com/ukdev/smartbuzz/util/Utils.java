package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Process;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.enums.Day;

import java.util.Calendar;

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
     * Converts a {@link Day} array to string
     * @param context the Android context
     * @param array the array
     * @return the array as a string
     */
    public static String convertDayArrayToString(Context context, Day[] array) {
        if (array == null)
            return null;
        else {
            if (array.length == LENGTH_WHOLE_WEEK)
                return context.getResources().getString(R.string.every_day);
            else {
                boolean weekDays = true;
                boolean weekends = true;
                for (Day day : array) {
                    if (array.length != LENGTH_WEEKEND || (day != Day.SUNDAY && day != Day.SATURDAY))
                        weekends = false;
                    if (array.length != LENGTH_WEEK_DAYS || (day != Day.MONDAY
                            && day != Day.TUESDAY
                            && day != Day.WEDNESDAY
                            && day != Day.THURSDAY
                            && day != Day.FRIDAY)) {
                        weekDays = false;
                    }
                }
                if (weekDays)
                    return context.getResources().getString(R.string.week_days);
                else if (weekends)
                    return context.getResources().getString(R.string.weekends);
                else {
                    String[] texts = context.getResources()
                                            .getStringArray(R.array.days_of_the_week_short);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < array.length; i++) {
                        for (int j = 0; j < texts.length; j++) {
                            if (j == (array[i].getValue() - 1)) {
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
     * Converts a string to a {@link Day} array
     * @param context the Android context
     * @param string the string
     * @return the string as a {@link Day} array
     */
    public static Day[] convertStringToDayArray(Context context, String string) {
        if (string == null)
            return null;
        else {
            Day[] values;
            if (string.equals(context.getResources().getString(R.string.every_day))) {
                values = new Day[LENGTH_WHOLE_WEEK];
                for (int i = 0; i < values.length; i++)
                    values[i] = Day.valueOf(i + 1);
            } else if (string.equals(context.getResources().getString(R.string.week_days))) {
                values = new Day[LENGTH_WEEK_DAYS];
                for (int i = 0; i < values.length; i++)
                    values[i] = Day.valueOf(i + 2);
            } else if (string.equals(context.getResources().getString(R.string.weekends))) {
                values = new Day[LENGTH_WEEKEND];
                values[0] = Day.SUNDAY;
                values[1] = Day.SATURDAY;
            } else {
                String[] texts = context.getResources()
                                        .getStringArray(R.array.days_of_the_week_short);
                String[] split = string.split(", ");
                values = new Day[split.length];
                for (int i = 0; i < split.length; i++) {
                    for (int j = 0; j < texts.length; j++) {
                        if (split[i].equalsIgnoreCase(texts[j])) {
                            values[i] = Day.valueOf(j + 1);
                            break;
                        }
                    }
                }
            }
            return values;
        }
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
        int hours = alarm.getTriggerTime().get(Calendar.HOUR_OF_DAY);
        int minutes = alarm.getTriggerTime().get(Calendar.MINUTE);
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
     * @param calendar the {@code Calendar}
     * @param field the {@code Calendar} field
     * @return the formatted time string
     */
    public static String getFormattedTimeString(Calendar calendar, int field) {
        int time = calendar.get(field);
        String formatted = String.valueOf(time);
        if (time < 10)
            formatted = "0" + formatted;
        return formatted;
    }

}
