package com.ukdev.smartbuzz.model;

import java.util.Calendar;

/**
 * A time wrapper
 *
 * @author Alan Camargo
 */
public class Time {

    public static final long ZERO = 0L;
    public static final long ONE_SECOND = 1000;
    public static final long TWO_SECONDS = 2000;
    public static final long FIFTEEN_SECONDS = 15000;
    public static final long ONE_MINUTE = 60000;
    public static final int TWO_MINUTES = 120000;
    public static final int THREE_MINUTES = 180000;
    public static final long FIVE_MINUTES = 300000L;
    public static final long TEN_MINUTES = 600000L;
    public static final long FIFTEEN_MINUTES = 900000L;
    public static final long TWENTY_MINUTES = 1200000L;
    public static final long TWENTY_FIVE_MINUTES = 1500000L;
    public static final long THIRTY_MINUTES = 1800000L;

    private final int hour;
    private final int minute;

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
