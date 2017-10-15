package com.ukdev.smartbuzz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.util.AlarmHandler;

import java.util.List;

/**
 * A {@code BroadcastReceiver} triggered when the
 * system time changes
 *
 * @author Alan Camargo
 */
public class TimeChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_TIME_CHANGED.equals(intent.getAction()))
            return;
        List<Alarm> activeAlarms = AlarmDao.getInstance(context).getActiveAlarms();
        int size = activeAlarms.size();
        if (size <= 0)
            return;
        for (Alarm alarm : activeAlarms) {
            AlarmHandler alarmHandler = new AlarmHandler(context, alarm);
            alarmHandler.updateAlarm();
        }
        Toast.makeText(context, R.string.time_changed, Toast.LENGTH_LONG).show();
    }

}
