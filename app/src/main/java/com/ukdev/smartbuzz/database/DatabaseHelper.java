package com.ukdev.smartbuzz.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.ukdev.smartbuzz.misc.LogTool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A helper to access SQLite databases
 *
 * @author Alan Camargo
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_PATH = "/data/data/com.ukdev.smartbuzz/databases/";
    private static final String DB_NAME = "database.db";
    private SQLiteDatabase database;
    private Context context;

    /**
     * Default constructor for {@code DatabaseHelper}
     * @param context the Android context
     */
    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        LogTool log = new LogTool(context);
        try {
            createDatabase();
            openDatabase();
        } catch (IOException e) {
            log.exception(e);
        }
    }

    private void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();
        if (!dbExists) {
            getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase db = null;
        try {
            String path = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (db != null)
            db.close();
        return db != null;
    }

    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024 * 50];
        int length;
        while ((length = input.read(buffer)) > 0)
            output.write(buffer, 0, length);

        output.flush();
        output.close();
        input.close();
    }

    private void openDatabase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}