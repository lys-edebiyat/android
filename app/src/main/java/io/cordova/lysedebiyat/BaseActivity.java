package io.cordova.lysedebiyat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
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


    protected void navigateToActivity(Class to, String stringToPass) {
        Intent intent = new Intent(this, to);

        // Put the data to the bundle.
        Bundle b = new Bundle();
        b.putString("data", stringToPass);
        intent.putExtras(b);

        // Start the activity and finish current one.
        startActivity(intent);
    }

    protected void navigateToActivity(Context from, Class to) {
        Intent intent = new Intent(from, to);
        startActivity(intent);
        finish();
    }

    protected void constructToolbar(String title){
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(title);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
