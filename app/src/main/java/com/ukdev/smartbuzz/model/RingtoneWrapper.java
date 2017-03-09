package com.ukdev.smartbuzz.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;

public class RingtoneWrapper {

    private String title;
    private Uri uri;

    public RingtoneWrapper(String title, Uri uri) {
        this.title = title;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

    public static RingtoneWrapper findRingtoneByUri(Context context, Uri uri) {
        ArrayList<RingtoneWrapper> ringtones = getAllRingtones(context);
        RingtoneWrapper ret = ringtones.get(0);
        for (RingtoneWrapper ringtone : ringtones) {
            if (ringtone.getUri().equals(uri))
                return ringtone;
        }
        return ret;
    }

    public static ArrayList<RingtoneWrapper> getAllRingtones(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        ArrayList<RingtoneWrapper> ringtones = new ArrayList<>();
        Uri uri;
        String title;
        for (int i = 0; i < cursor.getCount(); i++) {
            uri = manager.getRingtoneUri(i);
            context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            title = manager.getRingtone(i).getTitle(context);
            ringtones.add(new RingtoneWrapper(title, uri));
        }
        return ringtones;
    }

}
