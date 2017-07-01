package com.ukdev.smartbuzz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;
import com.ukdev.smartbuzz.util.ViewUtils;

/**
 * Fragment containing snooze duration information
 *
 * @author Alan Camargo
 */
public class SnoozeDurationFragment extends Fragment {

    private AppCompatSpinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.fragment_snooze_duration, container, attachToRoot);
        spinner = (AppCompatSpinner) view.findViewById(R.id.spinner_snooze_duration);
        ViewUtils.populateSnoozeDurationSpinner(getContext(), spinner);
        return view;
    }

    /**
     * Gets the selected snooze duration
     * @return the selected snooze duration
     */
    public SnoozeDuration getSelectedSnoozeDuration() {
        int position = spinner.getSelectedItemPosition();
        SnoozeDuration[] snoozeDurations = SnoozeDuration.values();
        return snoozeDurations[position];
    }

}
