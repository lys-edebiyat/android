package io.cordova.lysedebiyat;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class YazarEserListe extends BaseActivity {

    private String[][] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yazar_eser_liste);

        // Construct the data array.
        String eraToQuery = getIntent().getExtras().getString("data");
        constructData(eraToQuery);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(eraToQuery);
        setSupportActionBar(tb);

        StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.yazar_eser_list);
        YazarEserAdapter adapter = new YazarEserAdapter(this, data);
        stickyList.setAdapter(adapter);
    }

    private String constructQuery(String eraToQuery) {

        String query = "SELECT yazar, eser FROM eserler " +
                "JOIN yazarlar ON eserler.yazar_id=yazarlar._id " +
                "JOIN donemler ON yazarlar.donem_id=donemler._id " +
                "WHERE donem='" + eraToQuery + "' " +
                "ORDER BY yazar ASC, eser ASC;";
        return query;
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
}
