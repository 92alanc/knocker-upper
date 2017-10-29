package com.ukdev.smartbuzz.misc;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.ukdev.smartbuzz.R;

/**
 * A graphic log tool
 *
 * @author Alan Camargo
 */
public class LogTool {

    private final AlertDialog.Builder dialogue;

    /**
     * Default constructor for {@code LogTool}
     * @param context the Android context
     */
    public LogTool(Context context) {
        dialogue = new AlertDialog.Builder(context);
        dialogue.setTitle(R.string.app_name);
    }

    /**
     * Logs simple information
     * @param text the information text
     */
    public void info(String text) {
        dialogue.setMessage(text);
        dialogue.setIcon(R.mipmap.ic_launcher);
        dialogue.setNeutralButton(R.string.ok, null);
        dialogue.show();
    }

}
