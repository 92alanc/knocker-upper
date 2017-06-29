package com.ukdev.smartbuzz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnFragmentAttachListener;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

/**
 * Fragment containing snooze duration information
 *
 * @author Alan Camargo
 */
public class SnoozeDurationFragment extends Fragment {

    private AppCompatSpinner spinner;
    private Context context;
    private OnFragmentAttachListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        return inflater.inflate(R.layout.fragment_snooze_duration, container, attachToRoot);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentAttachListener)
            listener = (OnFragmentAttachListener) context;
        else {
            String message = String.format("%s must implement the OnFragmentAttachedListener interface.",
                                           context.toString());
            throw new RuntimeException(message);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * Gets the selected snooze duration
     * @return the selected snooze duration
     */
    public SnoozeDuration getSelectedSnoozeDuration() {
        return (SnoozeDuration) spinner.getSelectedItem();
    }

}
