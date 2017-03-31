package io.cordova.lysedebiyat.SliderHelper;

import android.content.Intent;

import io.cordova.lysedebiyat.BaseActivity;
import io.cordova.lysedebiyat.R;

public abstract class SlidingBaseWithNoBackActivity extends BaseActivity {


    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}
