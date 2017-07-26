package com.ukdev.smartbuzz.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.List;

/**
 * A {@code BroadcastReceiver} triggered when the device boots
 *
 * @author Alan Camargo
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            List<Alarm> activeAlarms = AlarmDao.getInstance(context).getActiveAlarms();
            int size = activeAlarms.size();
            if (size > 0) {
                for (Alarm alarm : activeAlarms) {
                    AlarmHandler handler = new AlarmHandler(context, alarm);
                    handler.setAlarm();
                }
                if (size > 0) {
                    String text;
                    if (size == 1)
                        text = context.getString(R.string.alarm_set);
                    else
                        text = String.format(context.getString(R.string.alarms_set), size);
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
