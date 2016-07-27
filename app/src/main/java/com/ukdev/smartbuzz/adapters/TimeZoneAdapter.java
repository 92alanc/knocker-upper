package com.ukdev.smartbuzz.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.model.TimeZoneWrapper;

/**
 * Time zone adapter
 * Enables the rendering of custom Spinner items holding time zones
 * Created by Alan Camargo - April 2016
 */
public class TimeZoneAdapter extends ArrayAdapter<TimeZoneWrapper> implements SpinnerAdapter
{

    private Context context;
    private int layoutResourceId;
    private TimeZoneWrapper[] data = null;

    public TimeZoneAdapter(Context context, int layoutResourceId, TimeZoneWrapper[] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getRow(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getRow(position, convertView, parent);
    }

    /**
     * Gets the row at a specific position
     * @param position - int
     * @param convertView - View
     * @param parent - ViewGroup
     * @return row
     */
    private View getRow(int position, View convertView, ViewGroup parent)
    {
        TimeZoneHolder holder = new TimeZoneHolder();
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater =
                    ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder.title = (TextView)row.findViewById(R.id.timeZoneTitle);
            holder.offset = (TextView)row.findViewById(R.id.timeZoneOffset);
            row.setTag(holder);
        }
        else
            holder = (TimeZoneHolder)row.getTag();
        TimeZoneWrapper timeZone = data[position];
        holder.title.setText(timeZone.getTitle());
        String offset = timeZone.getOffset().toString();
        if (offset.contains("-"))
            offset = offset.replaceAll("-", "GMT-");
        else if (offset.equals("00:00"))
            offset = offset.replaceAll(offset, "GMT");
        else
            offset = offset.replaceAll(offset, "GMT+" + offset);
        holder.offset.setText(offset);
        return row;
    }

    /**
     * Holds all fields for the custom Spinner item
     */
    private static class TimeZoneHolder
    {
        TextView title;
        TextView offset;
    }

}
