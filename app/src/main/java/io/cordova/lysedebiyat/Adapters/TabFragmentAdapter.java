package io.cordova.lysedebiyat.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.cordova.lysedebiyat.EraInfoActivity;
import io.cordova.lysedebiyat.PageFragment;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Dönem Bilgisi", "Eserler"};

    private String bookList[][];
    private EraInfoActivity eraInfo;

    public TabFragmentAdapter(FragmentManager fm, String bookList[][], EraInfoActivity eraInfo) {
        super(fm);
        this.bookList = bookList;
        this.eraInfo = eraInfo;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, bookList, eraInfo);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}