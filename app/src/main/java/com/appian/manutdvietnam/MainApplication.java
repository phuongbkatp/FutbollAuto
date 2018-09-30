package com.appian.manutdvietnam;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.appian.manutdvietnam.data.app.Language;
import com.appian.manutdvietnam.util.Utils;
import com.facebook.FacebookSdk;

public class MainApplication extends MultiDexApplication {
    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if(sInstance == null) {
            sInstance = this;
        }
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
