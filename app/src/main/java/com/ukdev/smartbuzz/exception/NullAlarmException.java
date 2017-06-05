package com.ukdev.smartbuzz.exception;

import android.content.Context;
import com.ukdev.smartbuzz.R;

/**
 * Exception thrown when an alarm is {@code null}
 *
 * @author Alan Camargo
 */
public class NullAlarmException extends Exception {

    /**
     * Default constructor for {@code NullAlarmException}
     * @param context the Android context
     */
    public NullAlarmException(Context context) {
        super(context.getString(R.string.exception_null_alarm));
    }

}
