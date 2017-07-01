package com.ukdev.smartbuzz.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
            if (activeAlarms.size() > 0) {
                for (Alarm alarm : activeAlarms) {
                    AlarmHandler handler = new AlarmHandler(context, alarm);
                    handler.setAlarm();
                }
                // TODO: show notification
            }
        }
    }

}
