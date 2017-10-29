package com.ukdev.smartbuzz.adapters;

import android.content.Context;
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

        holder.alarmTitleTextView.setText(alarm.getTitle());

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

    private int getImageViewSrc(int hour) {
        if (hour >= 6 && hour <= 18)
            return R.drawable.ic_day;
        else
            return R.drawable.ic_night;
    }

}
