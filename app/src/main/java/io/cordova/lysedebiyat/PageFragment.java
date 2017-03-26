package io.cordova.lysedebiyat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String BOOK_LIST = "BOOK_LIST";
    public static final String ERA_INFO = "ERA_INFO";

    private int mPage;
    private String data[][];
    private EraInfo eraInfo;

    public static PageFragment newInstance(int page, String[][] bookList, EraInfo eraInfo) {
        Bundle args = new Bundle();

        // Put tha arguments to pass to the fragment.
        args.putInt(ARG_PAGE, page);
        args.putSerializable(BOOK_LIST, bookList);
        args.putSerializable(ERA_INFO, eraInfo);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        data = (String[][]) getArguments().getSerializable(BOOK_LIST);
        eraInfo = (EraInfo) getArguments().getSerializable(ERA_INFO);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPage == 1) {
            View view = inflater.inflate(R.layout.fragment_era_info, container, false);

            TextView eraInfoText = (TextView) view.findViewById(R.id.era_info);
            TextView importantAuthors = (TextView) view.findViewById(R.id.important_authors);

            eraInfoText.setText(eraInfo.getInfo());
            importantAuthors.setText(eraInfo.getAuthors());

            // Set the button click listener.
            Button detailsButton = (Button) view.findViewById(R.id.details_button);
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uriUrl = Uri.parse(eraInfo.getLink());
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }
            });

            return view;
        } else {

            View view = inflater.inflate(R.layout.fragment_yazar_eser_liste, container, false);
            StickyListHeadersListView stickyList = (StickyListHeadersListView) view.findViewById(R.id.yazar_eser_list);
            YazarEserAdapter adapter = new YazarEserAdapter(getActivity(), data);
            stickyList.setAdapter(adapter);
            return view;
        }

    }
}