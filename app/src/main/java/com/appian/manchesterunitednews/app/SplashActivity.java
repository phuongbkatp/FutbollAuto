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
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.AppConfigManager;
import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.network.NetworkHelper;
import com.appian.manchesterunitednews.service.notification.NotificationFactory;
import com.appian.manchesterunitednews.util.ViewHelper;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private Handler mHandler;
    private RelativeLayout mRlEnglish;
    private LinearLayout mChooseLanguage;
    private RelativeLayout mRlVietnamese;
    private View view_English;
    private View view_Vietnamese;
    String language;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);
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
        mRlEnglish = findViewById(R.id.english_layout);
        view_English = findViewById(R.id.english_view);
        mChooseLanguage = findViewById(R.id.choose_language);
        language = AppConfigManager.getInstance().getLanguage(this);
        if (language.equals(Language.ENGLISH)) {
            ViewHelper.setBackground(view_English, getResources().getDrawable(R.drawable.theme_enable_circle));
        } else {
            ViewHelper.setBackground(view_English, getResources().getDrawable(R.drawable.theme_disable_circle));

        }
        mRlEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Language.setLocale(SplashActivity.this, Language.ENGLISH);
                ViewHelper.setBackground(view_English, getResources().getDrawable(R.drawable.theme_enable_circle));
                ViewHelper.setBackground(view_Vietnamese, getResources().getDrawable(R.drawable.theme_disable_circle));

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mRlVietnamese = findViewById(R.id.vietnam_layout);
        view_Vietnamese = findViewById(R.id.vietnam_view);
        if (language.equals(Language.VIETNAMESE)) {
            ViewHelper.setBackground(view_Vietnamese, getResources().getDrawable(R.drawable.theme_disable_circle));
        } else {
            ViewHelper.setBackground(view_Vietnamese, getResources().getDrawable(R.drawable.theme_disable_circle));
        }
        mRlVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Language.setLocale(SplashActivity.this, Language.VIETNAMESE);
                ViewHelper.setBackground(view_Vietnamese, getResources().getDrawable(R.drawable.theme_enable_circle));
                ViewHelper.setBackground(view_English, getResources().getDrawable(R.drawable.theme_disable_circle));
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void launchMainScreen() {
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppConfigManager.isFirstTime(SplashActivity.this)) {
                    mChooseLanguage.setVisibility(View.VISIBLE);
                    AppConfigManager.setIsFirstTime(SplashActivity.this, false);
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
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
