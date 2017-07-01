package com.ukdev.smartbuzz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.Ringtone;
import com.ukdev.smartbuzz.util.ViewUtils;

/**
 * Fragment containing ringtone information
 *
 * @author Alan Camargo
 */
public class RingtoneFragment extends Fragment {

    private AppCompatSpinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.fragment_ringtone, container, attachToRoot);
        spinner = (AppCompatSpinner) view.findViewById(R.id.spinner_ringtone);
        ViewUtils.populateRingtoneSpinner(getContext(), spinner);
        return view;
    }

    /**
     * Gets the selected ringtone
     * @return the selected ringtone
     */
    public Ringtone getSelectedRingtone() {
        return (Ringtone) spinner.getSelectedItem();
    }

}
