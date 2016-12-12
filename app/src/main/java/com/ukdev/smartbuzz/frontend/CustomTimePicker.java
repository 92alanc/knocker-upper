package com.ukdev.smartbuzz.frontend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

/**
 * Custom time picker
 * Not scrollable
 * Created by Alan Camargo - April 2016
 */
public class CustomTimePicker extends TimePicker
{

    public CustomTimePicker(Context context)
    {
        super(context);
    }

    public CustomTimePicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Prevents TimePicker from being scrolled
     * @param ev - MotionEvent
     * @return on intercept
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

}
