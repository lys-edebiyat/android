package io.cordova.lysedebiyat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;

import io.cordova.lysedebiyat.DatabaseHelpers.DataBaseHelper;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final int[] buttons = {
            R.id.cevap1,
            R.id.cevap2,
            R.id.cevap3
    };

    protected Drawer menu;

    protected DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        MainApplication ma = (MainApplication) getApplication();
        myDbHelper = ma.getMyDbHelper();
    }

    protected void navigateToWeb(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    protected void setToolbarTitle(Activity v, String title) {
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }
}
