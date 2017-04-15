package io.cordova.lysedebiyat.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spanned;
import android.widget.Button;
import android.widget.TextView;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import io.cordova.lysedebiyat.DatabaseHelpers.StatsDatabaseHelper;
import io.cordova.lysedebiyat.R;

public class QuestionHelper {

    protected Context context;

    // Stats counters
    protected int correctCount = 0;
    protected int wrongCount = 0;
    protected Boolean isCorrectKnown = false;


    // Data properties
    String data[][];
    String answer;
    String question;
    String era;


    // UI Elements
    TextView correctAnswer;
    TextView wrongAnswer;
    TextView soru;
    Button[] buttonObjects;

    String eraList[];
    HashSet<String> selectedEras;

    SharedPreferences settings;


    StatsDatabaseHelper statDb;
    long unixTime = System.currentTimeMillis() / 1000L;

    MaterialStyledDialog.Builder dialog;

    public QuestionHelper(Context c) {
        context = c;
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
