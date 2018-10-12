package com.appian.manchesterunitednews.app.news.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.news.view.ListNewsView;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appnet.android.football.fbvn.data.News;
import com.appnet.android.football.fbvn.data.NewsAuto;

import java.util.List;

public class ListNewsPresenter extends BasePresenter<ListNewsView> implements OnResponseListener<List<NewsAuto>> {
    public static final int TYPE_APP = 1;
    public static final int TYPE_CATEGORY = 2;
    public static final int TYPE_MATCH = 3;
    public static final int TYPE_VIDEO_MATCH = 4;

    private final NewsInteractor mInteractor;

    public ListNewsPresenter(NewsInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onSuccess(List<NewsAuto> data) {
        if (getView() == null) {
            return;
        }
        getView().showListNews(data);
    }

    @Override
    public void onFailure(String error) {
        if (getView() == null) {
            return;
        }
        getView().onLoadListNewsFail();
    }

    public void loadListNews(int type) {
        switch (type) {
            case TYPE_APP:
                mInteractor.loadNewsLatest(this);
                break;

        }
    }
}
