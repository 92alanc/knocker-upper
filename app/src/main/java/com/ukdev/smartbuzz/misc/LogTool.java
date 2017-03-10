package com.ukdev.smartbuzz.misc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.ukdev.smartbuzz.R;

public class LogTool {

    private AlertDialog.Builder dialogue;

    public LogTool(Context context) {
        dialogue = new AlertDialog.Builder(context);
    }

    public void exception(Exception e) {
        dialogue.setTitle(R.string.app_name);
        dialogue.setMessage(e.getMessage());
        dialogue.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        dialogue.show();
    }

    public void info(String text) {
        dialogue.setTitle(R.string.info);
        dialogue.setMessage(text);
        dialogue.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        dialogue.show();
    }

}
