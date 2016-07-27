package com.ukdev.smartbuzz.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ukdev.smartbuzz.database.AlarmDAO;
import com.ukdev.smartbuzz.extras.AlarmHandler;
import com.ukdev.smartbuzz.extras.FrontEndTools;
import com.ukdev.smartbuzz.extras.BackEndTools;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.R;

/**
 * Alarm adapter
 * Enables the rendering of custom ListView items holding alarms
 * Created by Alan Camargo - April 2016
 */
public class AlarmAdapter extends ArrayAdapter<Alarm>
{

    private Context context;
    private int layoutResourceId;
    private Alarm[] data = null;

    public AlarmAdapter(Context context, int layoutResourceId, Alarm[] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        AlarmHolder holder = new AlarmHolder();
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater =
                    ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder.title = (TextView)row.findViewById(R.id.titleRow);
            holder.triggerTime = (TextView)row.findViewById(R.id.triggerTimeRow);
            holder.repetition = (TextView)row.findViewById(R.id.repetitionRow);
            holder.reminderIcon = (ImageView)row.findViewById(R.id.reminderIcon);
            holder.timeZoneIcon = (ImageView)row.findViewById(R.id.timeZoneListViewIcon);
            holder.alarmSwitch = (Switch)row.findViewById(R.id.alarmSwitch);
            holder.sunMoonImg = (ImageView)row.findViewById(R.id.sunMoonImg);
            row.setTag(holder);
        }
        else
            holder = (AlarmHolder)row.getTag();
        final Alarm alarm = data[position];
        holder.title.setText(alarm.getTitle());
        holder.triggerTime.setText(alarm.getTriggerTime().toString());
        holder.repetition.setText(BackEndTools.convertIntArrayToString(context,
                alarm.getRepetition()));
        if (alarm.getTriggerTime().getHours() >= 6 &&
                alarm.getTriggerTime().getHours() < 18) // Day time
            holder.sunMoonImg.setImageResource(R.drawable.sun);
        else // Night time
            holder.sunMoonImg.setImageResource(R.drawable.moon);
        if (alarm.isReminder())
            holder.reminderIcon.setVisibility(View.VISIBLE);
        else
            holder.reminderIcon.setVisibility(View.INVISIBLE);
        if (alarm.hasDifferentTimeZone())
            holder.timeZoneIcon.setVisibility(View.VISIBLE);
        else
            holder.timeZoneIcon.setVisibility(View.INVISIBLE);
        if (alarm.isOn())
        {
            holder.alarmSwitch.setChecked(true);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        else
        {
            holder.alarmSwitch.setChecked(false);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        if (holder.alarmSwitch.isChecked())
            alarm.toggle(true);
        else
            alarm.toggle(false);
        final AlarmHolder finalHolder = holder;
        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    alarm.toggle(true);
                    AlarmHandler.scheduleAlarm(context, alarm);
                    compoundButton.setChecked(true);
                    finalHolder.title.setTextColor(ContextCompat.getColor(context,
                            R.color.green));
                }
                else
                {
                    if (alarm.isLocked())
                    {
                        FrontEndTools.showToast(context,
                                String.format(context.getString(R.string.alarm_locked),
                                        alarm.getTitle()),
                                Toast.LENGTH_LONG);
                        compoundButton.setChecked(true);
                    }
                    else
                    {
                        alarm.toggle(false);
                        AlarmHandler.cancelAlarm(context, alarm);
                        compoundButton.setChecked(false);
                        finalHolder.title.setTextColor(ContextCompat.getColor(context,
                                R.color.red));
                    }
                }
                if (!alarm.isLocked())
                {
                    AlarmDAO.update(context, alarm.getId(), alarm);
                    FrontEndTools.showNotification(context);
                }
            }
        });
        return row;
    }

    /**
     * Holds all fields for the custom ListView item
     */
    private static class AlarmHolder
    {
        TextView title;
        TextView triggerTime;
        TextView repetition;
        ImageView reminderIcon;
        ImageView timeZoneIcon;
        Switch alarmSwitch;
        ImageView sunMoonImg;
    }

}
