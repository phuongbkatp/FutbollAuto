package com.appian.manchesterunitednews.app.match.videohighlight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.app.detailnews.presenter.DetailNewsPresenter;
import com.appian.manchesterunitednews.app.detailnews.view.DetailNewsView;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.appnet.android.football.fbvn.data.News;

public class VideoActivity extends BaseActivity implements DetailNewsView {
    private WebView mWvContent;
    private View mViewLoading;

    private DetailNewsPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mViewLoading = findViewById(R.id.progress_loading);
        View btnBack = findViewById(R.id.rl_back_to_app);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mWvContent = findViewById(R.id.video_webview);
        mWvContent.setWebChromeClient(new VideoWebChromeClient());
        mWvContent.setWebViewClient(new VideoWebViewClient());
        ViewHelper.improveWebSetting(mWvContent);
        int id = 0;
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }
        mPresenter = new DetailNewsPresenter(new NewsInteractor());
        mPresenter.attachView(this);
        //mPresenter.loadNewsDetail(id);
    }

    @Override
    public void showNews(DetailNewsAuto news) {
/*        if (mWvContent != null && news != null && !TextUtils.isEmpty(news.getUrl())) {
            mWvContent.loadUrl(news.getUrl());
        }*/
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            finish();
            ViewHelper.launchMainScreen(getApplicationContext());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private class VideoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mViewLoading.setVisibility(View.GONE);
        }
    }

    private class VideoWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        VideoWebChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            return BitmapFactory.decodeResource(VideoActivity.this.getApplicationContext().getResources(), R.id.frame_full_screen);
        }

        @Override
        public void onHideCustomView() {
            ((FrameLayout) VideoActivity.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            VideoActivity.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            VideoActivity.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        @Override
        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = VideoActivity.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = VideoActivity.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) VideoActivity.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            int newUiOptions = VideoActivity.this.getWindow().getDecorView().getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 19) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else if(Build.VERSION.SDK_INT >= 16) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            } else {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            VideoActivity.this.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);// 3846);
        }
    }
}
