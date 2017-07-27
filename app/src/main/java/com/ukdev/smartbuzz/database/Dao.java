package com.ukdev.smartbuzz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Base data access object
 *
 * @author Alan Camargo
 */
abstract class Dao {

    private DatabaseHelper helper;

    SQLiteDatabase reader;
    SQLiteDatabase writer;
    Context context;

    /**
     * Default constructor for {@code Dao}
     * @param context the Android context
     */
    Dao(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        openDatabase();
    }

    /**
     * Opens the database
     */
    void openDatabase() {
        reader = helper.getReadableDatabase();
        writer = helper.getWritableDatabase();
    }

}
