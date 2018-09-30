package com.appian.manchesterunitednews.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseStateFragmentPagerAdapter extends FragmentPagerAdapter implements StateFragment {
    private List<Fragment> mFragments;

    public BaseStateFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    @Override
    public void onCreated(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public void onDestroyed(Fragment fragment) {
        mFragments.remove(fragment);
    }

    public List<Fragment> getStateFragments() {
        return mFragments;
    }
}
