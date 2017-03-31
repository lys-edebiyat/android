package io.cordova.lysedebiyat.SliderHelper;

import io.cordova.lysedebiyat.R;

public abstract class SlidingBaseActivity extends SlidingBaseWithNoBackActivity {

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
