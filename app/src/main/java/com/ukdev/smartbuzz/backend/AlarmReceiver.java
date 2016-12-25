package com.ukdev.smartbuzz.backend;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.model.Alarm;

/**
 * Alarm receiver
 * Receives all alarm intents
 * Created by Alan Camargo - April 2016
 */
public class AlarmReceiver extends WakefulBroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int id = intent.getIntExtra(AppConstants.EXTRA_ID, AppConstants.DEFAULT_INTENT_EXTRA);
        Alarm alarm = AlarmRepository.getInstance(context).select(id);
        if (intent.getAction().equals(AppConstants.ACTION_TRIGGER_ALARM))
            AlarmHandler.handleReceivedAlarm(context, alarm);
        else if (intent.getAction().equals(AppConstants.ACTION_SLEEP_CHECKER))
            AlarmHandler.handleReceivedSleepChecker(context, alarm);
        else if (intent.getAction().equals(AppConstants.ACTION_SNOOZE))
            AlarmHandler.handleSnoozeTask(context, alarm);
    }

}
