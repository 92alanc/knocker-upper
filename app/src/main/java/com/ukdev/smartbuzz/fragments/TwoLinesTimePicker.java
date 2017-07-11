package com.ukdev.smartbuzz.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.Time;

import java.util.Calendar;

/**
 * Fragment with a {@code TimePicker}
 *
 * @author William Miranda
 */
public class TwoLinesTimePicker extends TwoLinesDefaultFragment<Time> {

    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_default, container, ATTACH_TO_ROOT);
        rootView = (ViewGroup) view.findViewById(R.id.rootView);
        value = new Time();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogue(view.getContext());
            }
        });
    }

    /**
     * Sets the value
     *
     * @param value the value
     */
    @Override
    public void setValue(Time value) {
        this.value = value;
        if (textSummary != null)
            textSummary.setText(value.toString());
    }

    private void showDialogue(Context context) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean use24HourClock = DateFormat.is24HourFormat(context);
        TimePickerDialog timePickerDialogue = new TimePickerDialog(context, listener, hour, minute, use24HourClock);
        timePickerDialogue.setTitle(title);
        timePickerDialogue.show();
    }

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            setValue(new Time(hour, minute));
        }
    };

}
