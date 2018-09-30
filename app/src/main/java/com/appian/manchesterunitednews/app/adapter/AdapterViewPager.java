package com.appian.manchesterunitednews.app.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdapterViewPager extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    public AdapterViewPager(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }
    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }
}