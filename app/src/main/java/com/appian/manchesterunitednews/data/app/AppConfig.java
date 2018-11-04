package com.appian.manchesterunitednews.data.app;

import android.content.Context;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.R;

import java.util.ArrayList;
import java.util.List;

public class AppConfig {
    private static final String TEST_ADMOB_BANNER = "ca-app-pub-3940256099942544/2934735716";
    private static final String TEST_ADMOB_INTERSTITIAL = "ca-app-pub-3940256099942544/4411468910";

    private RemoteConfigData mConfigData;

    private static AppConfig sInstance;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if(sInstance == null) {
            sInstance = new AppConfig();
        }
        return sInstance;
    }

    public int getTeamId(Context context) {
        return context.getResources().getInteger(R.integer.team_id);
    }

    public String getAppKey() {
        return BuildConfig.FLAVOR;
    }

    public String getAdmobBanner1() {
        if (isDebug()) {
            return TEST_ADMOB_BANNER;
        }
        return "";
    }

    public String getAdmobInterstitial() {
        if (isDebug()) {
            return TEST_ADMOB_INTERSTITIAL;
        }
        return "";
    }

    public String getFbAdsNative1() {
        return "";
    }

    public String getFbAdsNative2() {
        return "";
    }

    public String getPolicyUrl() {
        return "";
    }

    public void setRemoteConfigData(RemoteConfigData dataConfig) {
        mConfigData = dataConfig;
    }

    public List<RemoteConfigData.League> getLeagues() {
        if(mConfigData == null || mConfigData.getLeagues() == null) {
            return new ArrayList<>();
        }
        return mConfigData.getLeagues();
    }

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
