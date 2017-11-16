package com.ukdev.smartbuzz.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnFragmentInflatedListener;

/**
 * Fragment for the dismiss operation
 *
 * @author Alan Camargo
 */
public class DismissFragment extends Fragment {

    private ImageButton button;
    private OnFragmentInflatedListener onFragmentInflatedListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.fragment_dismiss, container, attachToRoot);
        button = view.findViewById(R.id.btDismiss);
        if (onFragmentInflatedListener != null)
            onFragmentInflatedListener.onFragmentInflated(this);
        return view;
    }

    /**
     * Sets the {@code OnFragmentInflatedListener}
     *
     * @param onFragmentInflatedListener the listener
     */
    public void setOnFragmentInflatedListener(
            OnFragmentInflatedListener onFragmentInflatedListener) {
        this.onFragmentInflatedListener = onFragmentInflatedListener;
    }

    /**
     * Sets the {@code OnClickListener} to the button
     *
     * @param onClickListener the listener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

}
