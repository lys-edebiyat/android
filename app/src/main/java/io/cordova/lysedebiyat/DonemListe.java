package io.cordova.lysedebiyat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DonemListe extends BaseActivity {

    String data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donem_liste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Yazar Eser Listesi");
        setSupportActionBar(toolbar);

        prepareListData();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.list_singular, R.id.label, this.data);

        final ListView listView = (ListView) findViewById(R.id.yazar_eser_liste);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String era = (String) listView.getItemAtPosition(position);
                navToAct(YazarEserListe.class, era);
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

    private void prepareListData() {
        Cursor cursor = myDbHelper.query("Select donem from donemler order by donem ASC;");
        data = new String[cursor.getCount()];
        int i = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
    }
}
