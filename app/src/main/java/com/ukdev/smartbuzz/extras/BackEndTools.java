package com.ukdev.smartbuzz.extras;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.GridLayout;
import android.widget.ToggleButton;
import com.ukdev.smartbuzz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Back end tools class
 * Provides useful tools to back end operations
 * Created by Alan Camargo - April 2016
 */
public class BackEndTools
{

    /**
     * Converts an int array to a comma-separated String like "1,2,3"
     * @param array - int[]
     * @return comma-separated String
     */
    public static String convertIntArrayToString(Context context, int[] array)
    {
        if (array == null)
            return null;
        else
        {
            if (array.length == 7)
                return context.getResources().getString(R.string.every_day);
            else
            {
                boolean weekDays = true;
                boolean weekends = true;
                for (int item : array)
                {
                    if (array.length != 2 || (item != 1 && item != 7))
                        weekends = false;
                    if (array.length != 5 || (item != 2 && item != 3 && item != 4
                            && item != 5 && item != 6))
                        weekDays = false;
                }
                if (weekDays)
                    return context.getResources().getString(R.string.week_days);
                else if (weekends)
                    return context.getResources().getString(R.string.weekends);
                else
                {
                    String[] texts = context.getResources()
                            .getStringArray(R.array.daysOfTheWeek);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < array.length; i++)
                    {
                        for (int j = 0; j < texts.length; j++)
                        {
                            if (j == (array[i] - 1))
                            {
                                sb.append(texts[j]);
                                if (i < array.length - 1)
                                    sb.append(", ");
                                break;
                            }
                        }
                    }
                    return sb.toString();
                }
            }
        }
    }

    /**
     * Converts a comma-separated String to an int array
     * @param context - Context
     * @param str - String
     * @return int array
     */
    public static int[] convertStringToIntArray(Context context, String str)
    {
        if (str == null)
            return null;
        else
        {
            int[] values;
            if (str.equals(context.getResources().getString(R.string.every_day)))
            {
                values = new int[7];
                for (int i = 0; i < values.length; i++)
                    values[i] = i + 1;
            }
            else if (str.equals(context.getResources().getString(R.string.week_days)))
            {
                values = new int[5];
                for (int i = 0; i < values.length; i++)
                    values[i] = i + 2;
            }
            else if (str.equals(context.getResources().getString(R.string.weekends)))
            {
                values = new int[2];
                values[0] = 1;
                values[1] = 7;
            }
            else
            {
                String[] texts = context.getResources()
                        .getStringArray(R.array.daysOfTheWeek);
                String[] split = str.split(", ");
                values = new int[split.length];
                for (int i = 0; i < split.length; i++)
                {
                    for (int j = 0; j < texts.length; j++)
                    {
                        if (split[i].equalsIgnoreCase(texts[j]))
                        {
                            values[i] = j + 1;
                            break;
                        }
                    }
                }
            }
            return values;
        }
    }

    /**
     * Gets an integer array with references
     * @param id - int
     * @return references
     */
    static int[] getReferencesArray(Context context, int id)
    {
        TypedArray typedArray = context.getResources().obtainTypedArray(id);
        int[] values = new int[typedArray.length()];
        for (int i = 0; i < values.length; i++)
        {
            values[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return values;
    }

    /**
     * Gets all selected repetition button values
     * @param layout - GridLayout
     * @return values
     */
    public static int[] getSelectedRepetition(GridLayout layout)
    {
        if (FrontEndTools.getToggleButtons(layout) != null)
        {
            ToggleButton[] buttons = FrontEndTools.getToggleButtons(layout);
            List<Integer> list = new ArrayList<>();
            boolean noButtonsChecked = true;
            assert buttons != null;
            for (int i = 0; i < buttons.length; i++)
            {
                if (buttons[i].isChecked())
                {
                    noButtonsChecked = false;
                    list.add(i + 1);
                }
            }
            if (noButtonsChecked)
                return null;
            else
            {
                int[] days = new int[list.size()];
                for (int i = 0; i < days.length; i++)
                    days[i] = list.get(i);
                return days;
            }
        }
        else
            return null;
    }

    /**
     * Requests Android M+ permissions
     * @param context - Context
     * @param activity - Activity
     */
    public static void requestPermissions(Context context, Activity activity)
    {
        // First we'll check if the user has already granted the permissions
        int readExternalStorage = ContextCompat.checkSelfPermission(context,
                AppConstants.PERMISSION_READ_EXTERNAL_STORAGE);

        // Request read external storage permission
        if (readExternalStorage != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity,
                    new String[] { AppConstants.PERMISSION_READ_EXTERNAL_STORAGE },
                    AppConstants.REQUEST_CODE);
    }

    /**
     * Kills the app
     * @param activity - Activity
     */
    public static void killApp(Activity activity)
    {
        activity.finish();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

}