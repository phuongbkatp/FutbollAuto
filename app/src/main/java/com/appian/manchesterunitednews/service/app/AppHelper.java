package com.appian.manchesterunitednews.service.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.data.app.RemoteConfigData;
import com.appian.manchesterunitednews.data.app.helper.NotificationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public final class AppHelper {
    private static final String PREF_APP_CONFIG = "app_config";
    private static final String FIRST_TIME = "first_time";

    private static final long CONFIG_EXPIRE_SECOND = 12 * 3600;     // 12 hours

    public static void refreshDeviceToken(Context context) {
        followNews(context, true);
        followEventMatch(context, true);
        followLanguage(Language.getLanguage(context));
    }

    public static void initSubscribe(Context context) {
        boolean subscribeNews = NotificationHelper.isSubscribeNews(context);
        followNews(context, subscribeNews);
        boolean subscribeMatch = NotificationHelper.isSubscribeMatch(context);
        followEventMatch(context, subscribeMatch);
        followLanguage(Language.getLanguage(context));
    }

    public static void changeLanguage(final Context context, String language) {
        followLanguage(language);
        Language.setLocale(context, language);
    }

    public static void followNews(Context context, boolean isSubscribe) {
        if (FirebaseMessaging.getInstance() == null) {
            return;
        }
        AppConfig appConfig = AppConfig.getInstance();
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("dailynews_app_" + appConfig.getAppKey());
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("dailynews_app_" + appConfig.getAppKey());
        }
    }

    private static void followLanguage(String language) {
        if (FirebaseMessaging.getInstance() == null) {
            return;
        }
        if (Language.VIETNAMESE.equals(language)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("language_" + Language.ENGLISH);
        } else if (Language.ENGLISH.equals(language)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("language_" + Language.VIETNAMESE);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("language_" + language);
    }

    public static void followEventMatch(Context context, boolean isSubscribe) {
        debugSubscribe(isSubscribe);
        if (FirebaseMessaging.getInstance() == null) {
            return;
        }
        AppConfig appConfig = AppConfig.getInstance();
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("match_team_" + appConfig.getTeamId(context));
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("match_team_" + appConfig.getTeamId(context));
        }
    }

    private static void debugSubscribe(boolean isSubscribe) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (FirebaseMessaging.getInstance() == null) {
            return;
        }
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("event_football");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("event_football");
        }
    }

    public static void initRemoteConfig() {
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        if (config == null) {
            return;
        }
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        config.setConfigSettings(settings);
        long expireTime = config.getInfo().getConfigSettings().isDeveloperModeEnabled() ? 0 : CONFIG_EXPIRE_SECOND;
        config.fetch(expireTime)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String app = BuildConfig.FLAVOR+"_config";
                        config.activateFetched();
                        String json = config.getString(app);
                        if(TextUtils.isEmpty(json)) {
                            return;
                        }
                        Gson gson = new Gson();
                        try {
                            RemoteConfigData data = gson.fromJson(json, RemoteConfigData.class);
                            if(data == null) {
                                return;
                            }
                            AppConfig.getInstance().setRemoteConfigData(data);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

}
