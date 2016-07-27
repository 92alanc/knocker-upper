package com.ukdev.smartbuzz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Snooze counter
 * Counts the number of times the snooze button
 * has been pressed
 * Created by Alan Camargo - July 2016
 */
public class SnoozeCounter extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "snooze_counter.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SNOOZE_COUNTER";
    private static final String COLUMN_NAME = "SNOOZE_COUNT";
    private static final String SQL_CREATE_TABLE =
            String.format("CREATE TABLE %1$s (%2$s INTEGER)",
                    TABLE_NAME, COLUMN_NAME);
    private static final String SQL_DROP_TABLE =
            String.format("DROP TABLE IF EXISTS %1$s", TABLE_NAME);

    /**
     * Initialises the database
     * @param context - Context
     */
    public SnoozeCounter(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    /**
     * Gets the number of times the snooze button has been pressed
     */
    public int getCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        int count = 0;
        cursor.moveToNext();
        if (cursor.getCount() > 0)
            count = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME));
        else
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, 0);
            db.insert(TABLE_NAME, null, values);
        }
        cursor.close();
        return count;
    }

    /**
     * Updates the count
     * @param n - int
     */
    public void update(int n)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (n <= 3)
            values.put(COLUMN_NAME, n);
        else
            values.put(COLUMN_NAME, 0);
        db.update(TABLE_NAME, values, null, null);
        db.close();
    }

    /**
     * Resets the counter
     */
    public void reset()
    {
        update(0);
    }

}
