package com.ukdev.smartbuzz.database;

import android.annotation.SuppressLint;
import android.content.Context;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.ArrayList;

public class AlarmRepository extends BaseRepository {

    private static final String TABLE_NAME = "ALARMS";

    @SuppressLint("StaticFieldLeak")
    private static AlarmRepository instance;

    public static AlarmRepository getInstance(Context context) {
        if (instance == null)
            instance = new AlarmRepository(context);
        return instance;
    }

    private AlarmRepository(Context context) {
        super(context);
    }

    @Override
    public void delete(Alarm alarm) {

    }

    @Override
    public void insert(Alarm alarm) {

    }

    @Override
    public int getLastId() {
        return 0;
    }

    @Override
    public ArrayList<Alarm> select() {
        return null;
    }

    @Override
    public Alarm select(int id) {
        return null;
    }

    @Override
    public ArrayList<Alarm> select(Column[] columns, Object[] values) {
        return null;
    }

    @Override
    public void update(Alarm alarm) {

    }

}
