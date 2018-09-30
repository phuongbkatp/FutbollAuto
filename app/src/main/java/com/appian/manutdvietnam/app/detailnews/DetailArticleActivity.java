package com.appian.manutdvietnam.app.detailnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseActivity;
import com.appian.manutdvietnam.app.adapter.AdapterViewPager;
import com.appian.manutdvietnam.data.app.AppConfig;
import com.appian.manutdvietnam.data.app.AppConfigManager;
import com.appian.manutdvietnam.util.ViewHelper;
import com.appnet.android.ads.admob.InterstitialAdMob;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class DetailArticleActivity extends BaseActivity implements DetailNewsFragment.OnButtonClickListener {

    public static final String EXTRA_NEWS_LIST_ID = "extra_news_list_id";

    private ViewPager mViewPagerNews;
    private InterstitialAdMob mInterstitialAdMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        List<Fragment> listNewsFragments = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            int ids[] = intent.getIntArrayExtra(EXTRA_NEWS_LIST_ID);
            if (ids != null) {
                for (int id : ids) {
                    listNewsFragments.add(DetailNewsFragment.newInstance(id));
                }
            }
        }
        mViewPagerNews = findViewById(R.id.viewpagerNews);
        CirclePageIndicator indicatorNews = findViewById(R.id.indicatorNews);
        AdapterViewPager newsAdapterViewPager = new AdapterViewPager(getSupportFragmentManager(), listNewsFragments);
        mViewPagerNews.setAdapter(newsAdapterViewPager);
        indicatorNews.setViewPager(mViewPagerNews);
        View btnBackArrow = findViewById(R.id.img_back_arrow);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(this);
        mInterstitialAdMob = new InterstitialAdMob(getApplicationContext(), appConfig.getAdmobInterstitial());
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

    @Override
    public void onButtonClicked(View view) {
        mViewPagerNews.setCurrentItem(mViewPagerNews.getCurrentItem() + 1);
    }
}
