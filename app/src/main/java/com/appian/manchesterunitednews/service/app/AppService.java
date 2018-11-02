package com.appian.manchesterunitednews.service.app;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.AppConfigManager;
import com.appian.manchesterunitednews.data.app.Language;
import com.google.firebase.messaging.FirebaseMessaging;

public class AppService extends IntentService {
    public static String ACTION_REFRESH_DEVICE_TOKEN = "action_refresh_device_token";
    public static String ACTION_CHANGE_LANGUAGE = "action_change_language";
    public static String ACTION_SETTING_EVENT_MATCH = "action_setting_event_match";
    public static String ACTION_FOLLOW_NEWS = "action_follow_news";
    public static String KEY_EVENT_MATCH_VALUE = "key_event_match_value";
    public static String KEY_NOTIFICATION_VALUE = "key_notification_value";


    public AppService() {
        super("AppService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (ACTION_REFRESH_DEVICE_TOKEN.equals(action)) {
            refreshDeviceToken();
        } else if (ACTION_CHANGE_LANGUAGE.equals(action)) {
            changeLanguage(intent);
        } else if (ACTION_SETTING_EVENT_MATCH.equals(action)) {
            boolean isSubscribe = intent.getBooleanExtra(KEY_EVENT_MATCH_VALUE, true);
            followEventMatch(isSubscribe);
        } else if (ACTION_FOLLOW_NEWS.equals(action)) {
            boolean isOn = intent.getBooleanExtra(KEY_NOTIFICATION_VALUE, true);
            followNews(isOn);
        }
    }

    private void refreshDeviceToken() {
        followNews(true);
        followEventMatch(true);
        followLanguage(AppConfigManager.getInstance().getLanguage(this));
    }

    private void changeLanguage(Intent intent) {
        if (intent != null) {
            String language = intent.getStringExtra(Language.EXTRA_LANGUAGE);
            followLanguage(language);
            Language.setLocale(this.getBaseContext(), language);
        }
    }

    private void followNews(boolean isSubscribe) {
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(this);
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("news_app_" + appConfig.getAppKey());
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news_app_" + appConfig.getAppKey());
        }
    }

    private void followLanguage(String language) {
        if (Language.VIETNAMESE.equals(language)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("language_" + Language.ENGLISH);
        } else if (Language.ENGLISH.equals(language)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("language_" + Language.VIETNAMESE);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("language_" + language);
    }

    private void followEventMatch(boolean isSubscribe) {
        debugSubscribe(isSubscribe);
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(this);
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("match_team_" + appConfig.getTeamId());
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("match_team_" + appConfig.getTeamId());
        }
    }

    private void debugSubscribe(boolean isSubscribe) {
        if(!BuildConfig.DEBUG) {
            return;
        }
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("event_football");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("event_football");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
