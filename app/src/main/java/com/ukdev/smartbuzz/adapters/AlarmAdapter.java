package com.ukdev.smartbuzz.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.listeners.OnItemClickListener;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.util.AlarmHandler;
import com.ukdev.smartbuzz.util.PreferenceUtils;
import com.ukdev.smartbuzz.util.Utils;

import java.util.List;

/**
 * Alarm adapter
 *
 * @author Alan Camargo
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {

    private final AlarmDao dao;
    private final Context context;
    private final List<Alarm> objects;
    private final OnItemClickListener listener;

    /**
     * Default constructor for {@code AlarmAdapter}
     * @param context the Android context
     * @param objects the objects
     * @param listener the onItemClickListener
     */
    public AlarmAdapter(Context context, List<Alarm> objects, OnItemClickListener listener) {
        this.context = context;
        this.objects = objects;
        this.listener = listener;
        dao = AlarmDao.getInstance(context);
    }

    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean attachToRoot = false;
        View view = inflater.inflate(R.layout.card_alarm, parent, attachToRoot);
        return new AlarmHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int position) {
        final Alarm alarm = objects.get(position);

        Time time = Time.valueOf(alarm.getTriggerTime());
        int src = getImageViewSrc(time.getHour());
        holder.dayNightImageView.setImageResource(src);
        int contentDescription = getImageViewContentDescription(time.getHour());
        holder.dayNightImageView.setContentDescription(context.getString(contentDescription));

        holder.alarmNameTextView.setText(alarm.getName());

        String triggerTime = time.toString();
        holder.triggerTimeTextView.setText(triggerTime);

        String repetition = Utils.convertIntArrayToString(context, alarm.getRepetition());
        holder.repetitionTextView.setText(repetition);

        holder.alarmSwitch.setChecked(alarm.isActive());
        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setActive(isChecked);
                dao.update(alarm);
                AlarmHandler alarmHandler = new AlarmHandler(context, alarm);
                if (isChecked)
                    alarmHandler.setAlarm();
                else
                    alarmHandler.cancelAlarm();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @DrawableRes
    private int getImageViewSrc(int hour) {
        SharedPreferences preferences = context.getSharedPreferences(PreferenceUtils.FILE_NAME,
                                                                     Context.MODE_PRIVATE);
        PreferenceUtils preferenceUtils = new PreferenceUtils((Activity) context, preferences);
        if (hour >= 6 && hour <= 18) {
            if (preferenceUtils.getTheme() == PreferenceUtils.Theme.DARK)
                return R.drawable.ic_day;
            else
                return R.drawable.ic_day_light;
        } else {
            if (preferenceUtils.getTheme() == PreferenceUtils.Theme.DARK)
                return R.drawable.ic_night;
            else
                return R.drawable.ic_night_light;
        }
    }

    @StringRes
    private int getImageViewContentDescription(int hour) {
        if (hour >= 6 && hour <= 18)
            return R.string.description_daytime;
        else
            return R.string.description_nighttime;
    }

}
