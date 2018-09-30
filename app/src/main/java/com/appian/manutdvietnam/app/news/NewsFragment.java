package com.appian.manutdvietnam.app.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseFragment;
import com.appian.manutdvietnam.app.detailnews.DetailArticleActivity;
import com.appian.manutdvietnam.app.news.presenter.ListNewsPresenter;
import com.appian.manutdvietnam.app.news.view.ListNewsView;
import com.appian.manutdvietnam.data.app.AppConfig;
import com.appian.manutdvietnam.data.app.AppConfigManager;
import com.appian.manutdvietnam.data.app.Language;
import com.appian.manutdvietnam.data.interactor.NewsInteractor;
import com.appian.manutdvietnam.util.CustomDialogFragment;
import com.appian.manutdvietnam.util.EndlessRecyclerViewScrollListener;
import com.appian.manutdvietnam.util.ItemClickSupport;
import com.appian.manutdvietnam.util.SimpleDividerItemDecoration;
import com.appnet.android.football.fbvn.data.News;

import java.util.List;

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ListNewsView {
    private static final int LIMIT_NEWS = 6;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLlNoData;

    private NewsRecycleAdapter mNewsAdapter;

    private int mNewsType = 0;
    private int mTypeId = 0;
    private int mCurrentPage = 1;
    private int mStartingPage = 1;
    private String mLanguage = Language.getDefaultLanguage();
    private int mAppId = 0;

    private ListNewsPresenter mListNewsPresenter;
    private EndlessRecyclerViewScrollListener mOnLoadMoreListener;
    CustomDialogFragment mVideoAlertDialog;

    public static NewsFragment newInstance(int appId, int type, int id) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putInt("appId", appId);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig config = AppConfigManager.getInstance().getAppConfig(getContext());
        mLanguage = AppConfigManager.getInstance().getLanguage(getContext());
        mCurrentPage = mStartingPage;
        mNewsAdapter = new NewsRecycleAdapter(getContext(), config.getFbAdsNative1());
        Bundle agrs = getArguments();
        if (agrs != null) {
            mNewsType = agrs.getInt("type");
            mTypeId = agrs.getInt("id");
            mAppId = agrs.getInt("appId");
        }
        mListNewsPresenter = new ListNewsPresenter(new NewsInteractor());
        mListNewsPresenter.attachView(this);
        loadNews(mStartingPage);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView lvNews = view.findViewById(R.id.lv_news);
        mSwipeRefreshLayout = view.findViewById(R.id.news_swipe_refresh_layout);
        mLlNoData = view.findViewById(R.id.ll_news_no_data);

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
                News item = mNewsAdapter.getItem(position);
                if(item == null) {
                    return;
                }
                if(item.isVideoType()) {
                    mVideoAlertDialog = CustomDialogFragment.newInstance(item.getId());
                    mVideoAlertDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                    if(getFragmentManager() != null) {
                        mVideoAlertDialog.show(getFragmentManager(), "DialogFragment");
                    }
                } else {
                    startDetailArticleActivity(position);
                }
            }
        });
        mOnLoadMoreListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                mCurrentPage++;
                loadNews(mCurrentPage);
            }
        };
        lvNews.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        lvNews.addOnScrollListener(mOnLoadMoreListener);
        mNewsAdapter.loadAd();
    }


    private void startDetailArticleActivity(int pos) {
        Intent intent = new Intent(this.getActivity(), DetailArticleActivity.class);
        List<News> nextItems = mNewsAdapter.getNextItems(pos, 3);
        int[] ids = new int[nextItems.size()+1];
        News itemNews = mNewsAdapter.getItem(pos);
        if (itemNews == null) {
            return;
        }
        ids[0] = itemNews.getId();
        int index = 1;
        for (News item : nextItems) {
            ids[index] = item.getId();
            index++;
        }
        intent.putExtra(DetailArticleActivity.EXTRA_NEWS_LIST_ID, ids);
        startActivity(intent);

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void onRefresh() {
        loadNews(mStartingPage);
        mOnLoadMoreListener.resetState();
        mCurrentPage = mStartingPage;
    }

    private void loadNews(int page) {
        showLoading(true);
        mListNewsPresenter.loadListNews(mAppId, mNewsType, mTypeId, mLanguage, page, LIMIT_NEWS);
    }

    @Override
    public void showListNews(List<News> data) {
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
    public void onPause() {
        super.onPause();
        if (mVideoAlertDialog != null) {
            mVideoAlertDialog.dismiss();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mListNewsPresenter.detachView();
    }

    private void showLoading(boolean isLoading) {
        if(getView() != null) {
            mSwipeRefreshLayout.setRefreshing(isLoading);
        }
    }
}
