package com.ukdev.smartbuzz.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnFragmentInflatedListener;
import com.ukdev.smartbuzz.misc.Extra;

/**
 * Fragment for the dismiss operation
 *
 * @author Alan Camargo
 */
public class DismissFragment extends Fragment {

    private boolean sleepCheckerMode;
    private Button button;
    private OnFragmentInflatedListener onFragmentInflatedListener;

    public static DismissFragment newInstance(boolean sleepCheckerMode) {
        DismissFragment instance = new DismissFragment();
        Bundle args = new Bundle();
        args.putBoolean(Extra.SLEEP_CHECKER_ON, sleepCheckerMode);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        parseArgs();
        View view = inflater.inflate(R.layout.fragment_dismiss, container, attachToRoot);
        button = view.findViewById(R.id.btDismiss);
        setButtonShape();
        if (onFragmentInflatedListener != null)
            onFragmentInflatedListener.onFragmentInflated(this);
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
     * Sets the {@code OnClickListener} to the button
     * @param onClickListener the listener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    private void parseArgs() {
        Bundle args = getArguments();
        sleepCheckerMode = args != null && args.getBoolean(Extra.SLEEP_CHECKER_ON);
    }

    private void setButtonShape() {
        try {
            if (sleepCheckerMode)
                button.setText(R.string.i_am_awake);
            else
                button.setText(R.string.dismiss);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

}
