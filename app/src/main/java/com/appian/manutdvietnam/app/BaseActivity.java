package com.appian.manutdvietnam.app;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.appian.manutdvietnam.data.app.Language;
import com.appian.manutdvietnam.network.NetworkHelper;
import com.appian.manutdvietnam.network.NetworkReceiver;


public class BaseActivity extends AppCompatActivity {
    private NetworkReceiver mNetworkReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mNetworkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkReceiver = new NetworkReceiver();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.onAttach(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mNetworkReceiver != null) {
            NetworkHelper.registerConnectivityChanged(getApplicationContext(), mNetworkReceiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mNetworkReceiver != null) {
            NetworkHelper.unregisterConnectivityReceiver(getApplicationContext(), mNetworkReceiver);
        }
    }
}
