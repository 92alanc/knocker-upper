package com.ukdev.smartbuzz.frontend;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.ukdev.smartbuzz.R;

/**
 * Snooze adapter
 * Enables the rendering of custom Spinner items holding snooze durations
 * Created by Alan Camargo - June 2016
 */
class SnoozeAdapter extends ArrayAdapter<String> implements SpinnerAdapter
{

    private Context context;
    private int layoutResourceId;
    private String[] data;

    SnoozeAdapter(Context context, String[] data)
    {
        super(context, R.layout.snooze_spinner_item, data);
        this.context = context;
        this.layoutResourceId = R.layout.snooze_spinner_item;
        this.data = data;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
    {
        return getRow(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        return getRow(position, convertView, parent);
    }

    /**
     * Gets the row at a specific position
     *
     * @param position    - int
     * @param convertView - View
     * @param parent      - ViewGroup
     * @return row
     */
    private View getRow(int position, View convertView, ViewGroup parent)
    {
        SnoozeHolder holder = new SnoozeHolder();
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder.text = (TextView) row.findViewById(R.id.snoozeSpinnerText);
            row.setTag(holder);
        }
        else
            holder = (SnoozeHolder) row.getTag();
        String text = data[position];
        holder.text.setText(text);
        return row;
    }

    /**
     * Holds all fields for the custom Spinner item
     */
    private static class SnoozeHolder
    {
        TextView text;
    }

}
