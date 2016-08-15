package com.ukdev.smartbuzz.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import com.ukdev.smartbuzz.extras.AppConstants;

import java.io.File;
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
     * @param uri - the ringtone URI
     * @param title - the ringtone title
     */
    public RingtoneWrapper(Uri uri, String title)
    {
        this.uri = uri;
        this.title = title;
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
     * @return uri
     */
    public Uri getUri()
    {
        return uri;
    }

    /**
     * Gets the title
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Tells whether the ringtone is playable
     * Note: a ringtone is considered as playable
     * if its size in bytes is greater than zero
     * @return true if the ringtone is playable
     */
    public boolean isPlayable()
    {
        File ringtoneFile = new File(uri.toString());
        return ringtoneFile.length() < 0;
    }

    /**
     * Gets all ringtones
     * @param context - Context
     * @return ringtones
     */
    public static ArrayList<RingtoneWrapper> getAllRingtones(Context context)
    {
        // First we'll add all alarm audio file to the list
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        ArrayList<RingtoneWrapper> ringtones = new ArrayList<>();
        String title;
        Uri uri;
        for (int i = 0; i < cursor.getCount(); i++)
        {
            uri = manager.getRingtoneUri(i);
            title = manager.getRingtone(i).getTitle(context);
            ringtones.add(new RingtoneWrapper(uri, title));
        }
        int permission = ContextCompat.checkSelfPermission(context,
                AppConstants.PERMISSION_READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED)
        {
            // Now we'll add all music files to the list as well
            ContentResolver resolver = context.getContentResolver();
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = resolver.query(uri, null, null, null, null);
            if(cursor != null && cursor.moveToFirst())
            {
                int pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int titleColumn = cursor.getColumnIndex
                        (MediaStore.Audio.Media.TITLE);
                do
                {
                    Uri songUri = Uri.parse(cursor.getString(pathColumn));
                    String songTitle = cursor.getString(titleColumn);
                    ringtones.add(new RingtoneWrapper(songUri, songTitle));
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        }
        return ringtones;
    }

}
