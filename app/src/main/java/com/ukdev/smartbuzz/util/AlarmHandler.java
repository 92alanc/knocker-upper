package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.AlarmClock;
import android.util.Log;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.activities.AlarmActivity;
import com.ukdev.smartbuzz.activities.SetupActivity;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.misc.Action;
import com.ukdev.smartbuzz.misc.Extra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.receivers.AlarmReceiver;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Controls various operations related to
 * an {@link Alarm}
 *
 * @author Alan Camargo
 */
public class AlarmHandler {

    private final Alarm alarm;
    private final AlarmManager manager;
    private final Context context;
    private final AlarmDao database;

    /**
     * Default constructor for {@code AlarmHandler}
     * @param context the Android context
     * @param alarm the alarm
     */
    public AlarmHandler(Context context, Alarm alarm) {
        this.alarm = alarm;
        this.context = context;
        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        database = AlarmDao.getInstance(context);
    }

    /**
     * Cancels an ongoing alarm
     */
    public void cancelAlarm() {
        cancelDelay();
        Intent intent = new Intent(Action.CANCEL_ALARM);
        intent.putExtra(Extra.ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent,
                                                                 PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }

    /**
     * Delays an alarm
     */
    public void delayAlarm() {
        Calendar now = Calendar.getInstance();
        long triggerTime = alarm.getSnoozeDuration() + now.getTimeInMillis();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Action.DELAY_ALARM);
        intent.putExtra(Extra.ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(manager, triggerTime, pendingIntent);
    }

    /**
     * Dismisses an alarm
     * @param activity the activity receiving the intent
     * @param wakeLock the wake lock to be released
     */
    public void dismissAlarm(Activity activity, PowerManager.WakeLock wakeLock) {
        if (alarm.isSleepCheckerOn())
            callSleepChecker();
        if (!alarm.isSleepCheckerOn() && !alarm.repeats()) {
            alarm.setActive(false);
            database.update(alarm);
        }
        if (wakeLock.isHeld())
            wakeLock.release();
        Utils.killApp(activity);
    }

    /**
     * Sets a new alarm
     */
    public void setAlarm() {
        long triggerTime = Utils.getNextValidTriggerTime(alarm);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Action.TRIGGER_ALARM);
        intent.putExtra(Extra.ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(manager, triggerTime, pendingIntent);
    }

    /**
     * Sets a new alarm by voice
     * @param intent the intent received from
     *               the voice command
     * @param activity the activity
     */
    public static void setAlarmByVoice(Intent intent, Activity activity) {
        Context context = activity.getApplicationContext();
        if (!intent.hasExtra(AlarmClock.EXTRA_HOUR)) {
            Intent i = new Intent(context, SetupActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return;
        }
        final int defaultValue = 0;
        String title = context.getString(R.string.new_alarm);
        if (intent.hasExtra(AlarmClock.EXTRA_MESSAGE))
            title = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE);
        int hour = intent.getIntExtra(AlarmClock.EXTRA_HOUR, defaultValue);
        int minutes = 0;
        if (intent.hasExtra(AlarmClock.EXTRA_MINUTES))
            minutes = intent.getIntExtra(AlarmClock.EXTRA_MINUTES, defaultValue);
        long triggerTime = new Time(hour, minutes).toCalendar().getTimeInMillis();
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        int volume = Utils.getDefaultVolume(context);

        Integer[] repetition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (intent.hasExtra(AlarmClock.EXTRA_DAYS)) {
                List<Integer> days = intent.getIntegerArrayListExtra(AlarmClock.EXTRA_DAYS);
                repetition = new Integer[days.size()];
                repetition = days.toArray(repetition);
            }
        }

        AlarmBuilder alarmBuilder = new AlarmBuilder();
        alarmBuilder.setTitle(title)
                    .setTriggerTime(triggerTime)
                    .setSnoozeDuration(Time.FIVE_MINUTES)
                    .setRingtoneUri(ringtoneUri)
                    .setVolume(volume)
                    .setRepetition(repetition);
        Alarm alarm = alarmBuilder.build();
        AlarmDao dao = AlarmDao.getInstance(context);
        long id = dao.insert(alarm);
        alarm.setId((int) id);

        long nextValidTriggerTime = Utils.getNextValidTriggerTime(alarm);
        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        receiverIntent.setAction(Action.TRIGGER_ALARM);
        receiverIntent.putExtra(Extra.ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(),
                                                                 receiverIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        startAlarmManager(manager, nextValidTriggerTime, pendingIntent);
        Utils.killApp(activity);
    }

    /**
     * Updates an alarm
     */
    public void updateAlarm() {
        cancelAlarm();
        setAlarm();
    }

    /**
     * Starts the target activity to display
     * the alarm being triggered
     */
    public void startAlarmActivity() {
        final boolean triggerSleepChecker = false;
        Intent intent = AlarmActivity.getIntent(context, alarm.getId(), triggerSleepChecker);
        context.startActivity(intent);
    }

    /**
     * Triggers an alarm
     */
    public void triggerAlarm() {
        if (alarm.isActive()) {
            Calendar now = Calendar.getInstance();
            int today = now.get(Calendar.DAY_OF_WEEK);
            int tomorrow;
            if (today == Calendar.SATURDAY)
                tomorrow = Calendar.SUNDAY;
            else
                tomorrow = today + 1;
            boolean triggerToday = false;
            boolean triggerTomorrow = false;
            if (alarm.repeats()) {
                Integer[] repetition = alarm.getRepetition();
                for (int i = 0; i < repetition.length; i++) {
                    if (i < repetition.length - 1) {
                        if (repetition[i + 1] == tomorrow)
                            triggerTomorrow = true;
                    } else {
                        Calendar time = Calendar.getInstance();
                        time.setTimeInMillis(alarm.getTriggerTime());
                        Log.d("Alan", String.format("trigger time = %1$d:%2$d",
                                                    time.get(Calendar.HOUR_OF_DAY),
                                                    time.get(Calendar.MINUTE)));
                        Integer[] repetition1 = alarm.getRepetition();
                        for (int i1 = 0; i1 < repetition1.length; i1++) {
                            Integer day = repetition1[i1];
                            Log.d("Alan", "repetition[" + i1 + "] = " + day);
                        }
                        Log.d("Alan", "today = " + today);
                        Log.d("Alan", "tomorrow = " + tomorrow);
                        if (repetition[0] == tomorrow)
                            triggerTomorrow = true;
                    }
                    if (triggerTomorrow)
                        setAlarm();
                    if (repetition[i] == today) {
                        triggerToday = true;
                        break;
                    }
                }
            } else
                triggerToday = true;
            if (triggerToday)
                startAlarmActivity();
        }
    }

    /**
     * Triggers Sleep Checker
     */
    public void triggerSleepChecker() {
        final boolean triggerSleepChecker = true;
        Intent intent = AlarmActivity.getIntent(context, alarm.getId(), triggerSleepChecker);
        context.startActivity(intent);
    }

    private void cancelDelay() {
        Intent intent = new Intent(Action.DELAY_ALARM);
        intent.putExtra(Extra.ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent,
                                                                 PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }

    private void callSleepChecker() {
        Random random = new Random();
        Calendar now = Calendar.getInstance();
        int upToTwoMinutes = random.nextInt(Time.TWO_MINUTES);
        int randomTime = Time.THREE_MINUTES + upToTwoMinutes;
        long callTime = now.getTimeInMillis() + randomTime;
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Action.TRIGGER_SLEEP_CHECKER);
        intent.putExtra(Extra.ID, alarm.getId());
        final int requestCode = 999;
        final int flags = 0;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
        startAlarmManager(manager, callTime, pendingIntent);
    }

    private static void startAlarmManager(AlarmManager manager, long triggerTime,
                                          PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerTime,
                                                                                         pendingIntent);
            manager.setAlarmClock(alarmClockInfo, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime,
                                              pendingIntent);
        }
    }

}
