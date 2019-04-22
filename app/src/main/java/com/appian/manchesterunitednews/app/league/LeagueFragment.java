package com.appian.manchesterunitednews.app.league;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.BaseStateFragmentPagerAdapter;
import com.appian.manchesterunitednews.app.ToolbarViewListener;
import com.appian.manchesterunitednews.app.fixture.FixtureFragment;
import com.appian.manchesterunitednews.app.fixture.MatchDayFragment;
import com.appian.manchesterunitednews.app.table.TableFragment;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.admob.BannerAdMob;

import java.util.List;

public class LeagueFragment extends BaseStateFragment {
    private ViewGroup mAdViewContainer;

    private MatchPagerAdapter adapter;
    private ToolbarViewListener mToolBar;

    private BannerAdMob mBannerAdMob;

    private int mLeagueId;
    private int mSeasonId;
    private String mLeagueName;
    private int mTeamId;

    public static LeagueFragment newInstance(Bundle args) {
        LeagueFragment fragment = new LeagueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        adapter = new MatchPagerAdapter(fm);
        Bundle args = getArguments();
        if (args != null) {
            mLeagueId = args.getInt("league_id");
            mSeasonId = args.getInt("season_id");
            mLeagueName = args.getString("league_name", "");
        }

    }


    public void updateLeagueSeason(Bundle args) {
        if (args != null) {
            mLeagueId = args.getInt("league_id");
            mSeasonId = args.getInt("season_id");
            mLeagueName = args.getString("league_name", "");
        }
        updateTitle();
        List<Fragment> fragments = adapter.getStateFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof OnLeagueUpdatedListener) {
                ((OnLeagueUpdatedListener) fragment).onLeagueUpdated(mLeagueId, mSeasonId);
            }
        }
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

    private void initView(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_match);
        ViewPager viewPager = view.findViewById(R.id.view_pager_match);
        mAdViewContainer = view.findViewById(R.id.admob_banner_container);
        Context context = getContext();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        View view0 = View.inflate(context, R.layout.custom_tab_layout, null);
        view0.findViewById(R.id.icon_tab).setBackgroundResource(R.drawable.calendar);
        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        if(tab0 != null) {
            tab0.setCustomView(view0);
        }
        View view1 = View.inflate(context, R.layout.custom_tab_layout, null);
        view1.findViewById(R.id.icon_tab).setBackgroundResource(R.drawable.award);
        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        if(tab1 != null) {
            tab1.setCustomView(view1);
        }
        View view2 = View.inflate(context, R.layout.custom_tab_layout, null);
        view2.findViewById(R.id.icon_tab).setBackgroundResource(R.drawable.list);
        TabLayout.Tab tab2 = tabLayout.getTabAt(2);
        if(tab2 != null) {
            tab2.setCustomView(view2);
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        updateTitle();
    }


    @Override
    public void onResume() {
        super.onResume();
        List<Fragment> fragments = adapter.getStateFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof OnLeagueUpdatedListener) {
                ((OnLeagueUpdatedListener) fragment).onLeagueUpdated(mLeagueId, mSeasonId);
            }
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.league_fragment;
    }

    private class MatchPagerAdapter extends BaseStateFragmentPagerAdapter {

        MatchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FixtureFragment.newInstance(mLeagueId, mSeasonId, mTeamId, this);
                case 1:
                    return MatchDayFragment.newInstance(mLeagueId, mSeasonId, this);
                case 2:
                    return TableFragment.newInstance(mLeagueId, mSeasonId, this);

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarViewListener) {
            mToolBar = (ToolbarViewListener) context;
        }
        AppConfig appConfig = AppConfig.getInstance();
        mTeamId = appConfig.getTeamId(context);
        mBannerAdMob = new BannerAdMob(context, appConfig.getAdbMobMatchDetail(context));
        Utils.addAdmobTestDevice(mBannerAdMob);
    }

    private void updateTitle() {
        if (mToolBar != null) {
            mToolBar.changeToolbarTitle(mLeagueName);
        }
    }

}
