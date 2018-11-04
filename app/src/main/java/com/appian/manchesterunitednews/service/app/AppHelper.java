package com.appian.manchesterunitednews.service.app;

import android.content.Context;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.AppConfigManager;
import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.data.app.helper.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessaging;

public final class AppHelper {

    public static void refreshDeviceToken(Context context) {
        followNews(context, true);
        followEventMatch(context, true);
        followLanguage(AppConfigManager.getInstance().getLanguage(context));
    }

    public static void initSubscribe(Context context) {
        boolean subscribeNews = NotificationHelper.isSubscribeNews(context);
        followNews(context, subscribeNews);
        boolean subscribeMatch = NotificationHelper.isSubscribeMatch(context);
        followEventMatch(context, subscribeMatch);
        followLanguage(AppConfigManager.getInstance().getLanguage(context));
    }

    public static void changeLanguage(final Context context, String language) {
        followLanguage(language);
        Language.setLocale(context, language);
    }

    public static void followNews(Context context, boolean isSubscribe) {
        if (FirebaseMessaging.getInstance() == null) {
            return;
        }
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(context);
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("news_app_" + appConfig.getAppKey());
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news_app_" + appConfig.getAppKey());
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
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(context);
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

}
