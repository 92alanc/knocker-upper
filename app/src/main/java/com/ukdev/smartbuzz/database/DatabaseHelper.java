package com.ukdev.smartbuzz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ukdev.smartbuzz.database.AlarmTable.TABLE_NAME;

/**
 * DatabaseHelper class
 * Controls the database
 * Created by Alan Camargo - April 2016
 */
class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "alarms.db";

    /**
     * Instantiates the class
     * @param context - Context
     */
    DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(AlarmTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int n)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
