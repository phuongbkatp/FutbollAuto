package com.appian.manchesterunitednews;

import android.content.Context;
import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;

import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.service.app.AppHelper;
import com.appian.manchesterunitednews.util.FontsOverride;
import com.appnet.android.ads.fb.FbAdHelper;

public class MainApplication extends MultiDexApplication {
    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if(sInstance == null) {
            sInstance = this;
        }
        FbAdHelper.initialize(this);
        new BackgroundTask().execute(getApplication());
    }

    public static MainApplication getApplication() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.onAttach(base));
    }

    private static class BackgroundTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... contexts) {
            Context context = contexts[0];
            FontsOverride.setDefaultFont(context, "DEFAULT", "sfregular.otf");
            AppHelper.initSubscribe(context);
            AppHelper.initRemoteConfig();
            return null;
        }
    }
}
