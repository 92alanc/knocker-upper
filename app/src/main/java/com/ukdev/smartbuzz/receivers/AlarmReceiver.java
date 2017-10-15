package com.ukdev.smartbuzz.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.util.AlarmHandler;

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
        if (IntentAction.TRIGGER_ALARM.toString().equals(intent.getAction()))
            handler.triggerAlarm();
        else if (IntentAction.TRIGGER_SLEEP_CHECKER.toString().equals(intent.getAction()))
            handler.triggerSleepChecker();
        else if (IntentAction.DELAY_ALARM.toString().equals(intent.getAction()))
            handler.startAlarmActivity();
    }

}
