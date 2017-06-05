package com.ukdev.smartbuzz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.ukdev.smartbuzz.model.Alarm;

import java.util.List;

/**
 * Base data access object
 *
 * @author Alan Camargo
 */
abstract class BaseDao {

    SQLiteDatabase reader;
    SQLiteDatabase writer;
    Context context;

    /**
     * Default constructor for {@code BaseDao}
     * @param context the Android context
     */
    BaseDao(Context context) {
        this.context = context;
        DatabaseHelper helper = new DatabaseHelper(context);
        reader = helper.getReadableDatabase();
        writer = helper.getWritableDatabase();
    }

    /**
     * Deletes an instance of {@link Alarm}
     * @param alarm the alarm to delete
     */
    public abstract void delete(Alarm alarm);

    /**
     * Inserts a new instance of {@link Alarm}
     * @param alarm the alarm to insert
     */
    public abstract void insert(Alarm alarm);

    /**
     * Gets all active instances of {@link Alarm}.
     * Active can be understood as scheduled.
     * @see Alarm#isActive()
     * @return all active alarms
     */
    public abstract List<Alarm> getActiveAlarms();

    /**
     * Gets the last {@link Alarm} database ID
     * @return the last ID
     */
    public abstract int getLastId();

    /**
     * Gets all instances of {@link Alarm}
     * @return all alarms
     */
    public abstract List<Alarm> select();

    /**
     * Gets an instance of {@link Alarm} by
     * its database ID
     * @param id the ID
     * @return the alarm found
     */
    public abstract Alarm select(int id);

    /**
     * Updates an instance of {@link Alarm}
     * @param alarm the alarm to be updated
     */
    public abstract void update(Alarm alarm);

}
