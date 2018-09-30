package com.appian.manchesterunitednews.app;


import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseStateFragment extends BaseFragment {
    private StateFragment mStateFragment;

    public void setStateFragment(StateFragment stateFragment) {
        mStateFragment = stateFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mStateFragment != null) {
            mStateFragment.onCreated(this);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mStateFragment != null) {
            mStateFragment.onDestroyed(this);
        }
    }
}
