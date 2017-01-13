package com.ukdev.smartbuzz.model;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Ringtone wrapper
 * Represents a ringtone with its URI and title
 * Created by Alan Camargo - May 2016
 */
public class RingtoneWrapper
{

    private Uri uri;
    private String title;

    /**
     * Instantiates the class
     *
     * @param uri   - the ringtone URI
     * @param title - the ringtone title
     */
    public RingtoneWrapper(Uri uri, String title)
    {
        this.uri = uri;
        this.title = title;
    }

    /**
     * Gets all ringtones
     *
     * @param context - Context
     * @return ringtones
     */
    public static ArrayList<RingtoneWrapper> getAllRingtones(Context context)
    {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        ArrayList<RingtoneWrapper> ringtones = new ArrayList<>();
        Uri uri;
        String title;
        for (int i = 0; i < cursor.getCount(); i++)
        {
            uri = manager.getRingtoneUri(i);
            title = manager.getRingtone(i).getTitle(context);
            ringtones.add(new RingtoneWrapper(uri, title));
        }
        return ringtones;
    }

    /**
     * Gets the ringtone as a String
     */
    @Override
    public String toString()
    {
        return title;
    }

    /**
     * Gets the URI
     *
     * @return uri
     */
    public Uri getUri()
    {
        return uri;
    }

    /**
     * Gets the title
     *
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

}
