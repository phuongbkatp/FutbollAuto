package com.appian.manchesterunitednews.app.news.view;

import com.appnet.android.football.fbvn.data.NewsAuto;

import java.util.List;

public interface ListNewsView {
    void showListNews(List<NewsAuto> data);
    void onLoadListNewsFail();
}
