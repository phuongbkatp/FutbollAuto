package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public final class EventHelper {
    public static final String EVENT_TAP_MATCH_NOTIFICATION = "TAP_MATCH_NOTIFICATION";
    public static final String EVENT_TAP_NEWS_DETAIL = "TAP_NEWS_DETAIL";
    public static final String EVENT_SCREEN_NEWS_CATEGORY = "SCREEN_NEWS_CATEGORY";
    public static final String EVENT_SCREEN_MATCH_DETAIL = "SCREEN_MATCH_DETAIL";
    public static final String SCREEN_LEAGUE = "SCREEN_LEAGUE";
    public static final String EVENT_TAP_VIDEO_HIGHLIGHTS = "TAP_VIDEO_HIGHLIGHTS";

    public static void log(Context context, String event, Bundle args) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if(args == null) {
            args = new Bundle();
        }
        firebaseAnalytics.logEvent(event, args);
    }
}
