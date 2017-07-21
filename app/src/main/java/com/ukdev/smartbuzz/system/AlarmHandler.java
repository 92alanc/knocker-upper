package com.ukdev.smartbuzz.system;

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
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.activities.AlarmActivity;
import com.ukdev.smartbuzz.activities.SetupActivity;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Time;
import com.ukdev.smartbuzz.model.enums.SnoozeDuration;
import com.ukdev.smartbuzz.util.Utils;

import java.util.Calendar;
import java.util.Random;

/**
 * Controls various operations related to
 * an {@link Alarm}
 *
 * @author Alan Camargo
 */
public class AlarmHandler {

    private Alarm alarm;
    private AlarmManager manager;
    private Context context;
    private AlarmDao database;

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
        Intent intent = new Intent(IntentAction.CANCEL_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }

    /**
     * Delays an alarm
     */
    public void delayAlarm() {
        Calendar now = Calendar.getInstance();
        long triggerTime = alarm.getSnoozeDuration().getValue() + now.getTimeInMillis();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(IntentAction.DELAY_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(triggerTime, pendingIntent);
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
        intent.setAction(IntentAction.TRIGGER_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(triggerTime, pendingIntent);
    }

    /**
     * Sets a new alarm by voice
     * @param intent the intent received from
     *               the voice command
     */
    public void setAlarmByVoice(Intent intent, Context context) {
        // FIXME: this s*** is creating 2 damn alarms
        if (!intent.hasExtra(AlarmClock.EXTRA_HOUR)) {
            Intent i = new Intent(context, SetupActivity.class);
            i.putExtra(IntentExtra.EDIT_MODE.toString(), false);
            i.putExtra(IntentExtra.SLEEP_CHECKER_ON.toString(), true);
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

        AlarmBuilder alarmBuilder = new AlarmBuilder();
        alarmBuilder.setTitle(title)
                    .setTriggerTime(triggerTime)
                    .setSnoozeDuration(SnoozeDuration.FIVE_MINUTES)
                    .setRingtoneUri(ringtoneUri)
                    .setVolume(volume);
        alarm = alarmBuilder.build();
        long id = database.insert(alarm);
        alarm.setId((int) id);
        database.update(alarm);
        setAlarm();
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
    void startAlarmActivity() {
        Intent activityIntent = new Intent(context, AlarmActivity.class);
        activityIntent.setAction(IntentAction.TRIGGER_ALARM.toString());
        activityIntent.putExtra(IntentExtra.SLEEP_CHECKER_ON.toString(), false);
        activityIntent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }

    /**
     * Triggers an alarm
     */
    void triggerAlarm() {
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
                int[] repetition = alarm.getRepetition();
                for (int i = 0; i < repetition.length; i++) {
                    if (i < repetition.length - 1) {
                        if (repetition[i + 1] == tomorrow)
                            triggerTomorrow = true;
                    } else {
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
    void triggerSleepChecker() {
        Intent activityIntent = new Intent(context, AlarmActivity.class);
        activityIntent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.setAction(IntentAction.TRIGGER_SLEEP_CHECKER.toString());
        context.startActivity(activityIntent);
    }

    private void cancelDelay() {
        Intent intent = new Intent(IntentAction.DELAY_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
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
        intent.setAction(IntentAction.TRIGGER_SLEEP_CHECKER.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        final int requestCode = 999;
        final int flags = 0;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
        startAlarmManager(callTime, pendingIntent);
    }

    private void startAlarmManager(long triggerTime, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerTime,
                                                                                         pendingIntent);
            manager.setAlarmClock(alarmClockInfo, pendingIntent);
        }
    }

}
