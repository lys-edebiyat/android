package io.cordova.lysedebiyat.DatabaseHelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatsDatabaseHelper extends SQLiteOpenHelper {

    private static long timestamp;
    private static Integer correctCount;
    private static Integer wrongCount;
    private static SQLiteDatabase db;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserStats.db";

    public static final String TABLE_NAME = "stats";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_CORRECT_ANSWERS = "correct_answers";
    public static final String COLUMN_NAME_WRONG_ANSWERS = "wrong_answers";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_TIMESTAMP + " INTEGER PRIMARY KEY," +
            COLUMN_NAME_CORRECT_ANSWERS + " INTEGER," +
            COLUMN_NAME_WRONG_ANSWERS + " INTEGER)";

    public StatsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void saveLastScores(long timestampToAdd, int correctAnswers, int wrongAnswers) {
        timestamp = timestampToAdd;
        correctCount = correctAnswers;
        wrongCount = wrongAnswers;

        new Thread(new Runnable() {
            @Override
            public void run() {
                StatsDatabaseHelper.insertScores();
            }
        }).start();

    }

    private static void insertScores() {
        String query = "INSERT OR REPLACE INTO " + TABLE_NAME +
                "(" + COLUMN_NAME_TIMESTAMP + "," +
                COLUMN_NAME_CORRECT_ANSWERS + "," +
                COLUMN_NAME_WRONG_ANSWERS +
                ") VALUES (" + timestamp + "," + correctCount + "," + wrongCount + ");";
        db.execSQL(query);
    }

    public static int getCount() {
        String query = "Select count(*) from " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int a = c.getInt(0);
        c.close();
        return a;
    }
}
