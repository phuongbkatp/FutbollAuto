package com.appian.manchesterunitednews.app.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.detailnews.DetailArticleActivity;
import com.appian.manchesterunitednews.app.news.presenter.ListNewsPresenter;
import com.appian.manchesterunitednews.app.news.view.ListNewsView;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.util.EndlessRecyclerViewScrollListener;
import com.appian.manchesterunitednews.util.EventHelper;
import com.appian.manchesterunitednews.util.ItemClickSupport;
import com.appian.manchesterunitednews.util.SimpleDividerItemDecoration;
import com.appnet.android.football.fbvn.data.NewsAuto;

import java.util.List;

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ListNewsView {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLlNoData;
    private ContentLoadingProgressBar mLoadingView;
    private NewsRecycleAdapter mNewsAdapter;

    private int mNewsType = 0;
    private int mCurrentPage = 1;
    private int mStartingPage = 1;
    private String mLanguage;
    private String mTeam;

    private ListNewsPresenter mListNewsPresenter;
    private EndlessRecyclerViewScrollListener mOnLoadMoreListener;

    public static NewsFragment newInstance(int appId, int type, int id) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putInt("appId", appId);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewsFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppConfig config = AppConfig.getInstance();
        mTeam = config.getAppKey();
        mLanguage = Language.getLanguage(context);
        mCurrentPage = mStartingPage;
        mNewsAdapter = new NewsRecycleAdapter(getContext(), context.getResources().getString(R.string.facebook_ads_list_news_feed), 5);
        Bundle agrs = getArguments();
        if (agrs != null) {
            mNewsType = agrs.getInt("type");
        }
        mListNewsPresenter = new ListNewsPresenter(new NewsInteractor());
        mListNewsPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView lvNews = view.findViewById(R.id.lv_news);
        mSwipeRefreshLayout = view.findViewById(R.id.news_swipe_refresh_layout);
        mLlNoData = view.findViewById(R.id.ll_news_no_data);
        mLoadingView = view.findViewById(R.id.loading_view);

        mCurrentPage = mStartingPage;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        lvNews.setLayoutManager(layoutManager);
        lvNews.setAdapter(mNewsAdapter);
        ItemClickSupport.addTo(lvNews).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                NewsAuto item = mNewsAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                startDetailArticleActivity(item.getLink());
            }
        });
        mOnLoadMoreListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                loadNews(mTeam, mLanguage);
            }
        };
        lvNews.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        lvNews.addOnScrollListener(mOnLoadMoreListener);
        loadNews(mTeam, mLanguage);
        mNewsAdapter.setStepAds(5);
        mNewsAdapter.loadAd();
    }


    private void startDetailArticleActivity(String link) {
        Intent intent = new Intent(this.getActivity(), DetailArticleActivity.class);
        intent.putExtra(DetailArticleActivity.LINK, link);
        startActivity(intent);
        logClickItemEvent();
    }

    private void logClickItemEvent() {
        String category = "latest";
        if(mNewsType == ListNewsPresenter.TYPE_TRENDING) {
            category = "top";
        } else if(mNewsType == ListNewsPresenter.TYPE_VIDEO) {
            category = "related";
        }
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("lang", Language.getLanguage(getContext()));
        EventHelper.log(getContext(), EventHelper.EVENT_TAP_NEWS_DETAIL, bundle);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void onRefresh() {
        loadNews(mTeam, mLanguage);
        mOnLoadMoreListener.resetState();
        mCurrentPage = mStartingPage;
    }

    private void loadNews(String team, String language) {
        showLoading(true);
        mListNewsPresenter.loadListNews(mNewsType,team, language);
    }

    @Override
    public void showListNews(List<NewsAuto> data) {
        showLoading(false);
        if (data != null) {
            if (mCurrentPage == mStartingPage) {
                mNewsAdapter.updateData(data);
            } else {
                mNewsAdapter.addData(data);
            }
        }
        if (getView() != null) {
            int visible = mNewsAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE;
            mLlNoData.setVisibility(visible);
        }
    }

    @Override
    public void onLoadListNewsFail() {
        showLoading(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListNewsPresenter.detachView();
    }

    private void showLoading(boolean isLoading) {
        if (getView() != null) {
            mSwipeRefreshLayout.setRefreshing(isLoading);
        }
        if(!isLoading) {
            mLoadingView.hide();
        }
    }
}
