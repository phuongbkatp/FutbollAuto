package com.appian.manchesterunitednews;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.util.FontsOverride;
import com.appian.manchesterunitednews.util.Utils;
import com.facebook.FacebookSdk;

public class MainApplication extends MultiDexApplication {
    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if(sInstance == null) {
            sInstance = this;
        }
        FontsOverride.setDefaultFont(this, "DEFAULT", "sfregular.otf");
        FacebookSdk.sdkInitialize(getApplicationContext());
        Utils.initAdmob(this);
    }

    public static MainApplication getApplication() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.onAttach(base));
    }
}
