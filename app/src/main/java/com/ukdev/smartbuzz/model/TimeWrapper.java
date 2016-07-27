package com.ukdev.smartbuzz.model;

import com.ukdev.smartbuzz.extras.AppConstants;

import java.util.Calendar;

/**
 * Time wrapper
 * Represents a time in hours and minutes
 * Created by Alan Camargo - April 2016
 */
public class TimeWrapper
{

    private int hours;
    private int minutes;

    /**
     * An instance of time
     * @param hours - int
     * @param minutes - int
     */
    public TimeWrapper(int hours, int minutes)
    {
        this.hours = hours;
        this.minutes = minutes;
    }

    /**
     * Gets the hours
     * @return hours
     */
    public int getHours() { return hours; }

    /**
     * Gets the minutes
     * @return minutes
     */
    public int getMinutes() { return minutes; }

    /**
     * Gets a time as String
     * Example: Hours:Minutes (24h format)
     * @return time as String
     */
    @Override
    public String toString()
    {
        String h = String.valueOf(hours);
        String m = String.valueOf(minutes);
        if (hours < 0)
        {
            int absolute = Math.abs(Integer.parseInt(h));
            if (hours > -10)
                h = "-0" + absolute;
            else
                h = "-" + absolute;
        }
        else
        if (hours < 10)
            h = "0" + h;
        if (minutes < 10)
            m = "0" + m;
        return h + ":" + m;
    }

    /**
     * Gets the next valid trigger time
     * Compares a given hour and minute with current time
     * If the given time is ahead of current time, it will
     * return the given time
     * Otherwise, it will return the given time + 1 day
     * @param alarm - Alarm
     * @return next trigger
     */
    public static long getNextValidTrigger(Alarm alarm)
    {
        int hours, minutes;
        if (alarm.hasDifferentTimeZone())
        {
            TimeWrapper localOffset = TimeZoneWrapper.getLocalTimeZoneOffset();
            int hoursDiff = -1 * (alarm.getTimeZone().getOffset().getHours() - localOffset.getHours());
            int minutesDiff = -1 * (alarm.getTimeZone().getOffset().getMinutes() - localOffset.getMinutes());
            hours = alarm.getTriggerTime().getHours() + hoursDiff;
            minutes = alarm.getTriggerTime().getMinutes() + minutesDiff;
        }
        else
        {
            hours = alarm.getTriggerTime().getHours();
            minutes = alarm.getTriggerTime().getMinutes();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar now = Calendar.getInstance();
        final long TRIGGER_TIME = calendar.getTimeInMillis();

        if ((hours > now.get(Calendar.HOUR_OF_DAY)
                && minutes <= now.get(Calendar.MINUTE))
                || (hours >= now.get(Calendar.HOUR_OF_DAY)
                && minutes > now.get(Calendar.MINUTE)))
            return TRIGGER_TIME;
        else
            return TRIGGER_TIME + AppConstants.ONE_DAY;
    }

}
