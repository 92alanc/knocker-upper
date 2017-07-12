package com.ukdev.smartbuzz.fragments;

import android.app.Fragment;
import android.widget.TextView;
import com.ukdev.smartbuzz.R;

/**
 * Default two-line fragment
 *
 * @author William Miranda
 */
public abstract class TwoLinesDefaultFragment<T> extends Fragment {

    static final boolean ATTACH_TO_ROOT = false;

    public interface TwoLinesChangeListener<T> {
        void onChange(T newValue);
    }

    TextView textSummary;
    protected T value;
    protected String title;
    String summary;
    TwoLinesChangeListener<T> changeListener;

    @Override
    public void onStart() {
        super.onStart();
        TextView textTitle = (TextView) getView().findViewById(R.id.title);
        textSummary = (TextView) getView().findViewById(R.id.summary);
        textTitle.setText(title);
        textSummary.setText(summary);
        if (value != null)
            setValue(value);
    }

    /**
     * Sets the value
     * @param value the value
     */
    public abstract void setValue(T value);

    /**
     * Gets the value
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the title
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the summary
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Sets the change listener
     * @param changeListener the change listener
     */
    public void setChangeListener(TwoLinesChangeListener<T> changeListener) {
        this.changeListener = changeListener;
    }

}
