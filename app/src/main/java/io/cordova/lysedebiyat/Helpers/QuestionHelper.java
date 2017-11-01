package io.cordova.lysedebiyat.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import io.cordova.lysedebiyat.DatabaseHelpers.DataBaseHelper;
import io.cordova.lysedebiyat.R;

public class QuestionHelper {

    private static final String PREFS_NAME = "LYSPrefs";
    private static final String ERA_LIST_KEY = "selectedEras";

    private FirebaseAnalytics mFirebaseAnalytics;

    protected Context context;

    // Stats counters
    private int correctCount = 0;
    private int wrongCount = 0;
    private Boolean isCorrectKnown = false;

    // Data properties
    private String data[][];
    private String answer;
    private String question;
    private String era;
    private String eraList[];
    private HashSet<String> selectedEras;

    // UI Elements
    private TextView correctAnswer;
    private TextView wrongAnswer;
    private TextView soru;
    private Button[] buttonObjects;

    // Additional accessor objects.
    private DataBaseHelper myDbHelper;
    private SharedPreferences settings;

    private InterstitialAd mInterstitialAd;

    private Integer questionsSinceLastAd = 0;

    // OnClickListener for the answer buttons.
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button clicked = (Button) v;
            String selectedAnswer = clicked.getText().toString();

            if (answer.equals(selectedAnswer)) {
                displaySuccess();
                logAnalyticsEvent("Correct");
            } else {
                displayFailure();
                logAnalyticsEvent("Wrong");
            }

            Random r = new Random();
            int randomNum = r.nextInt(10);
            if (randomNum <= 1 && mInterstitialAd.isLoaded() && questionsSinceLastAd > 5) {
                mInterstitialAd.show();
                questionsSinceLastAd = 0;
            }

