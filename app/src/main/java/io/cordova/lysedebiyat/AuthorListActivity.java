package io.cordova.lysedebiyat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import io.cordova.lysedebiyat.Adapters.AlphabeticStickyListAdapter;
import io.cordova.lysedebiyat.SliderHelper.SlidingBaseWithNoBackActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AuthorListActivity extends SlidingBaseWithNoBackActivity {

    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_book_list);
        setToolbarTitle(this, "Yazarlar");

        prepareAuthorData();
        setView();

    }

    private void setView() {
        final StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        AlphabeticStickyListAdapter adapter = new AlphabeticStickyListAdapter(this, data);
        stickyList.setAdapter(adapter);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String author = (String) stickyList.getItemAtPosition(position);
                navToAct(AuthorBookListActivity.class, author);
            }
        });
    }

    protected void navToAct(Class to, String stringToPass) {
        Intent intent = new Intent(this, to);

        // Put the data to the bundle.
        Bundle b = new Bundle();
        b.putString("data", stringToPass);
        intent.putExtras(b);

        // Start the activity and finish current one.
        startActivity(intent);
    }


    private void prepareAuthorData() {
        Cursor cursor = myDbHelper.query("SELECT yazar FROM yazarlar ORDER BY yazar;");
        data = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            data[i] = cursor.getString(0);
            i++;
        }
        cursor.close();
    }
}
