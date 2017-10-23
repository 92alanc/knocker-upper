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
 * A {@code BroadcastReceiver} triggered when the device boots
 *
 * @author Alan Camargo
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            List<Alarm> activeAlarms = AlarmDao.getInstance(context).getActiveAlarms();
            int size = activeAlarms.size();
            if (size <= 0)
                return;
            for (Alarm alarm : activeAlarms) {
                AlarmHandler handler = new AlarmHandler(context, alarm);
                handler.setAlarm();
            }
            String text = context.getResources().getQuantityString(R.plurals.alarms_set, size, size);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

}
