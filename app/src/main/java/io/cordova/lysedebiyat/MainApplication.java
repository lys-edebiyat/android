package io.cordova.lysedebiyat;

import android.app.Application;
import android.database.SQLException;

import com.google.android.gms.ads.MobileAds;

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

        String admobAppId = getResources().getString(R.string.admob_app_id);
        MobileAds.initialize(this, admobAppId);
    }

    protected void prepareDatabase() {

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