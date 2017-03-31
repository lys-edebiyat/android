package io.cordova.lysedebiyat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.cordova.lysedebiyat.SliderHelper.SlidingBaseWithNoBackActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AuthorListActivity extends SlidingBaseWithNoBackActivity {

    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);
        setToolbarTitle(this, "Yazarlar");

        prepareAuthorData();
        setView();

    }

    private void setView() {

        StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        AuthorBookAdapter adapter = new AuthorBookAdapter(this, data);
        stickyList.setAdapter(adapter);
    }


    private void prepareAuthorData() {
        Cursor cursor = myDbHelper.query("SELECT yazar FROM yazarlar ORDER BY yazar");
        data = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            data[i] = cursor.getString(0);
            i++;
        }
        cursor.close();
    }
}
