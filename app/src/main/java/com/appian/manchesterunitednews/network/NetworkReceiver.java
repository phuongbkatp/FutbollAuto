package com.appian.manchesterunitednews.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(NetworkHelper.CONNECTIVITY_ACTION.equals(action)) {
            Intent serviceIntent = new Intent(context, NetworkService.class);
            context.startService(serviceIntent);
        }
    }

}
