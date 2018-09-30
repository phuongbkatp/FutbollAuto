package com.appian.manchesterunitednews.data.app.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class NotificationHelper {
    private static final long MAX_AGE_SUBSCRIBE = 1000*60*60*24*3;  // 3 days

    private static final String PREF_APP_NOTIFICATION_CONFIG = "app_notification_config";
    public static final String KEY_NOTIFICATION_ON_OFF_ALL = "notification_on_off_all";
    public static final String KEY_CATEGORY_NOTIFICATION = "notification_category";
    public static final String KEY_LANGUAGE_SETTING = "language_setting";
    public static final String KEY_BREAK_NEWS = "notification_break_news";
    public static final String KEY_MATCH_REMINDER = "football_reminder";
    public static final String KEY_MATCH_START = "football_start";
    public static final String KEY_MATCH_GOAL = "football_goal";
    public static final String KEY_MATCH_CARD = "football_card";
    public static final String KEY_MATCH_SUBSTITUTION = "football_substitution";
    public static final String KEY_MATCH_FINISH = "football_finish";
    public static final String KEY_MATCH_LINEUPS = "football_lineups";
    public static final String KEY_MATCH_OTHER_EVENTS = "football_other";

    public static final String KEY_PRIVACY_POLICY = "privacy_policy";
    public static final String KEY_RATE_SETTING = "rate_button";
    public static final String KEY_CLEAR_CACHE = "clear_cache";
    private static final String KEY_TIME_REGISTER_TOPIC = "time_register_topic";

    public static boolean isFollowAll(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_NOTIFICATION_ON_OFF_ALL, true);
    }

    public static boolean isSubscribeNews(Context context) {
        return isValidateSubscribe(context);
    }

    public static void setSubscribeNews(Context context, boolean isSubscribe) {
        long lastTime = (isSubscribe) ? System.currentTimeMillis() : 0;
       registerLastSubscribe(context, lastTime);
    }

    private static boolean isValidateSubscribe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_APP_NOTIFICATION_CONFIG, Context.MODE_PRIVATE);
        long lastTime = prefs.getLong(KEY_TIME_REGISTER_TOPIC, 0);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastTime < MAX_AGE_SUBSCRIBE);
    }

    private static void registerLastSubscribe(Context context, long time) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_APP_NOTIFICATION_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_TIME_REGISTER_TOPIC, time);
        editor.apply();
    }
}
