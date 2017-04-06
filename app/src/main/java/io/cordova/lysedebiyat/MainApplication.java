package io.cordova.lysedebiyat;

import android.app.Application;
import android.database.SQLException;

import java.io.IOException;

import io.cordova.lysedebiyat.DatabaseHelpers.DataBaseHelper;

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
    }

    protected void prepareDatabase() {
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Veritabanı yaratılamadı.");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw new Error("Veritabanı açılamadı: " + sqle.getMessage());
        }
    }

    public DataBaseHelper getMyDbHelper() {
        return myDbHelper;
    }

}