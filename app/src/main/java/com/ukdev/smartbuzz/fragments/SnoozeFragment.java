package com.ukdev.smartbuzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnViewInflatedListener;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;

/**
 * Fragment for the snooze operation
 *
 * @author Alan Camargo
 */
public class SnoozeFragment extends Fragment {

    private Button button;
    private OnViewInflatedListener onViewInflatedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.fragment_snooze, container, attachToRoot);
        button = view.findViewById(R.id.btSnooze);
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
    public void setButtonText(SnoozeDuration snoozeDuration) {
        StringBuilder sb = new StringBuilder("+");
        switch (snoozeDuration) {
            case FIVE_MINUTES:
                sb.append("5 min");
                break;
            case TEN_MINUTES:
                sb.append("10 min");
                break;
            case FIFTEEN_MINUTES:
                sb.append("15 min");
                break;
            case TWENTY_MINUTES:
                sb.append("20 min");
                break;
            case TWENTY_FIVE_MINUTES:
                sb.append("25 min");
                break;
            case THIRTY_MINUTES:
                sb.append("30 min");
                break;
            default:
                return;
        }
        button.setText(sb.toString());
    }

}
