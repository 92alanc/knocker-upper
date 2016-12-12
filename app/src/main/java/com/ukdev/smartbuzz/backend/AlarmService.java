package com.ukdev.smartbuzz.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Alarm service class
 * Created by Alan Camargo - December 2016
 */
public class AlarmService extends Service
{

    // TODO: implement AlarmService

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}
