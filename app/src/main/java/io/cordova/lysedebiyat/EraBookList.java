package io.cordova.lysedebiyat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;

import io.cordova.lysedebiyat.SliderHelper.SlidingBaseActivity;

public class EraBookList extends SlidingBaseActivity {

    private String[][] data;
    private EraInfoActivity eraInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donem_sliding_tabs);

        // Construct the data array.
        String eraToQuery = getIntent().getExtras().getString("data");
        constructData(eraToQuery);
        constructEraInfo(eraToQuery);

        // Set the toolbar title to the era name.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(eraToQuery);
        setSupportActionBar(tb);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), data, eraInfo));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    private String constructQuery(String eraToQuery) {
        return "SELECT yazar, eser FROM eserler " +
                "JOIN yazarlar ON eserler.yazar_id=yazarlar._id " +
                "JOIN donemler ON yazarlar.donem_id=donemler._id " +
                "WHERE donem='" + eraToQuery + "' " +
                "ORDER BY yazar ASC, eser ASC;";
    }

    private String constructEraInfoQuery(String eraToQuery) {
        return "SELECT info, yazarlar, link FROM donemler_info " +
                "JOIN donemler ON donem_id=_id " +
                "WHERE donem='" + eraToQuery + "';";
    }

    private void constructData(String eraToQuery) {

        // Construct and execute the query.
        String query = constructQuery(eraToQuery);
        Cursor cursor = myDbHelper.query(query);

        // Create the data array.
        data = new String[cursor.getCount()][3];
        int i = 0;

        // Fill the data array.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data[i][0] = cursor.getString(0);
            data[i][1] = cursor.getString(1);
            i++;
            cursor.moveToNext();
        }
    }

    private void constructEraInfo(String eraToQuery) {

        // Construct and execute the query.
        String query = constructEraInfoQuery(eraToQuery);
        Cursor cursor = myDbHelper.query(query);

        // Fill the data array.
        cursor.moveToFirst();

        String info = cursor.getString(0);
        String authors = cursor.getString(1);
        String link = cursor.getString(2);
        eraInfo = new EraInfoActivity(info, authors, link);
        cursor.close();
    }
}
