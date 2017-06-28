package com.ukdev.smartbuzz.system;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.provider.AlarmClock;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.activities.SetupActivity;
import com.ukdev.smartbuzz.misc.IntentAction;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.exception.NullAlarmException;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.AlarmBuilder;
import com.ukdev.smartbuzz.model.Ringtone;
import com.ukdev.smartbuzz.model.enums.Day;
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

    private static final int TWO_MINUTES = 120000;
    private static final int THREE_MINUTES = 180000;

    /**
     * Default constructor for {@code AlarmHandler}
     * @param context the Android context
     * @param alarm the alarm
     * @throws NullAlarmException if the alarm is {@code null}
     */
    public AlarmHandler(Context context, Alarm alarm) throws NullAlarmException {
        if (alarm == null)
            throw new NullAlarmException(context);
        this.alarm = alarm;
        this.context = context;
        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        database = AlarmDao.getInstance(context);
    }

    /**
     * Calls Sleep checker
     */
    public void callSleepChecker() {
        Random random = new Random();
        Calendar now = Calendar.getInstance();
        int upToTwoMinutes = random.nextInt(TWO_MINUTES);
        int randomTime = THREE_MINUTES + upToTwoMinutes;
        long callTime = now.getTimeInMillis() + randomTime;
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(IntentAction.CALL_SLEEP_CHECKER.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        final int requestCode = 999;
        final int flags = 0;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
        startAlarmManager(callTime, pendingIntent);
    }

    /**
     * Cancels an ongoing alarm
     */
    public void cancelAlarm() {
        Intent intent = new Intent(IntentAction.CANCEL_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }

    /**
     * Cancels a delay
     */
    public void cancelDelay() {
        Intent intent = new Intent(IntentAction.CANCEL_ALARM.toString());
        intent.setAction(IntentAction.DELAY_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
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
        if (alarm.vibrates() || activity.getIntent().getAction().equals(IntentAction.WAKE_UP.toString()))
            alarm.stopVibration();
        alarm.stopRingtone();
        if (alarm.isSleepCheckerOn())
            callSleepChecker();
        if (!alarm.isSleepCheckerOn() && !alarm.repeats())
            alarm.setActive(false);
        if (wakeLock.isHeld())
            wakeLock.release();
        database.update(alarm);
        Utils.killApp(activity);
    }

    /**
     * Sets a new alarm
     */
    public void setAlarm() {
        database.update(alarm);
        long triggerTime = Utils.getNextValidTriggerTime(alarm);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(IntentAction.SCHEDULE_ALARM.toString());
        intent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
        startAlarmManager(triggerTime, pendingIntent);
    }

    /**
     * Sets a new alarm by voice
     * @param context the Android context
     * @param intent the intent received from
     *               the voice command
     */
    public void setAlarmByVoice(Context context, Intent intent) {
        if (!intent.hasExtra(AlarmClock.EXTRA_HOUR)) {
            Intent i = new Intent(context, SetupActivity.class);
            i.setAction(IntentAction.CREATE_ALARM.toString());
            boolean sleepCheckerOn = true;
            i.putExtra(IntentExtra.SLEEP_CHECKER_ON.toString(), sleepCheckerOn);
            context.startActivity(i);
            return;
        }
        int nextAvailableId = database.getLastId() + 1;
        final int defaultValue = 0;
        final int firstRingtoneIndex = 0;
        String title = context.getString(R.string.new_alarm);
        if (intent.hasExtra(AlarmClock.EXTRA_MESSAGE))
            title = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE);
        int hour = intent.getIntExtra(AlarmClock.EXTRA_HOUR, defaultValue);
        int minutes = 0;
        if (intent.hasExtra(AlarmClock.EXTRA_MINUTES))
            minutes = intent.getIntExtra(AlarmClock.EXTRA_MINUTES, defaultValue);
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.set(Calendar.HOUR_OF_DAY, hour);
        triggerTime.set(Calendar.MINUTE, minutes);
        Ringtone ringtone = Ringtone.getAllRingtones(context).get(firstRingtoneIndex);
        int volume = Utils.getDefaultVolume(context);

        AlarmBuilder alarmBuilder = new AlarmBuilder(context);
        alarmBuilder.setId(nextAvailableId)
                    .setTitle(title)
                    .setTriggerTime(triggerTime)
                    .setSnoozeDuration(SnoozeDuration.FIVE_MINUTES)
                    .setRingtone(ringtone)
                    .setVolume(volume);
        Alarm alarm = alarmBuilder.build();
        database.insert(alarm);
        setAlarm();
    }

    /**
     * Starts the target activity to display
     * the alarm being triggered
     */
    public void startAlarmActivity() {
        Intent activityIntent = new Intent(context, null); // TODO: replace null with AlarmActivity.class
        activityIntent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        activityIntent.setAction(IntentAction.TRIGGER_ALARM.toString());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }

    /**
     * Triggers an alarm
     */
    public void triggerAlarm() {
        if (alarm.isActive()) {
            Calendar now = Calendar.getInstance();
            Day today = Day.valueOf(now.get(Calendar.DAY_OF_WEEK));
            Day tomorrow;
            if (today == Day.SATURDAY)
                tomorrow = Day.SUNDAY;
            else
                tomorrow = Day.valueOf(today.getValue() + 1);
            boolean triggerToday = false;
            boolean triggerTomorrow = false;
            if (alarm.repeats()) {
                Day[] repetition = alarm.getRepetition();
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
    public void triggerSleepChecker() {
        Intent activityIntent = new Intent(context, null); // TODO: replace null with SleepCheckerActivity.class
        activityIntent.putExtra(IntentExtra.ID.toString(), alarm.getId());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.setAction(IntentAction.TRIGGER_SLEEP_CHECKER.toString());
        context.startActivity(activityIntent);
    }

    /**
     * Updates an alarm
     */
    public void updateAlarm() {
        cancelAlarm();
        cancelDelay();
        setAlarm();
    }

    private void startAlarmManager(long triggerTime, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            manager.setAlarmClock(new AlarmManager.AlarmClockInfo(triggerTime, pendingIntent), pendingIntent);
    }

}