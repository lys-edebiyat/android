package io.cordova.lysedebiyat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    static final String appId = "io.cordova.lysedebiyat";
    static final String appWebUrl = "https://lys-edebiyat.github.io/";

    // UI Elements
    TextView soru;
    Button[] buttonObjects;

    String data[][];
    int dataLength;
    String answer;
    String question;
    String era;

    MaterialStyledDialog.Builder dialog;

    private enum MenuItems {
        BACK_TO_HOME,
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
        soru = (TextView) findViewById(R.id.soru);

        dialog = new MaterialStyledDialog.Builder(this)
                .setStyle(Style.HEADER_WITH_TITLE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        createQuestion();
                    }
                });

        prepareQuestionData();
        prepareButtonBindings();
        createQuestion();
        prepareDrawer(this);
    }

    protected void prepareDrawer(Activity activity) {

        // Set the drawer items.
        final PrimaryDrawerItem backToHome = new PrimaryDrawerItem()
                .withIdentifier(MenuItems.BACK_TO_HOME.ordinal())
                .withName(R.string.back_to_game)
                .withIcon(GoogleMaterial.Icon.gmd_home);

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
                .withIcon(GoogleMaterial.Icon.gmd_rate_review);

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
                        eraList,
                        authorList,
                        rateOnStore,
                        share,
                        openInWeb,
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
                            case ERA_LIST:
                                navigateToActivity(EraList.class);
                                break;
                            case AUTHOR_LIST:
                                navigateToActivity(AuthorListActivity.class);
                                break;
                            case RATE_ON_STORE:
                                navigateToStore();
                                break;
                            case OPEN_IN_WEB:
                                navigateToWeb(appWebUrl);
                                break;
                            case SHARE:
                                shareIt();
                                break;
                            case ABOUT_US:
                                navigateToActivity(AboutActivity.class);
                                break;
                            case CONTACT:
                                sendMail();
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

    private void sendMail() {

        String deviceInfo = "Sistem bilgilerimi de buraya bırakıyorum:\n";
        deviceInfo += "OS: " + System.getProperty("os.version") + "\n";
        deviceInfo += "SDK Version: " + Build.VERSION.SDK_INT + "\n";
        deviceInfo += "Device: " + Build.DEVICE + "\n";
        deviceInfo += "Model: " + Build.MODEL + "\n";
        deviceInfo += "Product: " + Build.PRODUCT + "\n";

        String mailContent = "Merhaba. Oyuna dair şöyle bir bildirim sağlamak istiyorum:\n\n\n\n";
        mailContent += deviceInfo;

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"burak.karakan@gmail.com", "frknhatipoglu@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "LYS Edebiyat İletişim");
        i.putExtra(Intent.EXTRA_TEXT, mailContent);
        try {
            startActivity(Intent.createChooser(i, "Mail Gönder"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Yüklenmiş bir e-mail programı bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + appId));
        if (!storeNavigationSucceeded(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser.
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + appId));
            if (!storeNavigationSucceeded(intent)) {
                Toast.makeText(this, R.string.play_store_not_opened, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean storeNavigationSucceeded(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private void navigateToActivity(Class className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }


    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "LYS Edebiyat Yazar - Eser");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "LYS Edebiyat Yazar - Eser Bilgi Yarışması'nı hemen indir!");
        startActivity(Intent.createChooser(sharingIntent, "Şununla paylaş"));
    }

    @Override
    public void onClick(View v) {
        Button clicked = (Button) v;
        String selectedAnswer = clicked.getText().toString();
        if (answer.equals(selectedAnswer)) {
            this.displaySuccess();
        } else {
            this.displayFailure();
        }
    }

    /**
     * Set on click listeners for the answer buttons.
     */
    private void prepareButtonBindings() {
        buttonObjects = new Button[3];
        buttonObjects[0] = (Button) findViewById(R.id.cevap1);
        buttonObjects[0].setOnClickListener(this);

        buttonObjects[1] = (Button) findViewById(R.id.cevap2);
        buttonObjects[1].setOnClickListener(this);

        buttonObjects[2] = (Button) findViewById(R.id.cevap3);
        buttonObjects[2].setOnClickListener(this);

        ImageView img = (ImageView) findViewById(R.id.menuIcon);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.openDrawer();
            }
        });
    }

    /**
     * Perform the SQL query and gather the formatted data.
     */
    private void prepareQuestionData() {
        Cursor cursor = myDbHelper.query("" +
                "SELECT yazarlar.yazar, eserler.eser, donemler.donem " +
                "FROM eserler " +
                "INNER JOIN yazarlar ON eserler.yazar_id=yazarlar._id " +
                "INNER JOIN donemler ON yazarlar.donem_id=donemler._id " +
                "ORDER BY eserler.eser ASC;");

        this.data = new String[cursor.getCount()][3];
        int i = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            this.data[i][0] = cursor.getString(0);
            this.data[i][1] = cursor.getString(1);
            this.data[i][2] = cursor.getString(2);
            i++;
            cursor.moveToNext();
        }
        dataLength = data.length;
        cursor.close();
    }

    /**
     * Create a question based on the dataset.
     */
    private void createQuestion() {
        String buttonText;
        Random r = new Random();
        int randomIndex = r.nextInt(dataLength);
        int answerIndex = r.nextInt(3);

        this.answer = this.data[randomIndex][0];
        this.question = this.data[randomIndex][1];
        this.era = this.data[randomIndex][2];

        // Set the question.
        soru.setText(question);

        Map<String, Integer> seenChoices = new HashMap<String, Integer>();
        seenChoices.put(question, 1);

        for (int count = 0; count < 3; count++) {
            randomIndex = r.nextInt(dataLength);
            Button button = buttonObjects[count];
            buttonText = this.data[randomIndex][0];
            while (seenChoices.get(buttonText) != null) {
                randomIndex = r.nextInt(dataLength);
                buttonText = this.data[randomIndex][0];
            }
            seenChoices.put(buttonText, 1);
            button.setText(buttonText);
        }

        Button button = buttonObjects[answerIndex];
        button.setText(this.answer);
    }

    /**
     * Display failure message.
     */
    private void displayFailure() {
        showMessage(R.color.dialogHeaderWrong, "Yanlış cevap :(", getDialogMessage());
    }

    /**
     * Display success message.
     */
    private void displaySuccess() {
        showMessage(R.color.dialogHeaderCorrect, "Doğru cevap!", getDialogMessage());
    }

    /**
     * Get the message for the failure and success dialogs.
     *
     * @return
     */
    private String getDialogMessage() {
        String msg = "<b>" + this.question + "</b> adlı eser <b>" + this.answer + "</b> tarafından yazılmıştır.<br><br> <b>";
        if (!this.era.equals("Bağımsız")) {
            return msg + this.answer + "</b> bir <b>" + this.era + "</b> edebiyatı yazarıdır.";
        } else if (!this.era.equals("Divan Edebiyatı") &&
                !this.era.equals("Fecr-i Ati Edebiyatı") &&
                !this.era.equals("Halk Edebiyatı")) {
            return msg + this.answer + "</b> bir <b>" + this.era + "</b> yazarıdır.";
        } else {
            return msg + this.answer + "</b> bağımsız bir yazardır.";
        }
    }

    /**
     * Show given message with a pretty material dialog.
     *
     * @param headerColor - Color of the header.
     * @param title       - Title of the dialog.
     * @param msg         - Message body of the dialog.
     */
    private void showMessage(int headerColor, String title, String msg) {
        TextView tv = new TextView(this);
        tv.setText(fromHtml(msg));

        dialog.setHeaderColor(headerColor)
                .setTitle(title)
                .setCustomView(tv, 20, 20, 20, 20)
                .setPositiveText(R.string.sweet_alert_next_question)
                .show();
    }

    /**
     * Display a simple toast.
     *
     * @param text
     */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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
