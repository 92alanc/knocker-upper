package com.ukdev.smartbuzz.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnItemClickListener;

/**
 * The alarm holder
 *
 * @author Alan Camargo
 */
public class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView dayNightImageView;
    final TextView alarmTitleTextView;
    final TextView triggerTimeTextView;
    final TextView repetitionTextView;
    final SwitchCompat alarmSwitch;
    private final OnItemClickListener onItemClickListener;

    /**
     * The alarm holder
     * @param itemView the item view
     * @param onItemClickListener the onItemClickListener
     */
    AlarmHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        View.OnClickListener onClickListener = this;
        itemView.setOnClickListener(onClickListener);
        dayNightImageView = itemView.findViewById(R.id.image_view_day_night);
        alarmTitleTextView = itemView.findViewById(R.id.text_view_alarm_title_card);
        triggerTimeTextView = itemView.findViewById(R.id.text_view_trigger_time);
        repetitionTextView = itemView.findViewById(R.id.text_view_repetition);
        alarmSwitch = itemView.findViewById(R.id.switch_alarm);
    }

    @Override
    public void onClick(View view) {
        onItemClickListener.onItemClick(getLayoutPosition());
    }

}
