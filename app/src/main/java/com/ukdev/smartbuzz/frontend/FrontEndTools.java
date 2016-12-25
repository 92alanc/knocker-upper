package com.ukdev.smartbuzz.frontend;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.activities.HomeActivity;
import com.ukdev.smartbuzz.backend.BackEndTools;
import com.ukdev.smartbuzz.database.AlarmRepository;
import com.ukdev.smartbuzz.extras.AppConstants;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.RingtoneWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * UI tools class
 * Provides useful tools for UI operations
 * Created by Alan Camargo - April 2016
 */
public class FrontEndTools
{

    /**
     * Shows a toast
     * @param context - Context
     * @param text - String
     * @param duration - int
     */
    public static void showToast(Context context, String text, int duration)
    {
        Toast.makeText(context, text, duration).show();
    }

    /**
     * Shows a simple dialogue
     * @param context - Context
     * @param title - String
     * @param message - String
     * @param iconId - int
     * @param neutralButtonText - String
     * @param listener - OnClickListener
     */
    public static void showDialogue(Context context, String title, String message,
                                    int iconId, String neutralButtonText,
                                    DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(context);
        dialogue.setTitle(title);
        dialogue.setMessage(message);
        dialogue.setIcon(iconId);
        dialogue.setNeutralButton(neutralButtonText, listener);
        dialogue.show();
    }

    /**
     * Adapts the alarms ListView
     * @param context - Context
     * @param listView - ListView
     * @param orderBy - String
     */
    public static void adaptAlarmsListView(Context context, ListView listView,
                                           String orderBy)
    {
        Alarm[] alarms = AlarmRepository.getInstance(context).selectAll(orderBy);
        AlarmAdapter adapter = new AlarmAdapter(context, R.layout.alarm_listview_item, alarms);
        listView.setAdapter(adapter);
    }

    /**
     * Adapts the snooze spinner
     * @param context - Context
     * @param spinner - AppCompatSpinner
     */
    public static void adaptSnoozeSpinner(Context context, AppCompatSpinner spinner)
    {
        String[] values = new String[3];
        values[0] = context.getString(R.string.five_min);
        values[1] = context.getString(R.string.ten_min);
        values[2] = context.getString(R.string.fifteen_min);
        SnoozeAdapter adapter = new SnoozeAdapter(context, values);
        spinner.setAdapter(adapter);
    }

    /**
     * Starts an activity
     * @param context - Context
     * @param targetActivity - Class
     */
    public static void startActivity(Context context, Class<?> targetActivity)
    {
        Intent intent = new Intent(context, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Adapts the ringtone picker
     * @param context - Context
     * @param ringtonePicker - AppCompatSpinner
     */
    public static void adaptRingtonePicker(Context context, AppCompatSpinner ringtonePicker)
    {
        ArrayList<RingtoneWrapper> ringtones = RingtoneWrapper.getAllRingtones(context);
        RingtoneAdapter adapter = new RingtoneAdapter(context, R.layout.ringtone_spinner_item,
                ringtones);
        ringtonePicker.setAdapter(adapter);
    }

    /**
     * Shows a notification with the number of alarms set
     * @param context - Context
     */
    public static void showNotification(Context context)
    {
        int alarmCount = AlarmRepository.getInstance(context).getActiveAlarms().size();
        NotificationManager manager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (alarmCount > 0)
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.alarm);

            builder.setContentTitle(context.getString(R.string.app_name));
            String text;
            if (alarmCount == 1)
                text = context.getResources().getString(R.string.one_alarm_set);
            else
                text = String.format(context.getResources().getString(R.string.x_alarms_set), alarmCount);
            builder.setContentText(text);

            Intent resultIntent = new Intent(context, HomeActivity.class);
            resultIntent.setAction(Intent.ACTION_RUN);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            builder.setOngoing(false);
            manager.notify(AppConstants.NOTIFICATION_ID, builder.build());
        }
        else
            manager.cancel(AppConstants.NOTIFICATION_ID);
    }

    /**
     * Hides the keyboard
     * @param activity - Activity
     */
    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager manager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Tells if the screen is locked
     * @param context - Context
     * @return screen is locked
     */
    public static boolean screenIsLocked(Context context)
    {
        KeyguardManager manager = (KeyguardManager)context
                .getSystemService(Context.KEYGUARD_SERVICE);
        return manager.inKeyguardRestrictedInputMode();
    }

    /**
     * Gets all toggle buttons in a grid layout
     * @param layout - GridLayout
     * @return all toggle buttons
     */
    public static ToggleButton[] getToggleButtons(GridLayout layout)
    {
        List<ToggleButton> list = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++)
        {
            if (layout.getChildAt(i) instanceof ToggleButton)
                list.add((ToggleButton) layout.getChildAt(i));
        }
        if (list.size() > 0)
        {
            ToggleButton[] buttons = new ToggleButton[list.size()];
            for (int i = 0; i < buttons.length; i++)
                buttons[i] = list.get(i);
            return buttons;
        }
        else
            return null;
    }

    /**
     * Shows a toast containing the time left for an alarm to trigger
     * @param context - Context
     * @param alarm - Alarm
     */
    public static void showTimeLeftToTrigger(Context context, Alarm alarm)
    {
        int isReminder = alarm.isReminder() ? 1 : 0;
        String token;
        int[] timeLeft = BackEndTools.getTimeLeftToTrigger(alarm);
        int days = timeLeft[0];
        int hours = timeLeft[1];
        int minutes = timeLeft[2];
        switch (isReminder)
        {
            case 1:
                if (days > 0 && hours == 0 && minutes == 0)
                    token = String.format(context.getString(R.string.remaining_d_reminder), days);
                else if (days > 0 && hours > 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_d_h_m_reminder), days, hours, minutes);
                else if (days > 0 && hours > 0 && minutes == 0)
                    token = String.format(context.getString(R.string.remaining_d_h_reminder), days, hours);
                else if (days > 0 && hours == 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_d_m_reminder), days, minutes);
                else if (days == 0 && hours > 0 && minutes == 0)
                    token = String.format(context.getString(R.string.remaining_h_reminder), hours);
                else if (days == 0 && hours > 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_h_m_reminder), hours, minutes);
                else if (days == 0 && hours == 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_m_reminder), minutes);
                else
                    token = String.format(context.getString(R.string.remaining_d_reminder), 1);
                break;
            case 0:
                if (days > 0 && hours == 0 && minutes == 0)
                    token = String.format(context.getString(R.string.remaining_d_alarm), days);
                else if (days > 0 && hours > 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_d_h_m_alarm), days, hours, minutes);
                else if (days > 0 && hours > 0 && minutes == 0)
                    token = String.format(context.getString(R.string.remaining_d_h_alarm), days, hours);
                else if (days > 0 && hours == 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_d_m_alarm), days, minutes);
                else if (days == 0 && hours > 0 && minutes == 0)
                    token = String.format(context.getString(R.string.remaining_h_alarm), hours);
                else if (days == 0 && hours > 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_h_m_alarm), hours, minutes);
                else if (days == 0 && hours == 0 && minutes > 0)
                    token = String.format(context.getString(R.string.remaining_m_alarm), minutes);
                else
                    token = String.format(context.getString(R.string.remaining_d_alarm), 1);
                break;
            default:
                token = null;
                break;
        }
        showToast(context, token, Toast.LENGTH_SHORT);
    }

    /**
     * Shows ads
     * @param activity - Activity
     * @param resId - int
     */
    public static void showAds(Activity activity, int resId)
    {
        AdView adView = (AdView)activity.findViewById(resId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}
