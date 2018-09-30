package com.appian.manchesterunitednews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;


public final class ReceiverHelper {
    private static final String ACTION_USER_PROFILE_CHANGED = "action_user_profile_changed";

    public static void notifyUserProfileChanged(Context context) {
        Intent intent = new Intent(ACTION_USER_PROFILE_CHANGED);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerUserProfileChanged(Context context, BroadcastReceiver broadcastReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_USER_PROFILE_CHANGED);
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }
}
