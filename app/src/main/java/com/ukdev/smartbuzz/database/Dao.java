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

    /**
     * Deletes an instance of {@link Alarm}
     * @param alarm the alarm to delete
     * @return {@code true} if the operation
     *         has been successful, otherwise
     *         {@code false}
     */
    public abstract boolean delete(Alarm alarm);

    /**
     * Inserts a new instance of {@link Alarm}
     * @param alarm the alarm to insert
     * @return the ID of the newly created alarm
     */
    public abstract long insert(Alarm alarm);

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
     * @return {@code true} if the operation
     *         has been successful, otherwise
     *         {@code false}
     */
    public abstract boolean update(Alarm alarm);

}
