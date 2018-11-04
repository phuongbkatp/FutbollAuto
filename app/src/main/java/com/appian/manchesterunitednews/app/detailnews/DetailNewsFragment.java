package com.appian.manchesterunitednews.app.detailnews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.comment.CommentsAdapter;
import com.appian.manchesterunitednews.app.detailnews.presenter.DetailNewsPresenter;
import com.appian.manchesterunitednews.app.detailnews.view.DetailNewsView;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.fbvn.data.Cell;
import com.appnet.android.football.fbvn.data.ColumnHeader;
import com.appnet.android.football.fbvn.data.ContentDetailNewsAuto;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.appnet.android.football.fbvn.data.RowHeader;
import com.bumptech.glide.Glide;
import com.marcinmoskala.videoplayview.VideoPlayView;

import java.util.List;

public class DetailNewsFragment extends BaseStateFragment implements DetailNewsView {
    private TextView mTvTitle;
    private TextView mTvDescription;
    private TextView mTvSource;
    private ImageView mImgThumbnail;
    private VideoPlayView mContentVideo;
    private RelativeLayout mRlVideo;
    private ProgressBar progressBar;
    private TextView mTvTimeNews;
    private RecyclerView ll_content;
    private ContentLoadingProgressBar mLoadingView;

    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;

    private String link;
    private DetailNewsAuto mNews;

    private DetailNewsRecycleAdapter mDetailNewsAdapter;
    private DetailNewsPresenter mPresenter;

    private CommentsAdapter mAdapter;


    @Override
    public void showNews(DetailNewsAuto news) {
        mNews = news;
        fillData();
    }

    public static DetailNewsFragment newInstance(int newsId) {
        Bundle args = new Bundle();
        args.putInt("news_id", newsId);
        DetailNewsFragment fragment = new DetailNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            link = args.getString("link");
        }
        mAdapter = new CommentsAdapter(getContext());
        mPresenter = new DetailNewsPresenter(new NewsInteractor());
        mPresenter.attachView(this);
        mPresenter.loadNewsDetail(link);

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail_article;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fillData();
    }

    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_news_detail_title);
        mTvDescription = view.findViewById(R.id.tv_news_detail_description);
        mTvSource = view.findViewById(R.id.tv_news_detail_source);
        mImgThumbnail = view.findViewById(R.id.img_news_detail_thumbnail);
        mContentVideo = view.findViewById(R.id.content_video);
        mLoadingView = view.findViewById(R.id.loading_view);

        mRlVideo = view.findViewById(R.id.rl_video);
        mTvTimeNews = view.findViewById(R.id.tv_time_news);
        ll_content = view.findViewById(R.id.ll_content);
        AppConfig config = AppConfig.getInstance();

        mDetailNewsAdapter = new DetailNewsRecycleAdapter(getContext(), "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ll_content.setLayoutManager(layoutManager);
        ll_content.setAdapter(mDetailNewsAdapter);
    }

    private void fillData() {
        mLoadingView.hide();
        if (mNews == null || getView() == null) {
            return;
        }
        mTvTimeNews.setText((Utils.formatTime(getContext(), mNews.getMetaDetailNewsAuto().getCreatedTime())));
        if (TextUtils.isEmpty(mNews.getMetaDetailNewsAuto().getDescription())) {
            mTvDescription.setVisibility(View.GONE);
        } else {
            mTvDescription.setVisibility(View.VISIBLE);
            mTvDescription.setText(mNews.getMetaDetailNewsAuto().getDescription());
        }
        mTvTitle.setText(mNews.getMetaDetailNewsAuto().getTitle());
        if (!TextUtils.isEmpty(mNews.getSource())) {
            mTvSource.setText(mNews.getSource());
        }
        String url = mNews.getMetaDetailNewsAuto().getVideo();
        if (!url.equals("")) {
            mRlVideo.setVisibility(View.VISIBLE);
            mContentVideo.setVideoUrl(url);
            Glide.with(this).load(mNews.getMetaDetailNewsAuto().getImage()).into(mContentVideo.getImageView());
        } else {
            mRlVideo.setVisibility(View.GONE);
        }
        ImageLoader.displayImage(mNews.getMetaDetailNewsAuto().getImage(), mImgThumbnail);
        List<ContentDetailNewsAuto> listContent = mNews.getContentDetailNewsAuto();
        if (listContent.size() == 0) {
            return;
        }
        mDetailNewsAdapter.updateData(listContent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
