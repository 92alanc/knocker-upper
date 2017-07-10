package com.ukdev.smartbuzz.fragments;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.ukdev.smartbuzz.R;

import java.util.Calendar;

/**
 * Fragment to select days of the week
 *
 * @author William Miranda
 */
public class TwoLinesDayOfTheWeek extends TwoLinesDefaultFragment<SparseBooleanArray> {

    private ToggleButton sundayButton;
    private ToggleButton mondayButton;
    private ToggleButton tuesdayButton;
    private ToggleButton wednesdayButton;
    private ToggleButton thursdayButton;
    private ToggleButton fridayButton;
    private ToggleButton saturdayButton;

    private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            int index = getIndexFromButton(button.getId());
            value.put(index, isChecked);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_day_of_week, container, ATTACH_TO_ROOT);
        sundayButton = (ToggleButton) view.findViewById(R.id.btSunday);
        mondayButton = (ToggleButton) view.findViewById(R.id.btMonday);
        tuesdayButton = (ToggleButton) view.findViewById(R.id.btTuesday);
        wednesdayButton = (ToggleButton) view.findViewById(R.id.btWednesday);
        thursdayButton = (ToggleButton) view.findViewById(R.id.btThursday);
        fridayButton = (ToggleButton) view.findViewById(R.id.btFriday);
        saturdayButton = (ToggleButton) view.findViewById(R.id.btSaturday);
        if (value == null)
            value = new SparseBooleanArray();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        sundayButton.setOnCheckedChangeListener(changeListener);
        mondayButton.setOnCheckedChangeListener(changeListener);
        tuesdayButton.setOnCheckedChangeListener(changeListener);
        wednesdayButton.setOnCheckedChangeListener(changeListener);
        thursdayButton.setOnCheckedChangeListener(changeListener);
        fridayButton.setOnCheckedChangeListener(changeListener);
        saturdayButton.setOnCheckedChangeListener(changeListener);
    }

    /**
     * Determines whether at least one of the
     * buttons is selected
     * @return {@code true} is positive, otherwise
     *         {@code false}
     */
    public boolean isSelected() {
        int size = value.size();
        for (int i = 0; i < size; i++) {
            if (value.valueAt(i))
                return true;
        }
        return false;
    }

    /**
     * Sets the value
     *
     * @param value the value
     */
    @Override
    public void setValue(SparseBooleanArray value) {
        value = value != null ? value : new SparseBooleanArray();
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            CompoundButton compoundButton = getButtonFromIndex(i);
            if (compoundButton != null) {
                boolean checked = value.get(i);
                compoundButton.setChecked(checked);
            }
        }
    }

    private CompoundButton getButtonFromIndex(int index) {
        switch (index) {
            case Calendar.SUNDAY:
                return sundayButton;
            case Calendar.MONDAY:
                return mondayButton;
            case Calendar.TUESDAY:
                return tuesdayButton;
            case Calendar.WEDNESDAY:
                return wednesdayButton;
            case Calendar.THURSDAY:
                return thursdayButton;
            case Calendar.FRIDAY:
                return fridayButton;
            case Calendar.SATURDAY:
                return saturdayButton;
            default:
                return null;
        }
    }

    private int getIndexFromButton(int resId) {
        switch (resId) {
            case R.id.btSunday:
                return Calendar.SUNDAY;
            case R.id.btMonday:
                return Calendar.MONDAY;
            case R.id.btTuesday:
                return Calendar.TUESDAY;
            case R.id.btWednesday:
                return Calendar.WEDNESDAY;
            case R.id.btThursday:
                return Calendar.THURSDAY;
            case R.id.btFriday:
                return Calendar.FRIDAY;
            case R.id.btSaturday:
                return Calendar.SATURDAY;
            default:
                return -1;
        }
    }

}
