package com.smart.notebook.ui.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;

    public ViewPagerAdapter(FragmentManager fm, Fragment[] mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
