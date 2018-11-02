package com.appian.manchesterunitednews.data.app.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class NotificationHelper {
    public static final String KEY_LANGUAGE_SETTING = "language_setting";
    public static final String KEY_BREAK_NEWS = "notification_break_news";
    public static final String KEY_MATCH_EVENT = "notification_match_event";

    public static final String KEY_PRIVACY_POLICY = "privacy_policy";
    public static final String KEY_RATE_SETTING = "rate_button";
    public static final String KEY_CLEAR_CACHE = "clear_cache";

    public static boolean isSubscribeNews(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_BREAK_NEWS, true);
    }

    public static boolean isSubscribeMatch(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(KEY_MATCH_EVENT, true);
    }
}
