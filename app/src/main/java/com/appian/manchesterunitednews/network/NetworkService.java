package com.appian.manchesterunitednews.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

public class NetworkService extends IntentService {
    public NetworkService() {
        super("NetworkService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean newConnection = NetworkHelper.isNetworkAvailable(getApplicationContext());
        boolean oldConnection = ConnectivityState.getInstance(getApplicationContext()).isConnected();
        if(newConnection != oldConnection) {
            ConnectivityState.getInstance(getApplicationContext()).setConnected(newConnection);
            postEventBus(new ConnectivityEvent(newConnection));
        }
    }

    private void postEventBus(Object object) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        EventBus.getDefault().post(object);
    }
}
