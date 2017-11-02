package com.ukdev.smartbuzz.misc;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.ukdev.smartbuzz.R;

/**
 * An {@code AlertDialog} helper
 *
 * @author Alan Camargo
 */
public class DialogueHelper {

    private final AlertDialog.Builder dialogue;

    /**
     * Default constructor for {@code DialogueHelper}
     * @param context the Android context
     */
    public DialogueHelper(Context context) {
        dialogue = new AlertDialog.Builder(context);
        dialogue.setTitle(R.string.app_name);
    }

    /**
     * Shows a dialogue
     * @param text the dialogue text
     */
    public void showDialogue(String text) {
        dialogue.setMessage(text);
        dialogue.setIcon(R.mipmap.ic_launcher);
        dialogue.setNeutralButton(R.string.ok, null);
        dialogue.show();
    }

}
