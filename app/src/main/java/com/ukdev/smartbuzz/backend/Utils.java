package com.ukdev.smartbuzz.backend;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Process;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.enums.Day;

import java.util.Calendar;

public class Utils {

    public static String convertDayArrayToString(Context context, Day[] array) {
        if (array == null)
            return null;
        else {
            if (array.length == 7)
                return context.getResources().getString(R.string.every_day);
            else {
                boolean weekDays = true;
                boolean weekends = true;
                for (Day day : array) {
                    if (array.length != 2 || (day != Day.SUNDAY && day != Day.SATURDAY))
                        weekends = false;
                    if (array.length != 5 || (day != Day.MONDAY
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
                            .getStringArray(R.array.daysOfTheWeek);
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

    public static Day[] convertStringToDayArray(Context context, String string) {
        if (string == null)
            return null;
        else {
            Day[] values;
            if (string.equals(context.getResources().getString(R.string.every_day))) {
                values = new Day[7];
                for (int i = 0; i < values.length; i++)
                    values[i] = Day.fromInt(i + 1);
            } else if (string.equals(context.getResources().getString(R.string.week_days))) {
                values = new Day[5];
                for (int i = 0; i < values.length; i++)
                    values[i] = Day.fromInt(i + 2);
            } else if (string.equals(context.getResources().getString(R.string.weekends))) {
                values = new Day[2];
                values[0] = Day.SUNDAY;
                values[1] = Day.SATURDAY;
            } else {
                String[] texts = context.getResources()
                        .getStringArray(R.array.daysOfTheWeek);
                String[] split = string.split(", ");
                values = new Day[split.length];
                for (int i = 0; i < split.length; i++) {
                    for (int j = 0; j < texts.length; j++) {
                        if (split[i].equalsIgnoreCase(texts[j])) {
                            values[i] = Day.fromInt(j + 1);
                            break;
                        }
                    }
                }
            }
            return values;
        }
    }

    public static long getNextValidTriggerTime(Alarm alarm) {
        int hours = alarm.getTriggerTime().get(Calendar.HOUR_OF_DAY);
        int minutes = alarm.getTriggerTime().get(Calendar.MINUTE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar now = Calendar.getInstance();

        if ((hours <= now.get(Calendar.HOUR_OF_DAY)
                || minutes > now.get(Calendar.MINUTE))
                && (hours < now.get(Calendar.HOUR_OF_DAY)
                || minutes <= now.get(Calendar.MINUTE))) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTimeInMillis();
    }

    public static int getDefaultVolume(Context context) {
        return getMaxVolume(context) / 2;
    }

    public static int getMaxVolume(Context context) {
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    public static void killApp(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

}
