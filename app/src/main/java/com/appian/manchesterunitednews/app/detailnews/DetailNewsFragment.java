package com.appian.manchesterunitednews.app.detailnews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.detailnews.presenter.DetailNewsPresenter;
import com.appian.manchesterunitednews.app.detailnews.view.DetailNewsView;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.admob.BannerAdMob;
import com.appnet.android.football.fbvn.data.ContentDetailNewsAuto;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.bumptech.glide.Glide;
import com.marcinmoskala.videoplayview.VideoPlayView;

import java.util.ArrayList;
import java.util.List;

public class DetailNewsFragment extends BaseStateFragment implements DetailNewsView {
    private TextView mTvTitle;
    private TextView mTvDescription;
    private TextView mTvSource;
    private ImageView mImgThumbnail;
    private VideoPlayView mContentVideo;
    private RelativeLayout mRlVideo;
    private TextView mTvTimeNews;
    private ContentLoadingProgressBar mLoadingView;
    private ViewGroup mAdViewContainer;

    private String link;
    private DetailNewsAuto mNews;

    private DetailNewsRecycleAdapter mDetailNewsAdapter;
    private DetailNewsPresenter mPresenter;

    private BannerAdMob mBannerAdMob;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBannerAdMob = new BannerAdMob(context, AppConfig.getInstance().getAdbMobNewsDetail(context));
        Utils.addAdmobTestDevice(mBannerAdMob);
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
        RecyclerView ll_content = view.findViewById(R.id.ll_content);
        mAdViewContainer = view.findViewById(R.id.admob_banner_container);

        mDetailNewsAdapter = new DetailNewsRecycleAdapter(getContext(), "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ll_content.setLayoutManager(layoutManager);
        ll_content.setNestedScrollingEnabled(false);
        ll_content.setAdapter(mDetailNewsAdapter);
        initAd(view);
    }

    private void initAd(View root) {
        Context context = getContext();
        if(context == null) {
            return;
        }
        //
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
        // Multi video
        List<ContentDetailNewsAuto> newListContent = new ArrayList<>();
        for(ContentDetailNewsAuto item : listContent) {
            if(!item.getType().equalsIgnoreCase("video")) {
                newListContent.add(item);
                continue;
            }
            String link = item.getLinkImg();
            if(TextUtils.isEmpty(link)) {
                continue;
            }
            String[] links = link.split(",");
            for(String src : links) {
                ContentDetailNewsAuto newItem = new ContentDetailNewsAuto();
                newItem.setLinkImg(src);
                newItem.setType(item.getType());
                newItem.setText(item.getText());
                newListContent.add(newItem);
            }
        }
        mDetailNewsAdapter.updateData(newListContent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
