package com.ukdev.smartbuzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.annotations.SnoozeDuration;
import com.ukdev.smartbuzz.listeners.OnFragmentInflatedListener;
import com.ukdev.smartbuzz.model.Time;

/**
 * Fragment for the snooze operation
 *
 * @author Alan Camargo
 */
public class SnoozeFragment extends Fragment {

    private Button button;
    private OnFragmentInflatedListener onFragmentInflatedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.fragment_snooze, container, attachToRoot);
        button = view.findViewById(R.id.btSnooze);
        if (onFragmentInflatedListener != null)
            onFragmentInflatedListener.onViewInflated(this);
        return view;
    }

    /**
     * Sets the {@code OnFragmentInflatedListener}
     * @param onFragmentInflatedListener the listener
     */
    public void setOnFragmentInflatedListener(OnFragmentInflatedListener onFragmentInflatedListener) {
        this.onFragmentInflatedListener = onFragmentInflatedListener;
    }

    /**
     * Sets the {@code OnClickListener}
     * @param onClickListener the listener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    /**
     * Sets the snooze button text based on
     * the alarm snooze duration
     * @param snoozeDuration the snooze duration
     */
    public void setButtonText(@SnoozeDuration long snoozeDuration) {
        if (snoozeDuration == Time.FIVE_MINUTES)
            button.setText(R.string.five_min);
        else if (snoozeDuration == Time.TEN_MINUTES)
            button.setText(R.string.ten_min);
        else if (snoozeDuration == Time.FIFTEEN_MINUTES)
            button.setText(R.string.fifteen_min);
        else if (snoozeDuration == Time.TWENTY_MINUTES)
            button.setText(R.string.twenty_min);
        else if (snoozeDuration == Time.TWENTY_FIVE_MINUTES)
            button.setText(R.string.twenty_five_min);
        else if (snoozeDuration == Time.THIRTY_MINUTES)
            button.setText(R.string.thirty_min);
    }

}
