package com.ukdev.smartbuzz.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.exception.NullAlarmException;
import com.ukdev.smartbuzz.misc.LogTool;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ArrayList<Alarm> activeAlarms = AlarmDao.getInstance(context).getActiveAlarms();
            if (activeAlarms.size() > 0) {
                for (Alarm alarm : activeAlarms) {
                    try {
                        AlarmHandler handler = new AlarmHandler(context, alarm);
                        handler.setAlarm();
                    } catch (NullAlarmException e) {
                        LogTool log = new LogTool(context);
                        log.exception(e);
                    }
                }
                // TODO: show notification
            }
        }
    }

}
