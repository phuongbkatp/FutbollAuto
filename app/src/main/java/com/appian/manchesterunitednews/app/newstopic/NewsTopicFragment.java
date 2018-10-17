package com.appian.manchesterunitednews.app.newstopic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.ToolbarViewListener;
import com.appian.manchesterunitednews.app.adapter.AdapterViewPager;
import com.appian.manchesterunitednews.app.news.NewsFragment;
import com.appian.manchesterunitednews.app.news.presenter.ListNewsPresenter;
import com.appian.manchesterunitednews.app.news.view.ListNewsView;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.AppConfigManager;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appnet.android.football.fbvn.data.News;
import com.appnet.android.football.fbvn.data.NewsAuto;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class NewsTopicFragment extends BaseFragment implements ListNewsView {
    private ToolbarViewListener mToolbar;

    private ListNewsPresenter mListNewsPresenter;

    private List<Fragment> mTopicFragments;
    private AdapterViewPager mNewsAdapterViewPager;
    private AdapterViewPager mTopicAdapterViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(getContext());
        List<Fragment> fList = new ArrayList<>();
        fList.add(NewsFragment.newInstance(appConfig.getAppId(), ListNewsPresenter.TYPE_APP, appConfig.getTabId(3)));
        fList.add(NewsFragment.newInstance(appConfig.getAppId(), ListNewsPresenter.TYPE_APP, appConfig.getTabId(4)));
        fList.add(NewsFragment.newInstance(appConfig.getAppId(), ListNewsPresenter.TYPE_APP, appConfig.getTabId(5)));
        mNewsAdapterViewPager = new AdapterViewPager(getChildFragmentManager(), fList);
        mTopicFragments = new ArrayList<>();
        mTopicAdapterViewPager = new AdapterViewPager(getChildFragmentManager(), mTopicFragments);
        mListNewsPresenter = new ListNewsPresenter(new NewsInteractor());
        mListNewsPresenter.attachView(this);
        String language = AppConfigManager.getInstance().getLanguage(getContext());
        //mListNewsPresenter.loadListNews(appConfig.getAppId(), ListNewsPresenter.TYPE_CATEGORY, appConfig.getCategoryEventId(), language, 1, 10);
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
        TabLayout tabHost = view.findViewById(R.id.materialTabHost);
        ViewPager topicViewPager = view.findViewById(R.id.viewpagerBanner);
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        ViewPager newsViewPager = view.findViewById(R.id.viewpager);

        newsViewPager.setAdapter(mNewsAdapterViewPager);
        newsViewPager.setOffscreenPageLimit(3);

        tabHost.setupWithViewPager(newsViewPager);
        Context context = getContext();
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(context);
        TabLayout.Tab tab0 = tabHost.getTabAt(0);
        if(tab0 != null) {
            tab0.setText(appConfig.getTabTitle(context, 3));
        }
        TabLayout.Tab tab1 = tabHost.getTabAt(1);
        if(tab1 != null) {
            tab1.setText(appConfig.getTabTitle(context, 4));
        }
        TabLayout.Tab tab2 = tabHost.getTabAt(2);
        if(tab2 != null) {
            tab2.setText(appConfig.getTabTitle(context, 5));
        }
        topicViewPager.setAdapter(mTopicAdapterViewPager);
        indicator.setViewPager(topicViewPager);

        setTitle();
    }

    @Override
    protected int getLayout() {
        return R.layout.news_topic_layout;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mListNewsPresenter.detachView();
    }

    private void setTitle() {
        if (mToolbar != null) {
            mToolbar.changeToolbarTitle(getString(R.string.news_topic_menu));
        }
    }

    @Override
    public void showListNews(List<NewsAuto> data) {
        if(data == null) {
            return;
        }
/*        for (NewsAuto news : data) {
            HeadNewsFragment fragment = HeadNewsFragment.newInstance(news.getId(), news.getTitle(),
                    news.getThumbnail(), news.getCreatedAt(), news.getCommentCount());
            mTopicFragments.add(fragment);
        }*/
        mTopicAdapterViewPager.notifyDataSetChanged();
    }

    @Override
    public void onLoadListNewsFail() {

    }
}
