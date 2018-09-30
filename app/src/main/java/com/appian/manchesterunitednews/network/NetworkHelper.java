package com.appian.manchesterunitednews.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public final class NetworkHelper {
    static final String CONNECTIVITY_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;// "android.net.conn.CONNECTIVITY_CHANGE";
    static final String WIFI_STATE_ACTION = WifiManager.WIFI_STATE_CHANGED_ACTION;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return false;
        }
        int type = activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (activeNetworkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
            }
            return true;
        }
        return false;
    }

    public static void registerConnectivityChanged(Context context, NetworkReceiver broadcastReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        intentFilter.addAction(WIFI_STATE_ACTION);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void unregisterConnectivityReceiver(Context context, NetworkReceiver broadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver);
    }
}
