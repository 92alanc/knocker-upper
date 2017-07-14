package com.ukdev.smartbuzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnViewInflatedListener;

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
        button = (Button) view.findViewById(R.id.btSnooze);
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

}
