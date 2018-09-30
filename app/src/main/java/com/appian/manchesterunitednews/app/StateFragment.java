package com.appian.manchesterunitednews.app;

import android.support.v4.app.Fragment;

public interface StateFragment {
    void onCreated(Fragment fragment);
    void onDestroyed(Fragment fragment);
}
