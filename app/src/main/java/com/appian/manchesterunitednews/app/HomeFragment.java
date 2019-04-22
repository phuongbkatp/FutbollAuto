package com.appian.manchesterunitednews.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.adapter.AdapterViewPager;
import com.appian.manchesterunitednews.app.fixture.presenter.TeamLastNextMatchPresenter;
import com.appian.manchesterunitednews.app.fixture.view.TeamLastNextMatchView;
import com.appian.manchesterunitednews.app.match.BannerFragment;
import com.appian.manchesterunitednews.app.news.NewsFragment;
import com.appian.manchesterunitednews.app.news.presenter.ListNewsPresenter;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.network.NetworkHelper;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.Tournament;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements TeamLastNextMatchView {
    private ViewPager mLastNextViewPager;
    private View mViewNoConnectivity;

    private ToolbarViewListener mToolbar;

    private List<Fragment> mLastNextFragments;
    private AdapterViewPager mNewsAdapterViewPager;
    private AdapterViewPager mLastNextAdapterViewPager;

    private int mTeamId = 0;
    private TeamLastNextMatchPresenter mTeamLastNextMatchPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig appConfig = AppConfig.getInstance();
        mTeamId = appConfig.getTeamId(getContext());
        List<Fragment> fList = new ArrayList<>();
        fList.add(NewsFragment.newInstance(ListNewsPresenter.TYPE_APP));
        fList.add(NewsFragment.newInstance(ListNewsPresenter.TYPE_TRENDING));
        fList.add(NewsFragment.newInstance(ListNewsPresenter.TYPE_VIDEO));
        mNewsAdapterViewPager = new AdapterViewPager(getChildFragmentManager(), fList);
        mLastNextFragments = new ArrayList<>();
        mLastNextAdapterViewPager = new AdapterViewPager(getChildFragmentManager(), mLastNextFragments);
        mTeamLastNextMatchPresenter = new TeamLastNextMatchPresenter();
        mTeamLastNextMatchPresenter.attachView(this);
        loadTeamLastNext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarViewListener) {
            mToolbar = (ToolbarViewListener) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
    }

    private void initLayout(View view) {
        TabLayout tabLayout = view.findViewById(R.id.materialTabHost);
        mLastNextViewPager = view.findViewById(R.id.viewpagerBanner);
        mViewNoConnectivity = view.findViewById(R.id.view_no_internet_connection);
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        ViewPager newsViewPager = view.findViewById(R.id.viewpager);

        Context context = getContext();
        newsViewPager.setAdapter(mNewsAdapterViewPager);
        newsViewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(newsViewPager);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        if (tab1 != null) {
            tab1.setText(getString(R.string.all_news));
        }
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        if (tab2 != null) {
            tab2.setText(getString(R.string.trend_news));
        }
        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        if (tab3 != null) {
            tab3.setText(getString(R.string.video_news));
        }
        mLastNextViewPager.setAdapter(mLastNextAdapterViewPager);

        indicator.setViewPager(mLastNextViewPager);
        setTitle();
    }

    @Override
    protected int getLayout() {
        return R.layout.home_layout;
    }

    private void loadTeamLastNext() {
        if (mTeamId == 0) {
            return;
        }
        mTeamLastNextMatchPresenter.loadMatchLastNext(mTeamId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTeamLastNextMatchPresenter.detachView();
    }

    private void setTitle() {
        if (mToolbar != null) {
            mToolbar.changeToolbarTitle(getString(R.string.home_menu));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkInternetConnection(NetworkHelper.isNetworkAvailable(getContext()));
    }

    @Override
    public void showMatchLastNext(List<Event> data) {
        if (data.isEmpty() || getView() == null) {
            return;
        }
        mLastNextFragments.clear();
        for (Event event : data) {
            Tournament tournament = event.getTournament();
            BannerFragment fragment = BannerFragment.newInstance(tournament.getName(), event.getNameStadium(),
                    event.getStartTimestamp(), event.getStatusDescription(), event.getStatus().getType(), event.getId(),
                    event.getHomeTeam().getName(), event.getHomeTeam().getId(),
                    event.getAwayTeam().getName(), event.getAwayTeam().getId(),
                    event.getCurrentHomeScore(), event.getCurrentAwayScore()
            );
            mLastNextFragments.add(fragment);
        }
        mLastNextAdapterViewPager.notifyDataSetChanged();
        int initPosition = 0;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < data.size(); i++) {
            Event event = data.get(i);
            String type = "";
            if (event.getStatus() != null) {
                type = event.getStatus().getType();
            }
            if (Constant.SOFA_MATCH_STATUS_IN_PROGRESS.equalsIgnoreCase(type)) {
                initPosition = i;
                break;
            }
            if(Constant.SOFA_MATCH_STATUS_FINISHED.equalsIgnoreCase(type) && currentTime - event.getStartTimestamp() < 86400000) {
                initPosition = i; // Latest finished match.
                break;
            }
            if (Constant.SOFA_MATCH_STATUS_NOT_STARTED.equalsIgnoreCase(type)) {
                initPosition = i;
                break;
            }
        }
        mLastNextViewPager.setCurrentItem(initPosition, false);
    }

    private void checkInternetConnection(boolean isConnected) {
        if (mViewNoConnectivity == null) {
            return;
        }
        int visible = (isConnected) ? View.GONE : View.VISIBLE;
        mViewNoConnectivity.setVisibility(visible);
    }

}
