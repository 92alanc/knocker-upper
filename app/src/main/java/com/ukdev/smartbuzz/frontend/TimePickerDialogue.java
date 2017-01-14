package com.ukdev.smartbuzz.frontend;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

/**
 * Custom time picker dialogue
 * Created by Alan Camargo - January 2017
 */

public class TimePickerDialogue extends TimePickerDialog
{

    private static int hourOfDay, minute;

    public TimePickerDialogue(Context context, final int hourOfDay, final int minute,
                              boolean is24HourView)
    {
        super(context, new OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {
                TimePickerDialogue.hourOfDay = i;
                TimePickerDialogue.minute = i1;
            }
        }, hourOfDay, minute, is24HourView);
        TimePickerDialogue.hourOfDay = hourOfDay;
        TimePickerDialogue.minute = minute;
    }

    /**
     * Gets the hour
     * @return hour
     */
    public int getHourOfDay()
    {
        return hourOfDay;
    }

    /**
     * Gets the minute
     * @return minute
     */
    public int getMinute()
    {
        return minute;
    }

}
