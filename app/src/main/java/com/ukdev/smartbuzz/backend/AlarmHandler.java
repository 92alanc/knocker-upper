package com.ukdev.smartbuzz.backend;

import android.app.AlarmManager;
import android.content.Context;
import com.ukdev.smartbuzz.model.Alarm;

public class AlarmHandler {

    private Alarm alarm;
    private AlarmManager manager;
    private Context context;

    public AlarmHandler(Context context, Alarm alarm) {
        this.alarm = alarm;
        this.context = context;
        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    public void cancelAlarm() {

    }

    public void delayAlarm() {

    }

    public void scheduleAlarm() {

    }

}
