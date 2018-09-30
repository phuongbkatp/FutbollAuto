package com.appian.manutdvietnam.app.detailnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseStateFragment;
import com.appian.manutdvietnam.app.comment.CommentActivity;
import com.appian.manutdvietnam.app.comment.CommentsAdapter;
import com.appian.manutdvietnam.app.comment.presenter.CommentsPresenter;
import com.appian.manutdvietnam.app.comment.view.CommentsView;
import com.appian.manutdvietnam.app.detailnews.presenter.DetailNewsPresenter;
import com.appian.manutdvietnam.app.detailnews.view.DetailNewsView;
import com.appian.manutdvietnam.data.interactor.CommentsInteractor;
import com.appian.manutdvietnam.data.interactor.NewsInteractor;
import com.appian.manutdvietnam.util.ImageLoader;
import com.appian.manutdvietnam.util.Utils;
import com.appian.manutdvietnam.util.ViewHelper;
import com.appnet.android.football.fbvn.data.Comment;
import com.appnet.android.football.fbvn.data.News;

import java.util.List;

public class DetailNewsFragment extends BaseStateFragment implements DetailNewsView, CommentsView {
    private TextView mTvTitle;
    private TextView mTvDescription;
    private WebView mWvContent;
    private TextView mTvSource;
    private TextView mTvCommentCount;
    private ImageView mImgThumbnail;
    private TextView mTvTimeNews;
    private View mViewLoading;
    private TextView mTvNewestComments;
    private Button mBtnLoadMoreComments;

    private DetailNewsPresenter mPresenter;

    private OnButtonClickListener mOnButtonClickListener;
    private CommentsAdapter mAdapter;
    private CommentsPresenter mCommentsPresenter;

    @Override
    public void showNews(News news) {
        mNews = news;
        fillData();
    }

    @Override
    public void showComments(List<Comment> data) {
        if(data == null) {
            return;
        }
        mAdapter.updateData(data);
    }

    @Override
    public void onLoadCommentsFail() {

    }

    interface OnButtonClickListener {
        void onButtonClicked(View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnButtonClickListener = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context).getLocalClassName()
                    + " must implement OnButtonClickListener");
        }
    }

    private int mNewsId = 0;
    private News mNews;

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
            mNewsId = args.getInt("news_id", 0);
        }
        mAdapter = new CommentsAdapter(getContext());
        mPresenter = new DetailNewsPresenter(new NewsInteractor());
        mPresenter.attachView(this);
        mPresenter.loadNewsDetail(mNewsId);
        mCommentsPresenter = new CommentsPresenter(new CommentsInteractor());
        mCommentsPresenter.attachView(this);
        loadComments();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail_article;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mWvContent.setWebChromeClient(new WebChromeClient());
        ViewHelper.improveWebSetting(mWvContent);
        fillData();
    }

    private void initView(View view) {
        mViewLoading = view.findViewById(R.id.progress_loading);
        mTvTitle = view.findViewById(R.id.tv_news_detail_title);
        mTvDescription = view.findViewById(R.id.tv_news_detail_description);
        mWvContent = view.findViewById(R.id.wv_news_detail_content);
        mTvSource = view.findViewById(R.id.tv_news_detail_source);
        mTvCommentCount = view.findViewById(R.id.tv_news_detail_comment_count);
        mImgThumbnail = view.findViewById(R.id.img_news_detail_thumbnail);
        mTvTimeNews = view.findViewById(R.id.tv_time_news);
        mTvNewestComments = view.findViewById(R.id.tv_newest_comments);
        mBtnLoadMoreComments = view.findViewById(R.id.btn_load_more_comments);
        View btnNext = view.findViewById(R.id.ll_next_layout);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnButtonClickListener != null) {
                    mOnButtonClickListener.onButtonClicked(view);
                }
            }
        });
        View.OnClickListener onShowComments = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreComments();
            }
        };
        View btnComment = view.findViewById(R.id.fl_comment_icon);
        btnComment.setOnClickListener(onShowComments);
        mBtnLoadMoreComments.setOnClickListener(onShowComments);
        mWvContent.setFocusable(false);
        mWvContent.setWebViewClient(new NewsWebViewClient());
        RecyclerView lvComments = view.findViewById(R.id.lv_comment);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvComments.setLayoutManager(llm);
        lvComments.setFocusable(false);
        lvComments.setAdapter(mAdapter);
    }

    private void fillData() {
        if (mNews == null || getView() == null) {
            return;
        }
        mTvTimeNews.setText((Utils.formatTime(getContext(), mNews.getCreatedAt())));
        if (TextUtils.isEmpty(mNews.getDescription())) {
            mTvDescription.setVisibility(View.GONE);
        } else {
            mTvDescription.setVisibility(View.VISIBLE);
            mTvDescription.setText(mNews.getDescription());
        }
        mTvTitle.setText(mNews.getTitle());
        mWvContent.setVisibility(View.VISIBLE);
        mWvContent.loadData(mNews.getContent(), "text/html; charset=utf-8", "utf-8");
        if (!TextUtils.isEmpty(mNews.getSource())) {
            mTvSource.setText(mNews.getSource());
        }
        mTvCommentCount.setText(String.valueOf(mNews.getCommentCount()));
        ImageLoader.displayImage(mNews.getThumbnail(), mImgThumbnail);
        if(mNews.getCommentCount() > 0) {
            mTvNewestComments.setVisibility(View.VISIBLE);
            mBtnLoadMoreComments.setText(R.string.load_more_comments);
        } else {
            mTvNewestComments.setVisibility(View.GONE);
            mBtnLoadMoreComments.setText(R.string.to_be_your_first_comment);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mCommentsPresenter.detachView();
    }

    private class NewsWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mViewLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void loadComments() {
        mCommentsPresenter.loadComments("news", mNewsId, 1, 5);
    }

    private void loadMoreComments() {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra(CommentActivity.KEY_ID, mNewsId);
        startActivity(intent);
    }
}
