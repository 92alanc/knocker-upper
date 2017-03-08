package com.ukdev.smartbuzz.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.ukdev.smartbuzz.backend.enums.Action;
import com.ukdev.smartbuzz.backend.enums.Extra;
import com.ukdev.smartbuzz.exception.NullAlarmException;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.enums.Day;

import java.util.Calendar;
import java.util.Random;

public class AlarmHandler {

    private Alarm alarm;
    private AlarmManager manager;
    private Context context;

    private static final int TWO_MINUTES = 2 * 60 * 1000;
    private static final int THREE_MINUTES = 3 * 60 * 1000;

    public AlarmHandler(Context context, Alarm alarm) throws NullAlarmException {
        if (alarm == null)
            throw new NullAlarmException();
        this.alarm = alarm;
        this.context = context;
        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    public void callSleepChecker() {
        Random random = new Random();
        Calendar now = Calendar.getInstance();
        int upToTwoMinutes = random.nextInt(TWO_MINUTES);
        int randomTime = THREE_MINUTES + upToTwoMinutes;
        long callTime = now.getTimeInMillis() + randomTime;
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Action.CALL_SLEEP_CHECKER.toString());
        intent.putExtra(Extra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 999, intent, 0);
        startAlarmManager(callTime, pendingIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(Action.CANCEL_ALARM.toString());
        intent.putExtra(Extra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }

    public void cancelDelay() {
        Intent intent = new Intent(Action.CANCEL_ALARM.toString());
        intent.setAction(Action.DELAY_ALARM.toString());
        intent.putExtra(Extra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }

    public void delayAlarm() {
        Calendar now = Calendar.getInstance();
        long triggerTime = alarm.getSnoozeDuration().getValue() + now.getTimeInMillis();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Action.DELAY_ALARM.toString());
        intent.putExtra(Extra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(triggerTime, pendingIntent);
    }

    public void handleReceivedAlarm() {
        if (alarm.isActive()) {
            Calendar now = Calendar.getInstance();
            Day today = Day.fromInt(now.get(Calendar.DAY_OF_WEEK));
            Day tomorrow;
            if (today == Day.SATURDAY)
                tomorrow = Day.SUNDAY;
            else
                tomorrow = Day.fromInt(today.getValue() + 1);
            // triggerFlags[0] = trigger today, triggerFlags[1] = trigger tomorrow
            boolean[] triggerFlags = new boolean[2];
            if (alarm.repeats()) {
                Day[] repetition = alarm.getRepetition();
                for (int i = 0; i < repetition.length; i++) {
                    if (i < repetition.length - 1) {
                        if (repetition[i + 1] == tomorrow)
                            triggerFlags[1] = true;
                    }
                    else {
                        if (repetition[0] == tomorrow)
                            triggerFlags[1] = true;
                    }
                    if (triggerFlags[1]) // If the alarm should trigger tomorrow
                        scheduleAlarm();
                    if (repetition[i] == today) {
                        triggerFlags[0] = true;
                        break;
                    }
                }
            }
            else
                triggerFlags[0] = true;
            if (triggerFlags[0]) { // If the alarm should trigger today
                Intent activityIntent = new Intent(context, null); // TODO: replace null with AlarmActivity.class
                activityIntent.putExtra(Extra.ID.toString(), alarm.getId());
                activityIntent.setAction(Action.TRIGGER_ALARM.toString());
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            }
        }
    }

    public void scheduleAlarm() {
        // TODO: update alarm in database
        long triggerTime = alarm.getTriggerTime().getTimeInMillis(); // TODO: implement getNextValidTriggerTime
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Action.SCHEDULE_ALARM.toString());
        intent.putExtra(Extra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(triggerTime, pendingIntent);
    }

    public void updateAlarm() {
        cancelAlarm();
        cancelDelay();
        scheduleAlarm();
    }

    private void startAlarmManager(long triggerTime, PendingIntent pendingIntent)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            manager.setAlarmClock(new AlarmManager.AlarmClockInfo(triggerTime, pendingIntent), pendingIntent);
    }

}
