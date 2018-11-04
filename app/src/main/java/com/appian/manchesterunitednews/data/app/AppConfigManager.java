package com.appian.manchesterunitednews.data.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class AppConfigManager {
    private static final String PREF_APP_CONFIG = "app_config";
    private static final String KEY_TEAM_ID = "default_team_id";
    private static final String KEY_TEAM_SOFA_ID = "default_team_sofa_id";
    private static final String KEY_APP_ID = "app_id";
    private static final String KEY_CURRENT_SEASON_ID = "default_season_league_id";
    private static final String KEY_TAB_5_ID = "category_tab5_id";
    private static final String KEY_TAB_4_ID = "category_tab4_id";
    private static final String KEY_TAB_3_ID = "category_tab3_id";
    private static final String KEY_TAB_2_ID = "category_tab2_id";
    private static final String KEY_TAB_1_ID = "category_tab1_id";
    private static final String KEY_EVENT_ID = "category_event_id";
    private static final String KEY_POLICY_URL = "policy_url";
    private static final String KEY_TAB_1_TITLE_VI = "tab1_title_vi";
    private static final String KEY_TAB_1_TITLE_EN = "tab1_title_en";
    private static final String KEY_TAB_2_TITLE_VI = "tab2_title_vi";
    private static final String KEY_TAB_2_TITLE_EN = "tab2_title_en";
    private static final String KEY_TAB_3_TITLE_VI = "tab3_title_vi";
    private static final String KEY_TAB_3_TITLE_EN = "tab3_title_en";
    private static final String KEY_TAB_4_TITLE_VI = "tab4_title_vi";
    private static final String KEY_TAB_4_TITLE_EN = "tab4_title_en";
    private static final String KEY_TAB_5_TITLE_VI = "tab5_title_vi";
    private static final String KEY_TAB_5_TITLE_EN = "tab5_title_en";
    private static final String KEY_SHARE_CONTENT = "share_content";
    private static final String KEY_ADMOB_APP_ID = "android_admob_app_id";
    private static final String KEY_ADMOB_BANNER_1 = "android_admob_banner_1";
    private static final String KEY_ADMOB_INTERSTITIAL_1 = "android_admob_interstitial_1";
    private static final String KEY_FB_NATIVE_1 = "android_fb_native_1";
    private static final String KEY_FB_NATIVE_2 = "android_fb_native_2";
    private static final String FIRST_TIME = "first_time";


    private static AppConfigManager sInstance;

    private AppConfig mAppConfig = null;

    private AppConfigManager() {

    }

    public static AppConfigManager getInstance() {
        if (sInstance == null) {
            sInstance = new AppConfigManager();
        }
        return sInstance;
    }

    public void saveAppConfig(Context context, HashMap<String, String> data) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_APP_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String[] keyInt = {KEY_TEAM_ID, KEY_TEAM_SOFA_ID, KEY_APP_ID, KEY_CURRENT_SEASON_ID, KEY_EVENT_ID,
                KEY_TAB_1_ID, KEY_TAB_2_ID, KEY_TAB_3_ID, KEY_TAB_4_ID, KEY_TAB_5_ID};
        for (String key : keyInt) {
            editor.putInt(key, fromString(data, key));
        }
        String[] keyStr = {KEY_POLICY_URL, KEY_TAB_1_TITLE_VI, KEY_TAB_1_TITLE_EN, KEY_TAB_2_TITLE_VI, KEY_TAB_2_TITLE_EN,
                KEY_TAB_3_TITLE_VI, KEY_TAB_3_TITLE_EN, KEY_TAB_4_TITLE_VI, KEY_TAB_4_TITLE_EN, KEY_TAB_5_TITLE_VI, KEY_TAB_5_TITLE_EN,
                KEY_SHARE_CONTENT,
                KEY_ADMOB_APP_ID, KEY_ADMOB_BANNER_1, KEY_ADMOB_INTERSTITIAL_1, KEY_FB_NATIVE_1, KEY_FB_NATIVE_2
        };
        for (String key : keyStr) {
            editor.putString(key, data.get(key));
        }
        editor.apply();
        mAppConfig = null;
        mAppConfig = getAppConfig(context);
    }

    public String
    getLanguage(Context context) {
        return Language.getLanguage(context);
    }

    public static boolean isFirstTime (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_APP_CONFIG, Context.MODE_PRIVATE);
         return prefs.getBoolean(FIRST_TIME, true);
    }
    public static void setIsFirstTime (Context context, boolean isFirst) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_APP_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FIRST_TIME, isFirst);
        editor.apply();
    }
    public AppConfig getAppConfig(Context context) {
        if (mAppConfig == null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_APP_CONFIG, Context.MODE_PRIVATE);
            int appId = prefs.getInt(KEY_APP_ID, 0);
            int teamId = prefs.getInt(KEY_TEAM_ID, 0);
            int teamSofaId = prefs.getInt(KEY_TEAM_SOFA_ID, 0);
            int seasonId = prefs.getInt(KEY_CURRENT_SEASON_ID, 0);
            int tab5Id = prefs.getInt(KEY_TAB_5_ID, 0);
            int tab4Id = prefs.getInt(KEY_TAB_4_ID, 0);
            int tab3Id = prefs.getInt(KEY_TAB_3_ID, 0);
            int tab2Id = prefs.getInt(KEY_TAB_2_ID, 0);
            int tab1Id = prefs.getInt(KEY_TAB_1_ID, 0);
            int eventId = prefs.getInt(KEY_EVENT_ID, 0);
            String policyUrl = prefs.getString(KEY_POLICY_URL, "");
            String shareContent = prefs.getString(KEY_SHARE_CONTENT, "");
            AppConfig.Builder builder = new AppConfig.Builder();
            String[] keyStr = {KEY_TAB_1_TITLE_VI, KEY_TAB_1_TITLE_EN, KEY_TAB_2_TITLE_VI, KEY_TAB_2_TITLE_EN,
                    KEY_TAB_3_TITLE_VI, KEY_TAB_3_TITLE_EN, KEY_TAB_4_TITLE_VI, KEY_TAB_4_TITLE_EN, KEY_TAB_5_TITLE_VI, KEY_TAB_5_TITLE_EN
            };
            for (String key : keyStr) {
                builder.putTitle(key, prefs.getString(key, ""));
            }
            String admobAppId = prefs.getString(KEY_ADMOB_APP_ID, "");
            String admobBanner1 = prefs.getString(KEY_ADMOB_BANNER_1, "");
            String admobInterstitial1 = prefs.getString(KEY_ADMOB_INTERSTITIAL_1, "");
            String fbNative1 = prefs.getString(KEY_FB_NATIVE_1, "");
            String fbNative2 = prefs.getString(KEY_FB_NATIVE_2, "");
            builder.setAppId(appId)
                    .setTeamId(teamId)
                    .setTeamSofaId(teamSofaId)
                    .setCurrentSeasonId(seasonId)
                    .setTab5Id(tab5Id)
                    .setTab4Id(tab4Id)
                    .setTab3Id(tab3Id)
                    .setTab2Id(tab2Id)
                    .setTab1Id(tab1Id)
                    .setCategoryEventId(eventId)
                    .setShareContent(shareContent)
                    .setPolicyUrl(policyUrl)
                    .setAdmobAppId(admobAppId)
                    .setAdmobBanner1(admobBanner1)
                    .setAdmobInterstitial1(admobInterstitial1)
                    .setFbAdsNative1(fbNative1)
                    .setFbAdsNative2(fbNative2);

            mAppConfig = builder.build();
        }
        return mAppConfig;
    }

    private int fromString(HashMap<String, String> data, String key) {
        int value;
        try {
            value = Integer.parseInt(data.get(key));
        } catch (RuntimeException e) {
            value = 0;
        }
        return value;
    }
}
