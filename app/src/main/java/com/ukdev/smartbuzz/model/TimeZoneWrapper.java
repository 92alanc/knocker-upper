package com.ukdev.smartbuzz.model;

import android.content.Context;
import com.ukdev.smartbuzz.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Time zone wrapper
 * Represents a time zone with its title and offset
 * Created by Alan Camargo - April 2016
 */
public class TimeZoneWrapper
{

    private String title;
    private TimeWrapper offset;

    /**
     * A time zone
     * @param title - String
     * @param offset - TimeWrapper
     */
    public TimeZoneWrapper(String title, TimeWrapper offset)
    {
        this.title = title;
        this.offset = offset;
    }

    /**
     * Gets a time zone as String
     * @return string
     */
    @Override
    public String toString()
    {
        return title + " / " + offset.toString();
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
     * Gets the offset
     * @return offset
     */
    public TimeWrapper getOffset()
    {
        return offset;
    }

    /**
     * Gets all time zones
     * @return time zones
     */
    public static TimeZoneWrapper[] getAllTimeZones(Context context)
    {
        TimeZoneWrapper[] timeZones =
                new TimeZoneWrapper[context.getResources().getStringArray(R.array.timezones).length];
        String[] titles = context.getResources().getStringArray(R.array.timezones);
        String[] offsetStrings = context.getResources().getStringArray(R.array.offsets);
        for (int i = 0; i < timeZones.length; i++)
        {
            String title = titles[i];
            TimeWrapper offset = TimeZoneWrapper.parseTimeZone(offsetStrings[i]);
            timeZones[i] = new TimeZoneWrapper(title, offset);
        }
        return timeZones;
    }

    /**
     * Gets the local time zone offset
     * @return local time zone offset
     */
    public static TimeWrapper getLocalTimeZoneOffset()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z", Locale.UK);
        String localTime = date.format(currentLocalTime);
        return TimeZoneWrapper.parseLocalTimeZoneOffset(localTime);
    }

    /**
     * Parses a time zone offset
     * @param offsetStr - String
     * @return hours and minutes format
     */
    private static TimeWrapper parseTimeZone(String offsetStr)
    {
        String[] raw = offsetStr.split("T");
        int hours = 0;
        int minutes = 0;
        if (raw.length > 1)
        {
            String time = offsetStr.split("T")[1]; // Gets everything after GMT (e.g.: -03:00)
            String[] hoursAndMinutes = time.split(":");
            if (hoursAndMinutes[0].startsWith("+"))
                hoursAndMinutes[0] = hoursAndMinutes[0].replace("+", "");
            hours = Integer.valueOf(hoursAndMinutes[0]);
            minutes = Integer.valueOf(hoursAndMinutes[1]);
        }
        return new TimeWrapper(hours, minutes);
    }

    /**
     * Parses a string representing the local time zone offset
     * E.g.: -0300 -> -03:00
     * @param timeString - String
     * @return local offset
     */
    private static TimeWrapper parseLocalTimeZoneOffset(String timeString)
    {
        String hoursString = timeString.substring(0, 3);
        String minutesString = timeString.substring(3);
        int hours = Integer.parseInt(hoursString);
        int minutes = Integer.parseInt(minutesString);
        return new TimeWrapper(hours, minutes);
    }

}
