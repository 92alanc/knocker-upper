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
import com.ukdev.smartbuzz.model.RingtoneWrapper;

import java.util.ArrayList;

/**
 * Ringtone adapter
 * Enables the rendering of custom Spinner items holding ringtones
 * Created by Alan Camargo - May 2016
 */
public class RingtoneAdapter extends ArrayAdapter<RingtoneWrapper>
        implements SpinnerAdapter
{

    private Context context;
    private int layoutResourceId;
    private ArrayList<RingtoneWrapper> data = null;

    public RingtoneAdapter(Context context, int layoutResourceId,
                           ArrayList<RingtoneWrapper> data)
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
     * @param position    - int
     * @param convertView - View
     * @param parent      - ViewGroup
     * @return row
     */
    private View getRow(int position, View convertView, ViewGroup parent)
    {
        RingtoneHolder holder = new RingtoneHolder();
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater =
                    ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder.uri = (TextView) row.findViewById(R.id.ringtoneUri);
            holder.title = (TextView) row.findViewById(R.id.ringtoneTitle);
            row.setTag(holder);
        } else {
            holder = (RingtoneHolder) row.getTag();
        }
        RingtoneWrapper ringtone = data.get(position);
        holder.uri.setText(ringtone.getUri().toString());
        String title = ringtone.getTitle();
        holder.title.setText(title);
        return row;
    }

    /**
     * Holds all fields for the custom Spinner item
     */
    private static class RingtoneHolder
    {
        TextView uri;
        TextView title;
    }

}
