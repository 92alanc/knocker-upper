package com.ukdev.smartbuzz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

abstract class BaseRepository {

    SQLiteDatabase reader;
    SQLiteDatabase writer;
    Context context;

    BaseRepository(Context context) {
        this.context = context;
        DatabaseHelper helper = new DatabaseHelper(context);
        reader = helper.getReadableDatabase();
        writer = helper.getWritableDatabase();
    }

    public abstract void delete(Alarm alarm);

    public abstract void insert(Alarm alarm);

    public abstract ArrayList<Alarm> getActiveAlarms();

    public abstract int getLastId();

    public abstract ArrayList<Alarm> select();

    public abstract Alarm select(int id);

    public abstract void update(Alarm alarm);

}
