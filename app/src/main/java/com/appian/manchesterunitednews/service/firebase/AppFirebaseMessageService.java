package com.appian.manchesterunitednews.service.firebase;

import android.util.Log;

import com.appian.manchesterunitednews.service.notification.NotificationFactory;
import com.appian.manchesterunitednews.service.notification.NotificationProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class AppFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("AAAA", "onMessageReceived: " + remoteMessage.getData());
        if(remoteMessage.getData() == null) {
            return;
        }
        NotificationProvider notificationProvider = NotificationFactory.create(getApplicationContext(), remoteMessage.getData());
        if(notificationProvider != null) {
            notificationProvider.pushNotify();
        }
    }

}
