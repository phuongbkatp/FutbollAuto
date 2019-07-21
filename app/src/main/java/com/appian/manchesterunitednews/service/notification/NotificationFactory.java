package com.appian.manchesterunitednews.service.notification;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.detailnews.DetailArticleActivity;
import com.appian.manchesterunitednews.app.match.MatchActivity;

import java.util.Map;

public class NotificationFactory {

    private static boolean handleNewsNotification(Context context, Bundle data) {
        String link = data.getString("link");
        Intent intent = new Intent(context, DetailArticleActivity.class);
        intent.putExtra("link", link);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    private static NotificationProvider createNewsNotification(Context context, Map<String, String> data) {
        String link = data.get("link");
        String title = data.get("title");
        return createNewsNotification(context, link, title);
    }

    private static NotificationProvider createNewsNotification(Context context, String link, String title) {
        Resources res = context.getResources();
        NotificationProvider notification = new NotificationProvider(context);
        notification.setTitle(res.getString(R.string.notification_news));
        notification.putExtra("link", link);
        notification.setClass(DetailArticleActivity.class);
        notification.setText(title);
        notification.setId(0);
        return notification;
    }

    private static boolean handleMatchNotification(Context context, Bundle data) {
        int id;
        try {
            id = Integer.parseInt(data.getString("id"));
        } catch (NumberFormatException e) {
            id = 0;
        }
        Intent intent = new Intent(context, MatchActivity.class);
        intent.putExtra(Constant.KEY_SOFA_MATCH_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    private static NotificationProvider createMatchNotification(Context context, Map<String, String> data) {
        int id;
        try {
            id = Integer.parseInt(data.get("id"));
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

    private static boolean handleOpenLinkNotification(Context context, Bundle data) {
        String link = data.getString("link");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    private static NotificationProvider createOpenLinkNotification(Context context, String link, String title) {
        Resources res = context.getResources();
        NotificationProvider notification = new NotificationProvider(context);
        notification.setTitle(res.getString(R.string.notification_news));
        notification.setData(Uri.parse(link));
        notification.setAction(Intent.ACTION_VIEW);
        notification.setText(title);
        notification.setId(0);
        return notification;
    }

    private static NotificationProvider createOpenLinkNotification(Context context, Map<String, String> data) {
        String link = data.get("link");
        String title = data.get("title");
        return createOpenLinkNotification(context, link, title);
    }

    public static NotificationProvider create(Context context, Map<String, String> data) {
        String type = data.get("type");
        if (Constant.NOTIFICATION_TYPE_NEWS.equals(type)) {
            return createNewsNotification(context, data);
        }
        if (Constant.NOTIFICATION_TYPE_MATCH.equals(type)) {
            return createMatchNotification(context, data);
        }
        if (Constant.NOTIFICATION_TYPE_OPEN_LINK.equals(type)) {
            return createOpenLinkNotification(context, data);
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
        if (Constant.NOTIFICATION_TYPE_OPEN_LINK.equals(type)) {
            return handleOpenLinkNotification(context, data);
        }
        return false;
    }
}
