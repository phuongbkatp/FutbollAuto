package com.appian.manchesterunitednews.app.news.view;

import com.appnet.android.football.fbvn.data.News;

import java.util.List;

public interface ListNewsView {
    void showListNews(List<News> data);
    void onLoadListNewsFail();
}
