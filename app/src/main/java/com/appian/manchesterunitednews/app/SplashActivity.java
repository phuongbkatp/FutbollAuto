package com.appian.manchesterunitednews.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.data.interactor.AppConfigInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.service.app.AppHelper;
import com.appian.manchesterunitednews.service.notification.NotificationFactory;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.fbvn.data.UserIpData;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);
        if (AppHelper.getCountryCode(getApplicationContext()).equals("")) {
            loadAppConfig();
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            boolean notification = NotificationFactory.handleNotification(getApplicationContext(), getIntent().getExtras());
            if(notification) {
                finish();
            } else {
                launchMainScreen();
            }
        } else {
            launchMainScreen();
        }

    }

    private void launchMainScreen() {
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void loadAppConfig() {
        final Context context = getApplicationContext();
        AppConfigInteractor interactor = new AppConfigInteractor();
        interactor.loadUserIp(new OnResponseListener<UserIpData>() {
            @Override
            public void onSuccess(UserIpData data) {
                AppHelper.saveCountryCode(context, data.getData().getCode());
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
