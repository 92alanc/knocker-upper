package com.ukdev.smartbuzz.misc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.ukdev.smartbuzz.R;

public class LogTool {

    private AlertDialog.Builder dialogue;

    public LogTool(Context context) {
        dialogue = new AlertDialog.Builder(context);
        dialogue.setTitle(R.string.app_name);
    }

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

    public void info(String text) {
        dialogue.setMessage(text);
        dialogue.setIcon(R.mipmap.ic_priority_high_white_24dp);
        dialogue.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        dialogue.show();
    }

}
