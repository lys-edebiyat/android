package io.cordova.lysedebiyat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    String data[][];
    String answer;
    String question;
    String era;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        prepareDrawer(this);
        prepareQuestionData();
        createQuestion();
        prepareButtonBindings();

    }

    protected void prepareDrawer(Activity activity) {

        // Set the drawer items.
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.game);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.author_book_list_title);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withDividerBelowHeader(true)
                .build();

        // Create the drawer and remember the `Drawer` result object
        menu = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(view.getId() == 2) {
                            navigateToList();
                        } else if(view.getId() == 3) {
                            menu.closeDrawer();
                        }
                        return true;
                    }
                })
                .build();
    }

    private void navigateToList() {
        Intent intent = new Intent(this, DonemListe.class);
        startActivity(intent);
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
        Button mClickButton1 = (Button) findViewById(R.id.cevap1);
        mClickButton1.setOnClickListener(this);
        Button mClickButton2 = (Button) findViewById(R.id.cevap2);
        mClickButton2.setOnClickListener(this);
        Button mClickButton3 = (Button) findViewById(R.id.cevap3);
        mClickButton3.setOnClickListener(this);
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
    }

    /**
     * Create a question based on the dataset.
     */
    private void createQuestion() {
        String buttonText;
        Random r = new Random();
        int dataLength = this.data.length;
        int randomIndex = r.nextInt(dataLength);
        int answerIndex = r.nextInt(3);

        this.answer = this.data[randomIndex][0];
        this.question = this.data[randomIndex][1];
        this.era = this.data[randomIndex][2];

        // Set the question.
        TextView soru = (TextView) findViewById(R.id.soru);
        soru.setText(question);

        Map<String, Integer> seenChoices = new HashMap<String, Integer>();
        seenChoices.put(question, 0);

        for (int count = 0; count < 3; count++) {
            randomIndex = r.nextInt(dataLength);
            Button button = (Button) findViewById(buttons[count]);
            buttonText = this.data[randomIndex][0];
            while (seenChoices.get(buttonText) != null) {
                randomIndex = r.nextInt(dataLength);
                buttonText = this.data[randomIndex][0];
            }
            seenChoices.put(buttonText, 0);
            button.setText(buttonText);
        }

        Button button = (Button) findViewById(buttons[answerIndex]);
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
        return "<b>" + this.question + "</b> adlı eser <b>" + this.answer + "</b> tarafından yazılmıştır. <b>" +
                this.answer + "</b> bir <b>" + this.era + "</b> edebiyatı yazarıdır.";
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
        new MaterialStyledDialog.Builder(this)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderColor(headerColor)
                .setTitle(title)
                .setCustomView(tv, 20, 20, 20, 20)
                .setPositiveText(R.string.sweet_alert_next_question)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        createQuestion();
                    }
                })
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
