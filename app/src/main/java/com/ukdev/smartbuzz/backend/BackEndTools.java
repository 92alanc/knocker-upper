package com.ukdev.smartbuzz.backend;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Process;
import android.widget.GridLayout;
import android.widget.ToggleButton;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.frontend.FrontEndTools;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Back end tools class
 * Provides useful tools to back end operations
 * Created by Alan Camargo - April 2016
 */
public class BackEndTools
{

    /**
     * Converts an int array to a comma-separated String like "1,2,3"
     * @param array - int[]
     * @return comma-separated String
     */
    public static String convertIntArrayToString(Context context, int[] array)
    {
        if (array == null)
            return null;
        else
        {
            if (array.length == 7)
                return context.getResources().getString(R.string.every_day);
            else
            {
                boolean weekDays = true;
                boolean weekends = true;
                for (int item : array)
                {
                    if (array.length != 2 || (item != 1 && item != 7))
                        weekends = false;
                    if (array.length != 5 || (item != 2 && item != 3 && item != 4
                                              && item != 5 && item != 6))
                        weekDays = false;
                }
                if (weekDays)
                    return context.getResources().getString(R.string.week_days);
                else if (weekends)
                    return context.getResources().getString(R.string.weekends);
                else
                {
                    String[] texts = context.getResources()
                                            .getStringArray(R.array.daysOfTheWeek);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < array.length; i++)
                    {
                        for (int j = 0; j < texts.length; j++)
                        {
                            if (j == (array[i] - 1))
                            {
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
     * Converts a comma-separated String to an int array
     * @param context - Context
     * @param str - String
     * @return int array
     */
    public static int[] convertStringToIntArray(Context context, String str)
    {
        if (str == null)
            return null;
        else
        {
            int[] values;
            if (str.equals(context.getResources().getString(R.string.every_day)))
            {
                values = new int[7];
                for (int i = 0; i < values.length; i++)
                    values[i] = i + 1;
            }
            else if (str.equals(context.getResources().getString(R.string.week_days)))
            {
                values = new int[5];
                for (int i = 0; i < values.length; i++)
                    values[i] = i + 2;
            }
            else if (str.equals(context.getResources().getString(R.string.weekends)))
            {
                values = new int[2];
                values[0] = 1;
                values[1] = 7;
            }
            else
            {
                String[] texts = context.getResources()
                                        .getStringArray(R.array.daysOfTheWeek);
                String[] split = str.split(", ");
                values = new int[split.length];
                for (int i = 0; i < split.length; i++)
                {
                    for (int j = 0; j < texts.length; j++)
                    {
                        if (split[i].equalsIgnoreCase(texts[j]))
                        {
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
     * Gets all selected repetition button values
     * @param layout - GridLayout
     * @return values
     */
    public static int[] getSelectedRepetition(GridLayout layout)
    {
        if (FrontEndTools.getToggleButtons(layout) != null)
        {
            ToggleButton[] buttons = FrontEndTools.getToggleButtons(layout);
            List<Integer> list = new ArrayList<>();
            boolean noButtonsChecked = true;
            assert buttons != null;
            for (int i = 0; i < buttons.length; i++)
            {
                if (buttons[i].isChecked())
                {
                    noButtonsChecked = false;
                    list.add(i + 1);
                }
            }
            if (noButtonsChecked)
                return null;
            else
            {
                int[] days = new int[list.size()];
                for (int i = 0; i < days.length; i++)
                    days[i] = list.get(i);
                return days;
            }
        }
        else
            return null;
    }

    /**
     * Gets the maximum volume available on the device
     * @param manager - AudioManager
     * @return max volume
     */
    public static int getMaxVolume(AudioManager manager)
    {
        return manager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    /**
     * Gets the next valid trigger time, in milliseconds
     * @param alarm - Alarm
     * @return next valid trigger time
     */
    static long getNextValidTriggerTime(Alarm alarm)
    {
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
                || minutes <= now.get(Calendar.MINUTE)))
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * Shows the time left to an alarm's trigger time
     * @param alarm - Alarm
     * @return an array containing the days, hours and minutes left
     */
    public static int[] getTimeLeftToTrigger(Alarm alarm)
    {
        int[] timeLeft = new int[3];
        int[] repetition = alarm.getRepetition();
        Calendar now = Calendar.getInstance();
        int daysDiff, hoursDiff, minutesDiff;
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.setTimeInMillis(getNextValidTriggerTime(alarm));
        hoursDiff = triggerTime.get(Calendar.HOUR_OF_DAY) - now.get(Calendar.HOUR_OF_DAY);
        minutesDiff = triggerTime.get(Calendar.MINUTE) - now.get(Calendar.MINUTE);
        if (minutesDiff < 0)
        {
            hoursDiff--;
            minutesDiff = 60 + minutesDiff;
        }
        if (hoursDiff < 0)
            hoursDiff = 24 + hoursDiff;
        if (alarm.repeats())
        {
            int nextDay = 0;
            int today = now.get(Calendar.DAY_OF_WEEK);
            int tomorrow;
            if (today != 7)
                tomorrow = today + 1;
            else
                tomorrow = 1;
            if ((repetition[0] == today)
                && (now.getTimeInMillis() < triggerTime.getTimeInMillis()))
                nextDay = repetition[0];
            else
            {
                for (int day : repetition)
                {
                    nextDay = day;
                    if (day == today || day == tomorrow)
                    {
                        if ((day == today) && ((triggerTime.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY))
                                               || (triggerTime.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY))
                                                  && triggerTime.get(Calendar.MINUTE) <= now.get(Calendar.MINUTE)))
                            continue;
                        else
                            break;
                    }
                    if (day > tomorrow)
                    {
                        nextDay = day;
                        break;
                    }
                    nextDay = repetition[0];
                }
            }
            daysDiff = nextDay - now.get(Calendar.DAY_OF_WEEK);
            if ((daysDiff == 1) && (triggerTime.get(Calendar.HOUR_OF_DAY) <= now.get(Calendar.HOUR_OF_DAY))
                && (triggerTime.get(Calendar.MINUTE) <= now.get(Calendar.MINUTE)))
                daysDiff = 0;
            else if (((daysDiff == 0) && (nextDay != today) && (nextDay != tomorrow)))
                daysDiff = 7;
            else if (((daysDiff == 0) && (repetition.length == 1)) && ((triggerTime.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY))
                                                                       || (triggerTime.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY))
                                                                          && triggerTime.get(Calendar.MINUTE) <= now.get(Calendar.MINUTE)))
                daysDiff = 6;
            else if (daysDiff < 0)
                daysDiff = 7 + daysDiff;
        }
        else
            daysDiff = 0;
        timeLeft[0] = daysDiff;
        timeLeft[1] = hoursDiff;
        timeLeft[2] = minutesDiff;
        return timeLeft;
    }

    /**
     * Kills the app
     * @param activity - Activity
     */
    public static void killApp(Activity activity)
    {
        activity.moveTaskToBack(true);
        activity.finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

}