package com.ukdev.smartbuzz.fragments;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
        button = (Button) view.findViewById(R.id.btDismiss);
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
            if (sleepCheckerMode) {
                button.setBackground(getShape(R.drawable.shape_dismiss_sleep_checker));
                button.setText(R.string.i_am_awake);
            } else {
                button.setBackground(getShape(R.drawable.shape_dismiss_alarm));
                button.setText(R.string.dismiss);
            }
        } catch (Resources.NotFoundException e) {
            LogTool logTool = new LogTool(getContext());
            logTool.exception(e);
        }
    }

    @SuppressWarnings("deprecation")
    private Drawable getShape(int shapeId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getResources().getDrawable(shapeId, getActivity().getTheme());
        else
            return getResources().getDrawable(shapeId);
    }

}
