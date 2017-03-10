package com.ukdev.smartbuzz.backend;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.ukdev.smartbuzz.backend.enums.Action;
import com.ukdev.smartbuzz.backend.enums.Extra;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.exception.NullAlarmException;
import com.ukdev.smartbuzz.model.Alarm;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(Extra.ID.toString(), 0);
        Alarm alarm = AlarmRepository.getInstance(context).select(id);
        try {
            AlarmHandler handler = new AlarmHandler(context, alarm);
            if (intent.getAction().equals(Action.TRIGGER_ALARM.toString()))
                handler.triggerAlarm();
            else if (intent.getAction().equals(Action.TRIGGER_SLEEP_CHECKER.toString()))
                handler.triggerSleepChecker();
            else if (intent.getAction().equals(Action.DELAY_ALARM.toString()))
                handler.startAlarmActivity();
        } catch (NullAlarmException e) {
            e.printStackTrace();
        }
    }

}
