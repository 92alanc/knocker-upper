package com.ukdev.smartbuzz.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.enums.Day;

/**
 * Repetition adapter
 *
 * @author Alan Camargo
 */
public class RepetitionAdapter extends ArrayAdapter<Day> implements SpinnerAdapter {

    private Context context;
    private Day[] repetition;

    /**
     * Default constructor for {@code RepetitionAdapter}
     * @param context the Android context
     * @param repetition the repetition
     */
    public RepetitionAdapter(Context context, Day[] repetition) {
        super(context, R.id.spinner_repetition, repetition);
        this.context = context;
        this.repetition = repetition;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getCount() {
        return repetition.length;
    }

    @Override
    public Day getItem(int i) {
        return repetition[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return repetition == null || repetition.length == 0;
    }

    @Override
    public int getItemViewType(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getViewTypeCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getItemId(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasStableIds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        throw new UnsupportedOperationException();
    }

}
