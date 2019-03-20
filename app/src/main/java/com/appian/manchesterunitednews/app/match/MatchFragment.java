package com.appian.manchesterunitednews.app.match;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.BaseStateFragmentPagerAdapter;
import com.appian.manchesterunitednews.app.ToolbarViewListener;
import com.appian.manchesterunitednews.app.match.lineups.LineupsFragment;
import com.appian.manchesterunitednews.app.match.statistics.StatisticsFragment;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.network.NetworkHelper;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.admob.BannerAdMob;

public class MatchFragment extends BaseStateFragment implements ToolbarViewListener {

    private TextView mTvTitle;
    private ViewGroup mAdViewContainer;

    private View mViewNoConnectivity;
    private BannerAdMob mBannerAdMob;

    private int mMatchId = 0;

    public static MatchFragment newInstance(int sofaMatchId) {
        Bundle args = new Bundle();
        args.putInt(Constant.KEY_SOFA_MATCH_ID, sofaMatchId);
        MatchFragment fragment = new MatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMatchId = args.getInt(Constant.KEY_SOFA_MATCH_ID, 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppConfig appConfig = AppConfig.getInstance();
        mBannerAdMob = new BannerAdMob(context, appConfig.getAdbMobMatchDetail(context));
        Utils.addAdmobTestDevice(mBannerAdMob);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mBannerAdMob.addView(mAdViewContainer);
        mBannerAdMob.setOnLoadListener(new OnAdLoadListener() {
            @Override
            public void onAdLoaded() {
                mAdViewContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailed() {
                mAdViewContainer.setVisibility(View.GONE);
            }
        });
        mBannerAdMob.loadAd();
    }

    @Override
    public void changeToolbarTitle(String title) {
        mTvTitle.setText(title);
    }

    private void initView(View view) {
        View btnBackArrow = view.findViewById(R.id.img_back_arrow);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_match);
        ViewPager viewPager = view.findViewById(R.id.view_pager_match);
        mTvTitle = view.findViewById(R.id.txtTitle);
        mAdViewContainer = view.findViewById(R.id.admob_banner_container);
        mViewNoConnectivity = view.findViewById(R.id.view_no_internet_connection);

        MatchPagerAdapter pagerAdapter = new MatchPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        mTvTitle.setText(getResources().getString(R.string.livescore_tab));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Resources res = getResources();
                switch (tab.getPosition()) {
                    case 0:
                        mTvTitle.setText(res.getString(R.string.statistic_tab));
                        break;
                    case 1:
                        mTvTitle.setText(res.getString(R.string.lineup_tab));
                        break;
                    case 2:
                        mTvTitle.setText(res.getString(R.string.livescore_tab));
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Context context = getContext();
        Resources res = getResources();
        View view4 = View.inflate(context, R.layout.custom_tab_layout, null);
        View tabStatistic = view4.findViewById(R.id.icon_tab);
        tabStatistic.setBackgroundResource(R.drawable.ic_stats_tab);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            tabStatistic.getBackground().setColorFilter(res.getColor(R.color.globalWhite), PorterDuff.Mode.SRC_IN);
        }
        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        if (tab0 != null) {
            tab0.setCustomView(view4);
        }

        View view2 = View.inflate(context, R.layout.custom_tab_layout, null);
        View tabLineups = view2.findViewById(R.id.icon_tab);
        tabLineups.setBackgroundResource(R.drawable.ic_line_up_tab);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            tabLineups.getBackground().setColorFilter(res.getColor(R.color.globalWhite), PorterDuff.Mode.SRC_IN);
        }
        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        if (tab1 != null) {
            tab1.setCustomView(view2);
        }

        View view1 = View.inflate(context, R.layout.custom_tab_layout, null);
        view1.findViewById(R.id.icon_tab).setBackgroundResource(R.drawable.ic_livescore_tab);
        TabLayout.Tab tab2 = tabLayout.getTabAt(2);
        if (tab2 != null) {
            tab2.setCustomView(view1);
            tab2.select();
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        checkInternetConnection(NetworkHelper.isNetworkAvailable(getContext()));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_match;
    }

    private class MatchPagerAdapter extends BaseStateFragmentPagerAdapter {

        MatchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return StatisticsFragment.newInstance(mMatchId, this);
                case 1:
                    return LineupsFragment.newInstance(mMatchId, this);
                case 2:
                    return MatchDetailFragment.newInstance(mMatchId, this);

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void checkInternetConnection(boolean isConnected) {
        if (mViewNoConnectivity == null) {
            return;
        }
        int visible = (isConnected) ? View.GONE : View.VISIBLE;
        mViewNoConnectivity.setVisibility(visible);
    }

}
