package com.appian.manchesterunitednews.service.notification;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.detailnews.DetailArticleActivity;
import com.appian.manchesterunitednews.app.match.MatchActivity;
import com.appian.manchesterunitednews.app.match.videohighlight.VideoActivity;

import java.util.Map;

public class NotificationFactory {

    private static boolean handleNewsNotification(Context context, Bundle data) {
        int id;
        try {
            id = Integer.parseInt(data.getString("id"));
        } catch (NumberFormatException e) {
            id = 0;
        }
        // Video type
        if("video".equals(data.getString("news_type"))) {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("id", id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            int[] ids = {id};
            Intent intent = new Intent(context, DetailArticleActivity.class);
            //intent.putExtra(DetailArticleActivity.EXTRA_NEWS_LIST_ID, ids);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

    private static NotificationProvider createNewsNotification(Context context, Map<String, String> data) {
        int id;
        try {
            id = Integer.parseInt(data.get("id"));
        } catch (NumberFormatException e) {
            id = 0;
        }
        String title = data.get("title");
        String newsType = data.get("news_type");
        return createNewsNotification(context, id, title, newsType);
    }

    private static NotificationProvider createNewsNotification(Context context, int id, String title, String newsType) {
        int[] ids = {id};
        Resources res = context.getResources();
        NotificationProvider notification = new NotificationProvider(context);
        if("video".equals(newsType)) {
            notification.setTitle(res.getString(R.string.notification_video));
            notification.putExtra("id", id);
            notification.setClass(VideoActivity.class);
        } else {
            //notification.putExtra(DetailArticleActivity.EXTRA_NEWS_LIST_ID, ids);
            notification.setTitle(res.getString(R.string.notification_news));
            notification.setClass(DetailArticleActivity.class);
        }
        notification.setText(title);
        notification.setId(id);
        return notification;
    }

    private static boolean handleMatchNotification(Context context, Bundle data) {
        int id;
        try {
            id = Integer.parseInt(data.getString("sofa_id"));
        } catch (NumberFormatException e) {
            id = 0;
        }
        Intent intent = new Intent(context, MatchActivity.class);
        intent.putExtra(Constant.KEY_SOFA_MATCH_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    private static NotificationProvider createMatchNotification(Context context, Map<String, String> data) {
        int id;
        try {
            id = Integer.parseInt(data.get("sofa_id"));
        } catch (NumberFormatException e) {
            id = 0;
        }
        String title = data.get("title");
        String body = data.get("body");
        return createMatchNotification(context, id, title, body);
    }

    private static NotificationProvider createMatchNotification(Context context, int id, String title, String body) {
        NotificationProvider notification = new NotificationProvider(context);
        notification.putExtra(Constant.KEY_SOFA_MATCH_ID, id);
        notification.setTitle(title);
        notification.setText(body);
        notification.setClass(MatchActivity.class);
        notification.setId(id);
        return notification;
    }

    public static NotificationProvider create(Context context, Map<String, String> data) {
        String type = data.get("type");
        if (Constant.NOTIFICATION_TYPE_NEWS.equals(type)) {
            return createNewsNotification(context, data);
        }
        if (Constant.NOTIFICATION_TYPE_MATCH.equals(type)) {
            return createMatchNotification(context, data);
        }
        return null;
    }

    public static boolean handleNotification(Context context, Bundle data) {
        String type = data.getString("type");
        if (Constant.NOTIFICATION_TYPE_NEWS.equals(type)) {
            return handleNewsNotification(context, data);
        }
        if (Constant.NOTIFICATION_TYPE_MATCH.equals(type)) {
            return handleMatchNotification(context, data);
        }
        return false;
    }
}
