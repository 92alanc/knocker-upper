package com.ukdev.smartbuzz.model;

import java.util.Calendar;

/**
 * A time wrapper
 *
 * @author Alan Camargo
 */
public class Time {

    private int hour;
    private int minute;

    /**
     * Creates an instance of {@code Time}
     * with the current time
     */
    public Time() {
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minute = Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * Creates an instance of {@code Time}
     * @param hour the hour
     * @param minute the minute
     */
    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Converts the time to a {@code Calendar}
     * @return the time as a {@code Calendar}
     */
    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    @Override
    public String toString() {
        String hour = this.hour < 10 ? "0" + this.hour : "" + this.hour;
        String minute = this.minute < 10 ? "0" + this.minute : "" + this.minute;
        return String.format("%1$s:%2$s", hour, minute);
    }

    /**
     * Gets the hour
     * @return the hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets the minute
     * @return the minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Gets the {@code Time} value of a number
     * of milliseconds
     * @param millis the milliseconds
     * @return the {@code Time} value
     */
    public static Time valueOf(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new Time(hour, minute);
    }

}
