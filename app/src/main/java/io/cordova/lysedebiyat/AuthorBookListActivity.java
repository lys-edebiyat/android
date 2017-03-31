package io.cordova.lysedebiyat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.astuetz.PagerSlidingTabStrip;

import io.cordova.lysedebiyat.Adapters.AlphabeticStickyListAdapter;
import io.cordova.lysedebiyat.SliderHelper.SlidingBaseActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AuthorBookListActivity extends SlidingBaseActivity {

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_book_list);

        // Construct the data array.
        String authorToQuery = getIntent().getExtras().getString("data");
        constructData(authorToQuery);

        // Set the toolbar title.
        setToolbarTitle(this, authorToQuery);
        setView();

    }
    private void setView() {
        final StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        AlphabeticStickyListAdapter adapter = new AlphabeticStickyListAdapter(this, data);
        stickyList.setAdapter(adapter);
    }

    private String constructQuery(String authorToQuery) {
        return "SELECT eser FROM eserler " +
                "JOIN yazarlar ON eserler.yazar_id=yazarlar._id " +
                "WHERE yazar='" + authorToQuery + "' " +
                "ORDER BY eser ASC;";
    }

    private void constructData(String eraToQuery) {

        // Construct and execute the query.
        String query = constructQuery(eraToQuery);
        Cursor cursor = myDbHelper.query(query);

        // Create the data array.
        data = new String[cursor.getCount()];
        int i = 0;

        // Fill the data array.
        while (cursor.moveToNext()) {
            data[i] = cursor.getString(0);
            i++;
        }

        cursor.close();
    }
}
