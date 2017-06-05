package com.ukdev.smartbuzz.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * A ringtone
 *
 * @author Alan Camargo
 */
public class Ringtone {

    private String title;
    private Uri uri;

    /**
     * Default constructor for {@code RingtoneWrapper}
     * @param title the title
     * @param uri the URI
     */
    public Ringtone(String title, Uri uri) {
        this.title = title;
        this.uri = uri;
    }

    /**
     * Gets the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the URI
     * @return the URI
     */
    public Uri getUri() {
        return uri;
    }

    /**
     * Finds a ringtone by its URI
     * @param context the Android context
     * @param uri the URI
     * @return the ringtone found
     */
    public static Ringtone findRingtoneByUri(Context context, Uri uri) {
        List<Ringtone> ringtones = getAllRingtones(context);
        Ringtone ret = ringtones.get(0);
        for (Ringtone ringtone : ringtones) {
            if (ringtone.getUri().equals(uri))
                return ringtone;
        }
        return ret;
    }

    /**
     * Gets all ringtones available in the device
     * @param context the Android context
     * @return all ringtones available in the device
     */
    public static List<Ringtone> getAllRingtones(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        List<Ringtone> ringtones = new ArrayList<>();
        Uri uri;
        String title;
        for (int i = 0; i < cursor.getCount(); i++) {
            uri = manager.getRingtoneUri(i);
            context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            title = manager.getRingtone(i).getTitle(context);
            ringtones.add(new Ringtone(title, uri));
        }
        return ringtones;
    }

}
