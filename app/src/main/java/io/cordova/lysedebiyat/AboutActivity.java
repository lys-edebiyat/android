package io.cordova.lysedebiyat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(R.string.about_us_title);

        // Set the button click listener.
        Button githubButton = (Button) findViewById(R.id.details_button);
        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToWeb(getResources().getString(R.string.github_url));
            }
        });

    }
}
