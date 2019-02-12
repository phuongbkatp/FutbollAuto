package com.appian.manchesterunitednews.service.firebase;

import android.util.Log;

import com.appian.manchesterunitednews.service.app.AppHelper;
import com.appian.manchesterunitednews.service.notification.NotificationFactory;
import com.appian.manchesterunitednews.service.notification.NotificationProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class AppFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppHelper.refreshDeviceToken(getApplicationContext());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() == null) {
            return;
        }
        Map<String, String> message = remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if(notification != null) {
            message.put("title", notification.getTitle());
            message.put("body", notification.getBody());
        }
        NotificationProvider notificationProvider = NotificationFactory.create(getApplicationContext(), remoteMessage.getData());
        if(notificationProvider != null) {
            notificationProvider.pushNotify();
        }
    }

}
