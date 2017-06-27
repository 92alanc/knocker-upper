package com.ukdev.smartbuzz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

/**
 * A custom {@link TimePicker} where the scrolling
 * function is disabled
 *
 * @author Alan Camargo
 */
public class CustomTimePicker extends TimePicker {

    /**
     * Constructor inherited from {@link TimePicker}.
     * @see TimePicker#TimePicker(Context)
     * @param context the Android context
     */
    public CustomTimePicker(Context context) {
        super(context);
    }

    /**
     * Constructor inherited from {@link TimePicker}.
     * @see TimePicker#TimePicker(Context, AttributeSet)
     * @param context the Android context
     * @param attrs the attributes
     */
    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor inherited from {@link TimePicker}.
     * @see TimePicker#TimePicker(Context, AttributeSet, int)
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

}