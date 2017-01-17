package com.ukdev.smartbuzz.frontend;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.activities.AlarmCreatorActivity;

import java.util.Calendar;

/**
 * Time picker fragment
 * Created by Alan Camargo - January 2017
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute)
    {
        Button button = (Button) getActivity().findViewById(R.id.timePickerButton);
        String h, m;
        if (hour < 10)
            h = "0" + hour;
        else
            h = String.valueOf(hour);
        if (minute < 10)
            m = "0" + minute;
        else
            m = String.valueOf(minute);
        String text = String.format("%1$s:%2$s", h, m);
        button.setText(text);
        AlarmCreatorActivity activity = (AlarmCreatorActivity) getActivity();
        activity.setHour(hour);
        activity.setMinute(minute);
    }

}
