package com.ukdev.smartbuzz.extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ukdev.smartbuzz.database.AlarmDAO;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

/**
 * Boot receiver
 * Called when the device is rebooted
 * Created by Alan Camargo - June 2016
 */
public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(AppConstants.ACTION_BOOT_COMPLETED))
        {
            ArrayList<Alarm> activeAlarms = AlarmDAO.getInstance(context).getActiveAlarms(context);
            if (activeAlarms.size() > 0)
            {
                for (Alarm alarm : activeAlarms)
                    AlarmHandler.scheduleAlarm(context, alarm);
            }
            FrontEndTools.showNotification(context);
        }
    }

}
