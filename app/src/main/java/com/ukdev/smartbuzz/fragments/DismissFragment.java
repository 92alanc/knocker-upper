package com.ukdev.smartbuzz.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnViewInflatedListener;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.misc.LogTool;

/**
 * Fragment for the dismiss operation
 *
 * @author Alan Camargo
 */
public class DismissFragment extends Fragment {

    private boolean sleepCheckerMode;
    private Button button;
    private OnViewInflatedListener onViewInflatedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        Bundle args = getArguments();
        sleepCheckerMode = args != null && args.getBoolean(IntentExtra.SLEEP_CHECKER_ON.toString());
        View view = inflater.inflate(R.layout.fragment_dismiss, container, attachToRoot);
        button = view.findViewById(R.id.btDismiss);
        setButtonShape();
        if (onViewInflatedListener != null)
            onViewInflatedListener.onViewInflated(this);
        return view;
    }

    /**
     * Sets the {@code OnViewInflatedListener}
     * @param onViewInflatedListener the listener
     */
    public void setOnViewInflatedListener(OnViewInflatedListener onViewInflatedListener) {
        this.onViewInflatedListener = onViewInflatedListener;
    }

    /**
     * Sets the {@code OnClickListener} to the button
     * @param onClickListener the listener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    private void setButtonShape() {
        try {
            if (sleepCheckerMode)
                button.setText(R.string.i_am_awake);
            else
                button.setText(R.string.dismiss);
        } catch (Resources.NotFoundException e) {
            LogTool logTool = new LogTool(getContext());
            logTool.exception(e);
        }
    }

}
