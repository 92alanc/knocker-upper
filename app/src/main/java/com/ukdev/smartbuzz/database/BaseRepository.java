package com.ukdev.smartbuzz.database;

import android.content.Context;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

abstract class BaseRepository {

    Context context;
    DatabaseHelper helper;

    BaseRepository(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
    }

    public abstract void delete(Alarm alarm);

    public abstract void insert(Alarm alarm);

    public abstract int getLastId();

    public abstract ArrayList<Alarm> select();

    public abstract Alarm select(int id);

    public abstract ArrayList<Alarm> select(Column[] columns, Object[] values);

    public abstract void update(Alarm alarm);

}