            questionsSinceLastAd++;
        }
    };

    private MaterialStyledDialog.Builder dialog;

    /**
     * Constructor for the helper class.
     *
     * @param c   Activity context.
     * @param mdb Database helper.
     */
    public QuestionHelper(Context c, DataBaseHelper mdb) {
        context = c;
        myDbHelper = mdb;

        soru = (TextView) ((Activity) context).findViewById(R.id.soru);
        settings = context.getSharedPreferences(PREFS_NAME, 0);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        // Get the initial ad.
        final String testDeviceId = c.getResources().getString(R.string.admob_test_device_id);
        mInterstitialAd = new InterstitialAd(c);
        mInterstitialAd.setAdUnitId(c.getResources().getString(R.string.admob_interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(testDeviceId).build());

        // Set an AdListener.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(testDeviceId).build());
            }
        });

        initializeAnswerDialog();
        prepareButtonBindings();
        prepareQuestionData();
        initAnswerCounts();
        createQuestion();
    }

    /**
     * Create the answer dialog initially.
     */
    private void initializeAnswerDialog() {
        dialog = new MaterialStyledDialog.Builder(context)
                .setCancelable(false)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .setStyle(Style.HEADER_WITH_TITLE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (isCorrectKnown) {
                            correctAnswer.setText(context.getString(R.string.dialog_correct_answer, correctCount));
                        } else {
                            wrongAnswer.setText(context.getString(R.string.dialog_wrong_answer, wrongCount));
                        }
                        createQuestion();
                    }
                });
    }

    /**
     * Set on click listeners for the answer buttons.
     */
    private void prepareButtonBindings() {
        buttonObjects = new Button[3];
        buttonObjects[0] = (Button) ((Activity) context).findViewById(R.id.cevap1);
        buttonObjects[0].setOnClickListener(listener);

        buttonObjects[1] = (Button) ((Activity) context).findViewById(R.id.cevap2);
        buttonObjects[1].setOnClickListener(listener);

        buttonObjects[2] = (Button) ((Activity) context).findViewById(R.id.cevap3);
        buttonObjects[2].setOnClickListener(listener);
    }


    public void createEraSelectionDialog() {

        View dialogLayout = ((Activity) context).getLayoutInflater().inflate(R.layout.era_selection_dialog_view, null);
        ListView listview = (ListView) dialogLayout.findViewById(R.id.listView1);

        // set adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.era_selection_dialog_item, eraList);
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        for (int i = 0; i < eraList.length; i++) {
            if (selectedEras.contains(eraList[i])) {
                listview.setItemChecked(i, true);
            }
        }

        new MaterialStyledDialog.Builder(context)
                .setTitle(R.string.dialog_era_selection_title)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderColor(R.color.accent)
                .setCustomView(dialogLayout, 20, 20, 20, 20)
                .setPositiveText(R.string.dialog_era_selection_save)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveSharedPref(dialog);
                    }
                })
                .show();
    }


    /**
     * Save the shared preferences for the era list selection.
     *
     * @param dialog Era selection dialog
     */
    public void saveSharedPref(MaterialDialog dialog) {

        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        int count = lv.getAdapter().getCount();
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        Boolean noErasSelected = true;

        // Check if no eras are selected in the list.
        for (int i = 0; i < count; i++) {
            if (checked.get(i)) {
                noErasSelected = false;
                break;
            }
        }

        // Update the selected eras list.
        selectedEras.clear();
        for (int i = 0; i < count; i++) {
            if (noErasSelected || checked.get(i)) {
                selectedEras.add(eraList[i]);
            }
        }

        // Perform the SQL query and update the dataset.
        updateQuestionDataSet();

        // Create a new question based on the new dataset.
        createQuestion();

        // Save the era list settings.
        SharedPreferences.Editor edit = settings.edit();
        edit.remove(ERA_LIST_KEY);
        edit.apply();
        edit.putStringSet(ERA_LIST_KEY, selectedEras);
        edit.apply();

        // Display the appropriate message.
        if (noErasSelected) {
            showToast("Hiç dönem seçmediğiniz için tüm dönemler dahil edildi.");
        } else {
            showToast("Dönem seçiminiz başarıyla güncellendi.");
        }

    }

    /**
     * Perform the SQL query and gather the formatted data.
     */
    private void prepareQuestionData() {
        Cursor cursor = myDbHelper.query("SELECT donem FROM donemler ORDER BY donem ASC");
        this.eraList = new String[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            this.eraList[i] = cursor.getString(0);
            i++;
        }

        selectedEras = (HashSet<String>) settings.getStringSet(ERA_LIST_KEY, null);

        if (selectedEras == null) {
            selectedEras = new HashSet<String>(Arrays.asList(eraList));
        }

        updateQuestionDataSet();
        cursor.close();
    }

    /**
     * Iterate over the cursor and save the formatted data to an array.
     */
    private void updateQuestionDataSet() {
        Cursor cursor = myDbHelper.query(constructGameDataQuery());

        this.data = new String[cursor.getCount()][3];

        int i = 0;
        while (cursor.moveToNext()) {
            this.data[i][0] = cursor.getString(0);
            this.data[i][1] = cursor.getString(1);
            this.data[i][2] = cursor.getString(2);
            i++;
        }

        cursor.close();
    }

    /**
     * Construct the data selection query.
     *
     * @return Era selection query.
     */
    private String constructGameDataQuery() {
        String whereClause = this.queryFilterFromArray(selectedEras);
        return "SELECT yazarlar.yazar, eserler.eser, donemler.donem " +
                "FROM eserler " +
                "INNER JOIN yazarlar ON eserler.yazar_id=yazarlar._id " +
                "INNER JOIN donemler ON yazarlar.donem_id=donemler._id " +
                whereClause +
                " ORDER BY eserler.eser ASC;";
    }

    /**
     * Create the 'IN (x,y,z)' part of the query string from a string set.
     *
     * @param array The array to be imploded.
     * @return Where clause of the query.
     */
    private String queryFilterFromArray(HashSet<String> array) {
        String inClause = "WHERE donemler.donem IN (";

        for (String era : array) {
            inClause += "'" + era + "',";
        }

        inClause = inClause.substring(0, inClause.length() - 1);
        inClause += ")";

        return inClause;
    }

    /**
     * Initialize answer count views.
     */
    private void initAnswerCounts() {
        correctAnswer = (TextView) ((Activity) context).findViewById(R.id.correctAnswer);
        wrongAnswer = (TextView) ((Activity) context).findViewById(R.id.wrongAnswer);

        correctAnswer.setText(context.getString(R.string.dialog_correct_answer, correctCount));
        wrongAnswer.setText(context.getString(R.string.dialog_wrong_answer, wrongCount));
    }

    /**
     * Create a question based on the dataset.
     */
    private void createQuestion() {
        String buttonText;
        Random r = new Random();
        int randomIndex = r.nextInt(data.length);
        int answerIndex = -1;

        this.answer = this.data[randomIndex][0];
        this.question = this.data[randomIndex][1];
        this.era = this.data[randomIndex][2];

        // Set the question.
        soru.setText(question);

        HashMap<String, Integer> seenChoices = new HashMap<>();
        seenChoices.put(answer, 1);

        for (int i = 0; i < 3; i++) {
            randomIndex = r.nextInt(data.length);
            Button button = buttonObjects[i];
            buttonText = this.data[randomIndex][0];
            while (seenChoices.get(buttonText) != null) {
                if (buttonText.equals(answer) && answerIndex == -1) {
                    answerIndex = i;
                    break;
                }
                randomIndex = r.nextInt(data.length);
                buttonText = this.data[randomIndex][0];
            }
            seenChoices.put(buttonText, 1);
            button.setText(buttonText);
        }
        if (answerIndex == -1) {
            answerIndex = r.nextInt(3);
        }
        Button button = buttonObjects[answerIndex];
        button.setText(this.answer);
    }

    /**
     * Display failure message.
     */
    private void displayFailure() {
        wrongCount++;
        isCorrectKnown = false;
        showMessage(R.color.dialogHeaderWrong, "Yanlış cevap :(", getDialogMessage());
    }

    /**
     * Display success message.
     */
    private void displaySuccess() {
        correctCount++;
        isCorrectKnown = true;
        correctAnswer.setText(context.getString(R.string.dialog_correct_answer, correctCount));
        showMessage(R.color.dialogHeaderCorrect, "Doğru cevap!", getDialogMessage());
    }

    private void logAnalyticsEvent(String eventType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Question");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventType);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "click");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Get the message for the failure and success dialogs.
     *
     * @return
     */
    private String getDialogMessage() {
        String msg = "<b>" + this.question + "</b> adlı eser <b>" + this.answer + "</b> tarafından yazılmıştır.<br><br> <b>";
        if (this.era.equals("Bağımsız")) {
            return msg + this.answer + "</b> bağımsız bir yazardır.";
        } else if (this.era.equals("Divan Edebiyatı") ||
                this.era.equals("Fecr-i Ati Edebiyatı") ||
                this.era.equals("Halk Edebiyatı")) {
            return msg + this.answer + "</b> bir <b>" + this.era + "</b> yazarıdır.";
        } else {
            return msg + this.answer + "</b> bir <b>" + this.era + "</b> edebiyatı yazarıdır.";
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
        TextView tv = new TextView(context);
        tv.setText(fromHtml(msg));

        dialog.setHeaderColor(headerColor)
                .setTitle(title)
                .setCustomView(tv, 20, 20, 20, 20)
                .setPositiveText(R.string.dialog_next_question)
                .show();
    }

    /**
     * fromHtml method for different API level compatibility.
     *
     * @param html HTML value to process.
     * @return fromHtml implementation result.
     */
    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /**
     * Display a simple toast.
     *
     * @param text
     */
    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}
