package com.appian.manutdvietnam.service.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.appian.manutdvietnam.BuildConfig;
import com.appian.manutdvietnam.data.app.AppConfig;
import com.appian.manutdvietnam.data.app.AppConfigManager;
import com.appian.manutdvietnam.data.app.Language;
import com.appian.manutdvietnam.data.app.helper.NotificationHelper;
import com.appian.manutdvietnam.data.interactor.AppConfigInteractor;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appian.manutdvietnam.util.Utils;
import com.appnet.android.football.fbvn.data.AppConfigData;
import com.appnet.android.football.fbvn.data.AppConfigItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;

public class AppService extends IntentService {
    public static String ACTION_LOAD_APP_CONFIG = "action_load_app_config";
    public static String ACTION_REFRESH_DEVICE_TOKEN = "action_refresh_device_token";
    public static String ACTION_CHANGE_LANGUAGE = "action_change_language";
    public static String ACTION_SETTING_EVENT_MATCH = "action_setting_event_match";
    public static String ACTION_CHANGE_NOTIFICATION = "action_change_notification";
    public static String ACTION_FOLLOW_NEWS = "action_follow_news";
    public static String KEY_EVENT_MATCH_TYPE = "key_event_match_type";
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
        if (ACTION_LOAD_APP_CONFIG.equals(action)) {
            loadAppConfig();
        } else if (ACTION_REFRESH_DEVICE_TOKEN.equals(action)) {
            refreshDeviceToken();
        } else if (ACTION_CHANGE_LANGUAGE.equals(action)) {
            changeLanguage(intent);
        } else if (ACTION_SETTING_EVENT_MATCH.equals(action)) {
            String event = intent.getStringExtra(KEY_EVENT_MATCH_TYPE);
            boolean isSubscribe = intent.getBooleanExtra(KEY_EVENT_MATCH_VALUE, true);
            followEventMatch(event, isSubscribe);
        } else if (ACTION_CHANGE_NOTIFICATION.equals(action)) {
            boolean isOn = intent.getBooleanExtra(KEY_NOTIFICATION_VALUE, true);
            followAll(isOn);
        } else if (ACTION_FOLLOW_NEWS.equals(action)) {
            boolean isOn = intent.getBooleanExtra(KEY_NOTIFICATION_VALUE, true);
            followNews(isOn);
        }
    }

    private void refreshDeviceToken() {
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(this);
        if (NotificationHelper.isSubscribeNews(this)) {
            return;
        }
        if (appConfig.getAppId() != 0 && appConfig.getTeamId() != 0) {
            followNews(appConfig.getAppId(), appConfig.getTeamId(), true);
            followMatchesOfTeam(appConfig.getTeamId(), true);
            NotificationHelper.setSubscribeNews(this, true);
        } else {
            AppHelper.loadAppConfig(this);
        }
        followAllEventMatch();
        followLanguage(AppConfigManager.getInstance().getLanguage(this));
    }

    private void changeLanguage(Intent intent) {
        if(intent != null) {
            String language = intent.getStringExtra(Language.EXTRA_LANGUAGE);
            followLanguage(language);
            Language.setLocale(this.getBaseContext(), language);
        }
    }

    private void followNews(boolean isOn) {
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(this);
        if (appConfig.getAppId() != 0 && appConfig.getTeamId() != 0) {
            followNews(appConfig.getAppId(), appConfig.getTeamId(), isOn);
        }
    }

    private void followNews(int appId, int teamId, boolean isSubscribe) {
        if (!NotificationHelper.isFollowAll(getApplicationContext())) {
            return;
        }
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("news_app_" + appId);
            FirebaseMessaging.getInstance().subscribeToTopic("news_team_" + teamId);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news_app_" + appId);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news_team_" + teamId);
        }
    }

    private void followLanguage(String language) {
        if (!NotificationHelper.isFollowAll(getApplicationContext())) {
            return;
        }
        if (Language.VIETNAMESE.equals(language)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("language_" + Language.ENGLISH);
        } else if (Language.ENGLISH.equals(language)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("language_" + Language.VIETNAMESE);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("language_" + language);
    }

    private void followEventMatch(String event, boolean isSubscribe) {
        if (!NotificationHelper.isFollowAll(getApplicationContext())) {
            return;
        }
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("event_type_" + event);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("event_type_" + event);
        }
    }

    private void followAllEventMatch() {
        if (!NotificationHelper.isFollowAll(getApplicationContext())) {
            return;
        }
        String[] events = {"football_goal", "football_card", "football_lineups", "football_substitution",
                "football_start", "football_finish", "football_reminder", "football_other"};
        for (String event : events) {
            FirebaseMessaging.getInstance().subscribeToTopic("event_type_" + event);
        }
    }

    private void followAll(boolean isOn) {
        if (isOn) {
            FirebaseInstanceId.getInstance().getToken();
        } else {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
                NotificationHelper.setSubscribeNews(getApplicationContext(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void followMatchesOfTeam(int teamId, boolean isSubscribe) {
        if (!NotificationHelper.isFollowAll(getApplicationContext())) {
            return;
        }
        if (isSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic("event_match_team_" + teamId);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("event_match_team_" + teamId);
        }
    }

    public void loadAppConfig() {
        final Context context = getApplicationContext();
        AppConfigInteractor interactor = new AppConfigInteractor();
        interactor.loadAppConfig(BuildConfig.FLAVOR, new OnResponseListener<AppConfigData>() {
            @Override
            public void onSuccess(AppConfigData data) {
                HashMap<String, String> map = new HashMap<>();
                for (AppConfigItem item : data.getData()) {
                    map.put(item.getKey(), item.getValue());
                }
                AppConfigManager.getInstance().saveAppConfig(context, map);
                AppHelper.refreshDeviceToken(context);
                Utils.initAdmob(context);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
