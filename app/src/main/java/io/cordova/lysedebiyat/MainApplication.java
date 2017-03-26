package io.cordova.lysedebiyat;

import android.app.Application;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import java.io.IOException;

public class MainApplication extends Application {

    DataBaseHelper myDbHelper;

    public MainApplication() {
        // Nothing yet.
    }

    @Override
    public void onCreate() {
        // Prepare the database.
        myDbHelper = new DataBaseHelper(this);
        prepareDatabase();
        Log.d("LYS", "Inst bitti");
    }

    protected void prepareDatabase() {
        Log.d("LYS", "YINE GELDİ GELDİ GELDİ");
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw new Error("DB could not be opened: " + sqle.getMessage());
        }
    }

    public DataBaseHelper getMyDbHelper() {
        return myDbHelper;
    }

}