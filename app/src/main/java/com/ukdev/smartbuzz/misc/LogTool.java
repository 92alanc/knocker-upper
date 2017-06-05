package com.ukdev.smartbuzz.misc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import com.ukdev.smartbuzz.R;

/**
 * A graphic log tool
 *
 * @author Alan Camargo
 */
public class LogTool {

    private AlertDialog.Builder dialogue;
    private Context context;

    /**
     * Default constructor for {@code LogTool}
     * @param context the Android context
     */
    public LogTool(Context context) {
        this.context = context;
        dialogue = new AlertDialog.Builder(context);
        dialogue.setTitle(R.string.app_name);
    }

    /**
     * Logs an exception
     * @param e the exception
     */
    public void exception(Exception e) {
        dialogue.setMessage(e.getMessage());
        dialogue.setIcon(R.mipmap.ic_sentiment_very_dissatisfied_white_24dp);
        dialogue.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        dialogue.show();
    }

    /**
     * Logs simple information
     * @param text the information text
     */
    public void info(String text) {
        dialogue.setMessage(text);
        dialogue.setIcon(R.drawable.app_icon);
        dialogue.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        dialogue.show();
    }

    /**
     * Logs simple information
     * @param resId the information text resource ID
     */
    public void info(@StringRes int resId) {
        info(context.getString(resId));
    }

}
