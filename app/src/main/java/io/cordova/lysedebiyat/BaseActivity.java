package io.cordova.lysedebiyat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final int[] buttons = {
            R.id.cevap1,
            R.id.cevap2,
            R.id.cevap3
    };

    Drawer menu;

    DataBaseHelper myDbHelper;

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


}
