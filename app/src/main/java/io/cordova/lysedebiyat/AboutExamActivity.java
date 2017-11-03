package io.cordova.lysedebiyat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AboutExamActivity extends BaseActivity {

    private class AboutSection {
        String title;
        String content;

        public AboutSection(int titleResource, int contentResource) {
            this.title = getResources().getString(titleResource);
            this.content = getResources().getString(contentResource);
        }

        public String getTitle() {
            return this.title;
        }

        public String getContent() {
            return this.content;
        }
    }

    List<AboutSection> sections = new ArrayList<>();

    private void initSections() {
        sections.add(new AboutSection(R.string.what_is_exam_title, R.string.what_is_exam_content));
        sections.add(new AboutSection(R.string.about_system_title, R.string.about_system_content));
        sections.add(new AboutSection(R.string.question_count_title, R.string.question_count_content));
        sections.add(new AboutSection(R.string.scoring_title, R.string.scoring_content));
        sections.add(new AboutSection(R.string.topics_title, R.string.topics_content));
    }

    private TextView createTextView(String content, boolean isHeader) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tw = new TextView(this);
        tw.setLayoutParams(params);
        if (isHeader) {
            tw.setTextColor(getResources().getColor(R.color.secondary_text));
            tw.setTextSize(24);
            tw.setTypeface(null, Typeface.BOLD);
        }
        tw.setText(content);

        return tw;
    }

    private View createDivider() {
        /*<View
                android:layout_width="match_parent"
                android:layout_height="1dp"
                android:layout_marginEnd="16dp"
                android:layout_marginStart="16dp"
                android:background="@color/divider" />*/

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 24, 0, 24);
        params.height = 1;
        View divider = new View(this);
        divider.setLayoutParams(params);
        divider.setBackgroundColor(getResources().getColor(R.color.divider));
        return divider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_exam);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(R.string.about_exam_title);

        this.initSections();
        LinearLayout wrapper = (LinearLayout) findViewById(R.id.about_exam_wrapper);
        wrapper.setPadding(48, 48, 48, 48);

        for (AboutSection section : this.sections) {
            wrapper.addView(this.createTextView(section.title, true));
            wrapper.addView(this.createTextView(section.content, false));
            wrapper.addView(this.createDivider());
        }
    }
}


