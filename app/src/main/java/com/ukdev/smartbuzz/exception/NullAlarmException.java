package com.ukdev.smartbuzz.exception;

import android.content.Context;
import com.ukdev.smartbuzz.R;

public class NullAlarmException extends Exception {

    public NullAlarmException(Context context) {
        super(context.getString(R.string.exception_null_alarm));
    }

}
