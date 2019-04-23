package com.appian.manchesterunitednews.app.coach;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.admob.BannerAdMob;

public class CoachDetailsActivity extends BaseActivity {

    private String mCoachName;
    private int mCoachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail);
        getBundleExtras();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_coach_detail, CoachDetailFragment.newInstance(mCoachId, mCoachName)).commit();
        initAds();
    }

    private void initAds() {
        Context context = getApplicationContext();
        BannerAdMob bannerAdMob = new BannerAdMob(context, context.getString(R.string.admob_match_detail_banner));
        Utils.addAdmobTestDevice(bannerAdMob);
        final ViewGroup adViewContainer = findViewById(R.id.admob_banner_container);
        bannerAdMob.addView(adViewContainer);
        bannerAdMob.setOnLoadListener(new OnAdLoadListener() {
            @Override
            public void onAdLoaded() {
                adViewContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailed() {
                adViewContainer.setVisibility(View.GONE);
            }
        });
        bannerAdMob.loadAd();
    }


    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mCoachName = bundle.getString(Constant.EXTRA_KEY_MANAGER_NAME);
            this.mCoachId = bundle.getInt(Constant.EXTRA_KEY_SOFA_MANAGER_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
