package com.ukdev.smartbuzz.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

/**
 * A custom {@code TimePicker} where the scrolling
 * function is disabled
 *
 * @author Alan Camargo
 */
public class CustomTimePicker extends TimePicker {

    /**
     * Constructor inherited from {@code TimePicker}
     * @param context the Android context
     */
    public CustomTimePicker(Context context) {
        super(context);
    }

    /**
     * Constructor inherited from {@code TimePicker}
     * @param context the Android context
     * @param attrs the attributes
     */
    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor inherited from {@code TimePicker}
     * @param context the Android context
     * @param attrs the attributes
     * @param defStyleAttr the default style attribute
     */
    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    /**
     * Gets the hour
     */
    @Override
    public int getHour() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return super.getHour();
        else
            return super.getCurrentHour();
    }

    /**
     * Gets the minute
     */
    @Override
    public int getMinute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return super.getMinute();
        else
            return super.getCurrentMinute();
    }

    /**
     * Sets the hour
     * @param hour the hour
     */
    @Override
    public void setHour(int hour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setHour(hour);
        else
            super.setCurrentHour(hour);
    }

    /**
     * Sets the minute
     * @param minute the minute
     */
    @Override
    public void setMinute(int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setMinute(minute);
        else
            super.setCurrentMinute(minute);
    }

}