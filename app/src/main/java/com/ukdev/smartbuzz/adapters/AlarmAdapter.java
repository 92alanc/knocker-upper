package com.ukdev.smartbuzz.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.util.Utils;
import com.ukdev.smartbuzz.listeners.RecyclerViewClickListener;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.Calendar;
import java.util.List;

/**
 * Alarm adapter
 *
 * @author Alan Camargo
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {

    private Context context;
    private List<Alarm> objects;
    private RecyclerViewClickListener listener;

    /**
     * Default constructor for {@code AlarmAdapter}
     * @param context the Android context
     * @param objects the objects
     * @param listener the click listener
     */
    public AlarmAdapter(Context context, List<Alarm> objects, RecyclerViewClickListener listener) {
        this.context = context;
        this.objects = objects;
        this.listener = listener;
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

        int src = getImageViewSrc(alarm.getTriggerTime());
        holder.dayNightImageView.setImageResource(src);

        holder.alarmTitleTextView.setText(alarm.getTitle());

        String hour = Utils.getFormattedTimeString(alarm.getTriggerTime(), Calendar.HOUR_OF_DAY);
        String minute = Utils.getFormattedTimeString(alarm.getTriggerTime(), Calendar.MINUTE);
        String triggerTime = String.format("%1$s:%2$s", hour, minute);
        holder.triggerTimeTextView.setText(triggerTime);

        String repetition = Utils.convertDayArrayToString(context, alarm.getRepetition());
        holder.repetitionTextView.setText(repetition);

        holder.alarmSwitch.setChecked(alarm.isActive());
        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setActive(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private int getImageViewSrc(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour <= 18)
            return R.mipmap.ic_brightness_7_white_24dp;
        else
            return R.mipmap.ic_brightness_3_white_24dp;
    }

    /**
     * The alarm holder
     *
     * @author Alan Camargo
     */
    static class AlarmHolder extends RecyclerView.ViewHolder implements RecyclerViewClickListener {

        ImageView dayNightImageView;
        TextView alarmTitleTextView;
        TextView triggerTimeTextView;
        TextView repetitionTextView;
        SwitchCompat alarmSwitch;
        RecyclerViewClickListener listener;

        /**
         * The alarm holder
         * @param itemView the item view
         * @param listener the click listener
         */
        AlarmHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            this.listener = listener;
            dayNightImageView = (ImageView) itemView.findViewById(R.id.image_view_day_night);
            alarmTitleTextView = (TextView) itemView.findViewById(R.id.text_view_alarm_title_card);
            triggerTimeTextView = (TextView) itemView.findViewById(R.id.text_view_trigger_time);
            repetitionTextView = (TextView) itemView.findViewById(R.id.text_view_repetition);
            alarmSwitch = (SwitchCompat) itemView.findViewById(R.id.switch_alarm);
        }

        /**
         * Method called when a recycler view item is clicked
         * @param view     the clicked view
         * @param position the clicked position
         */
        @Override
        public void onItemClick(View view, int position) {
            listener.onItemClick(view, getLayoutPosition());
        }

    }

}
