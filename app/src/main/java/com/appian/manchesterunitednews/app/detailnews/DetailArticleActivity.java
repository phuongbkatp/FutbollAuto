package com.appian.manchesterunitednews.app.detailnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.util.Utils;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.ads.admob.InterstitialAdMob;

public class DetailArticleActivity extends BaseActivity {
    private static final String TAG_DETAIL_NEWS = "detail_news";

    public static final String LINK = "link";

    private InterstitialAdMob mInterstitialAdMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Intent intent = getIntent();
        String link = intent.getStringExtra(LINK);
        Bundle bundle = new Bundle();
        bundle.putString(LINK, link);
        if (savedInstanceState == null) {
            Fragment newFragment = new DetailNewsFragment();
            newFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.view_content, newFragment, TAG_DETAIL_NEWS).commit();
        }

        View btnBackArrow = findViewById(R.id.img_back_arrow);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        AppConfig appConfig = AppConfig.getInstance();
        mInterstitialAdMob = new InterstitialAdMob(getApplicationContext(), appConfig.getAdmobInterstitial(getApplicationContext()));
        Utils.addAdmobTestDevice(mInterstitialAdMob);
        mInterstitialAdMob.loadAd();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            finish();
            ViewHelper.launchMainScreen(getApplicationContext());
        } else {
            super.onBackPressed();
            mInterstitialAdMob.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}
