package io.cordova.lysedebiyat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashSet;

import io.cordova.lysedebiyat.DatabaseHelpers.StatsDatabaseHelper;
import io.cordova.lysedebiyat.Helpers.NavigationHelper;
import io.cordova.lysedebiyat.Helpers.QuestionHelper;

public class MainActivity extends BaseActivity {

    static final String APP_WEB_URL = "https://lys-edebiyat.github.io/";

    // Stats counters
    protected int correctCount = 0;
    protected int wrongCount = 0;

    QuestionHelper questionHelper;
    NavigationHelper navigationHelper;

    String eraList[];
    HashSet<String> selectedEras;
    StatsDatabaseHelper statDb;
    long unixTime = System.currentTimeMillis() / 1000L;

    boolean doubleBackToExitPressedOnce = false;

    private enum MenuItems {
        BACK_TO_HOME,
        ERA_SELECTION,
        ERA_LIST,
        AUTHOR_LIST,
        OPEN_IN_WEB,
        SHARE,
        RATE_ON_STORE,
        ABOUT_US,
        CONTACT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statDb = new StatsDatabaseHelper(this);

        questionHelper = new QuestionHelper(this, myDbHelper);
        navigationHelper = new NavigationHelper(this);

        prepareButtonBindings();
        prepareDrawer(this);
    }

    protected void onPause() {
        super.onPause();
        statDb.saveLastScores(unixTime, correctCount, wrongCount);
    }

    protected void prepareDrawer(Activity activity) {

        // Set the drawer items.
        PrimaryDrawerItem backToHome = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.BACK_TO_HOME.ordinal())
                .withName(R.string.back_to_game)
                .withIcon(GoogleMaterial.Icon.gmd_home);

        // Set the drawer items.
        PrimaryDrawerItem eraSelection = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.ERA_SELECTION.ordinal())
                .withName(R.string.era_selection_title)
                .withIcon(GoogleMaterial.Icon.gmd_check_box);

        PrimaryDrawerItem eraList = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.ERA_LIST.ordinal())
                .withName(R.string.era_list_title)
                .withIcon(GoogleMaterial.Icon.gmd_list);

        PrimaryDrawerItem authorList = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.AUTHOR_LIST.ordinal())
                .withName(R.string.author_list_title)
                .withIcon(GoogleMaterial.Icon.gmd_book);

        PrimaryDrawerItem rateOnStore = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.RATE_ON_STORE.ordinal())
                .withName(R.string.rate_on_store)
                .withIcon(GoogleMaterial.Icon.gmd_star_border);

        PrimaryDrawerItem openInWeb = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.OPEN_IN_WEB.ordinal())
                .withName(R.string.open_in_web)
                .withIcon(GoogleMaterial.Icon.gmd_open_in_browser);

        PrimaryDrawerItem share = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.SHARE.ordinal())
                .withName(R.string.share)
                .withIcon(GoogleMaterial.Icon.gmd_share);

        PrimaryDrawerItem aboutUs = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.ABOUT_US.ordinal())
                .withName(R.string.about_us)
                .withIcon(GoogleMaterial.Icon.gmd_info_outline);

        PrimaryDrawerItem contact = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.CONTACT.ordinal())
                .withName(R.string.contact_us)
                .withIcon(GoogleMaterial.Icon.gmd_mail_outline);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.icon)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .withDividerBelowHeader(true)
                .build();


        // Create the drawer and remember the `Drawer` result object
        menu = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        backToHome,
                        eraSelection,
                        eraList,
                        authorList,
                        new DividerDrawerItem(),
                        rateOnStore,
                        share,
                        openInWeb,
                        new DividerDrawerItem(),
                        aboutUs,
                        contact
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        MenuItems[] m = MenuItems.values();
                        switch (m[view.getId()]) {
                            case BACK_TO_HOME:
                                menu.closeDrawer();
                                break;
                            case ERA_SELECTION:
                                questionHelper.createEraSelectionDialog();
                                break;
                            case ERA_LIST:
                                navigationHelper.navigateToActivity(EraListActivity.class);
                                break;
                            case AUTHOR_LIST:
                                navigationHelper.navigateToActivity(AuthorListActivity.class);
                                break;
                            case RATE_ON_STORE:
                                navigationHelper.navigateToStore();
                                break;
                            case OPEN_IN_WEB:
                                navigateToWeb(APP_WEB_URL);
                                break;
                            case SHARE:
                                navigationHelper.shareIt();
                                break;
                            case ABOUT_US:
                                navigationHelper.navigateToActivity(AboutActivity.class);
                                break;
                            case CONTACT:
                                navigationHelper.sendMail();
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }


    /**
     * Exit from the application if back is pressed two times in 2 seconds.
     */
    @Override
    public void onBackPressed() {
        if (menu.isDrawerOpen()) {
            menu.closeDrawer();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            showToast("Çıkmak için tekrar geri tuşuna basın.");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    /**
     * Set on click listeners for the answer buttons.
     */
    private void prepareButtonBindings() {
        ImageView img = (ImageView) findViewById(R.id.menuIcon);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.openDrawer();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
