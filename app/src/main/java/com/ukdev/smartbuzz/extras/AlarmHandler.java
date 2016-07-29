package com.ukdev.smartbuzz.extras;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import com.ukdev.smartbuzz.activities.AlarmActivity;
import com.ukdev.smartbuzz.activities.SleepCheckerActivity;
import com.ukdev.smartbuzz.database.AlarmDAO;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.TimeWrapper;

import java.util.Calendar;
import java.util.Random;

import static android.app.AlarmManager.RTC_WAKEUP;

/**
 * Alarm handler
 * Manages all alarm related operations
 * Created by Alan Camargo - June 2016
 */
public class AlarmHandler
{

    /**
     * Schedules an alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void scheduleAlarm(Context context, Alarm alarm)
    {
        AlarmManager manager = (AlarmManager)context
                .getSystemService(Context.ALARM_SERVICE);
        long triggerTime = TimeWrapper
                .getNextValidTrigger(alarm);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AppConstants.ACTION_TRIGGER_ALARM);
        intent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        if (AppConstants.OS_VERSION < Build.VERSION_CODES.KITKAT)
            manager.set(RTC_WAKEUP, triggerTime, pendingIntent);
        else
            manager.setExact(RTC_WAKEUP, triggerTime, pendingIntent);
    }

    /**
     * Handles a received alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    static void handleReceivedAlarm(Context context, Alarm alarm)
    {
        if (alarm != null && alarm.isOn())
        {
            Calendar now = Calendar.getInstance();
            int today = now.get(Calendar.DAY_OF_WEEK);
            int tomorrow;
            if (today == 7) // If today is Saturday (day 7)
                tomorrow = 1;
            else
                tomorrow = today + 1;
            // triggerFlags[0] = trigger today, triggerFlags[1] = trigger tomorrow
            boolean[] triggerFlags = new boolean[2];
            if (alarm.repeats())
            {
                int[] repetition = alarm.getRepetition();
                for (int i = 0; i < repetition.length; i++)
                {
                    if (i < repetition.length - 1)
                    {
                        if (repetition[i + 1] == tomorrow)
                            triggerFlags[1] = true;
                    }
                    else
                    {
                        if (repetition[0] == tomorrow)
                            triggerFlags[1] = true;
                    }
                    if (triggerFlags[1]) // If the alarm should trigger tomorrow
                        AlarmHandler.scheduleAlarm(context, alarm);
                    if (repetition[i] == today)
                    {
                        triggerFlags[0] = true;
                        break;
                    }
                }
            }
            else
                triggerFlags[0] = true;
            if (triggerFlags[0]) // If the alarm should trigger today
            {
                Intent activityIntent = new Intent(context, AlarmActivity.class);
                activityIntent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
                activityIntent.setAction(AppConstants.ACTION_TRIGGER_ALARM);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            }
        }
    }

    /**
     * Cancels an alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void cancelAlarm(Context context, Alarm alarm)
    {
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AppConstants.ACTION_CANCEL_ALARM);
        intent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Updates an alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void updateAlarm(Context context, Alarm alarm)
    {
        cancelAlarm(context, alarm);
        scheduleAlarm(context, alarm);
    }

    /**
     * Handles a received Sleep checker intent
     * @param context - Context
     */
    static void handleReceivedSleepChecker(Context context, Alarm alarm)
    {
        if (alarm != null)
        {
            Intent activityIntent = new Intent(context, SleepCheckerActivity.class);
            activityIntent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.setAction(AppConstants.ACTION_SLEEP_CHECKER);
            context.startActivity(activityIntent);
        }
    }

    /**
     * Calls Sleep Checker in a
     * random time between 3 and 5 minutes
     * @param context - Context
     * @param alarm - Alarm
     */
    private static void callSleepChecker(Context context, Alarm alarm)
    {
        if (alarm != null)
        {
            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Random random = new Random();
            Calendar now = Calendar.getInstance();
            int upToTwoMinutes = random.nextInt(AppConstants.TWO_MINUTES);
            int randomTime = AppConstants.THREE_MINUTES + upToTwoMinutes;
            long callTime = now.getTimeInMillis() + randomTime;
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(AppConstants.ACTION_SLEEP_CHECKER);
            intent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 999, intent, 0);
            if (AppConstants.OS_VERSION >= Build.VERSION_CODES.KITKAT)
                manager.setExact(AlarmManager.RTC_WAKEUP, callTime, pendingIntent);
            else
                manager.set(AlarmManager.RTC_WAKEUP, callTime, pendingIntent);
        }
    }

    /**
     * Handles a received snooze task
     * @param context - Context
     * @param alarm - Alarm
     */
    static void handleSnoozeTask(Context context, Alarm alarm)
    {
        if (alarm != null)
        {
            Intent activityIntent = new Intent(context, AlarmActivity.class);
            activityIntent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.setAction(AppConstants.ACTION_TRIGGER_ALARM);
            context.startActivity(activityIntent);
        }
    }

    /**
     * Snoozes the ongoing alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void snoozeAlarm(Context context, Alarm alarm)
    {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar now = Calendar.getInstance();
        long snoozeTime;
        switch (alarm.getSnooze())
        {
            case 5:
                snoozeTime = AppConstants.FIVE_MINUTES;
                break;
            case 10:
                snoozeTime = AppConstants.TEN_MINUTES;
                break;
            case 15:
                snoozeTime = AppConstants.FIFTEEN_MINUTES;
                break;
            default:
                // Damn! Something really wrong happened
                snoozeTime = Long.MIN_VALUE;
                break;
        }
        snoozeTime = snoozeTime + now.getTimeInMillis();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AppConstants.ACTION_SNOOZE);
        intent.putExtra(AppConstants.EXTRA_ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        if (AppConstants.OS_VERSION >= Build.VERSION_CODES.KITKAT)
            manager.setExact(RTC_WAKEUP, snoozeTime, pendingIntent);
        else
            manager.set(RTC_WAKEUP, snoozeTime, pendingIntent);
    }

    /**
     * Dismisses the alarm
     */
    public static void dismissAlarm(Alarm alarm, Activity activity,
                                    Context context, MediaPlayer player,
                                    Vibrator vibrator, PowerManager.WakeLock wakeLock)
    {
        if (alarm.vibrates() || activity.getIntent().getAction().equals(AppConstants.ACTION_MAYHEM))
            vibrator.cancel();
        player.release();
        if (!alarm.isReminder())
        {
            callSleepChecker(context, alarm);
            alarm.setLocked(true);
            AlarmDAO.update(context, alarm.getId(), alarm);
        }
        if (alarm.isReminder() && !alarm.repeats())
            killAlarm(context, alarm);
        if (wakeLock.isHeld())
            wakeLock.release();
        BackEndTools.killApp(activity);
    }

    /**
     * Kills an alarm
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void killAlarm(Context context, Alarm alarm)
    {
        alarm.toggle(false);
        AlarmDAO.update(context, alarm.getId(), alarm);
        FrontEndTools.showNotification(context);
    }

}
