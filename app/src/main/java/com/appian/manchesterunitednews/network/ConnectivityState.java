package com.appian.manchesterunitednews.network;

import android.content.Context;

class ConnectivityState {
    private static ConnectivityState sInstance;

    private boolean isConnected;

    private ConnectivityState(Context context) {
        isConnected = NetworkHelper.isNetworkAvailable(context);
    }

    public static ConnectivityState getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new ConnectivityState(context);
        }
        return sInstance;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
