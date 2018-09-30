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

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.AppConfigManager;
import com.appian.manchesterunitednews.network.NetworkHelper;
import com.appian.manchesterunitednews.service.app.AppHelper;
import com.appian.manchesterunitednews.service.notification.NotificationFactory;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Context context = getApplicationContext();
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(context);
        if ((appConfig.getTeamId() == 0 || appConfig.getAppId() == 0) && !NetworkHelper.isNetworkAvailable(context)) {
            showNetworkDialog();
            return;
        }
        loadAppConfig();
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

    private void loadAppConfig() {
        AppHelper.loadAppConfig(getApplicationContext());
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

    private void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources res = getResources();
        builder.setTitle(res.getString(R.string.title_network_error));
        builder.setMessage(res.getString(R.string.body_network_error));
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
