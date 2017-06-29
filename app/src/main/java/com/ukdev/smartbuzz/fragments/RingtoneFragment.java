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
import com.ukdev.smartbuzz.model.Ringtone;
import com.ukdev.smartbuzz.util.ViewUtils;

/**
 * Fragment containing ringtone information
 *
 * @author Alan Camargo
 */
public class RingtoneFragment extends Fragment {

    private AppCompatSpinner spinner;
    private Context context;
    private OnFragmentAttachListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.fragment_ringtone, container, attachToRoot);
        listener.onLoadFragment();
        spinner = (AppCompatSpinner) view.findViewById(R.id.spinner_ringtone);
        ViewUtils.populateRingtoneSpinner(context, spinner);
        listener.onAttachFragment();
        return view;
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
     * Gets the selected ringtone
     * @return the selected ringtone
     */
    public Ringtone getSelectedRingtone() {
        return (Ringtone) spinner.getSelectedItem();
    }

}
