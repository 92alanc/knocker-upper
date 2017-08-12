package com.ukdev.smartbuzz.system;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;

/**
 * A {@code WakefulBroadcastReceiver} for alarms
 *
 * @author Alan Camargo
 */
@SuppressWarnings("deprecation")
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(IntentExtra.ID.toString(), 0);
        Alarm alarm = AlarmDao.getInstance(context).select(id);
        if (alarm == null)
            return;
        AlarmHandler handler = new AlarmHandler(context, alarm);
        if (intent.getAction().equals(IntentAction.TRIGGER_ALARM.toString()))
            handler.triggerAlarm();
        else if (intent.getAction().equals(IntentAction.TRIGGER_SLEEP_CHECKER.toString()))
            handler.triggerSleepChecker();
        else if (intent.getAction().equals(IntentAction.DELAY_ALARM.toString()))
            handler.startAlarmActivity();
    }

}
