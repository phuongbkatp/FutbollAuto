package com.appian.manutdvietnam.app.news.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.news.view.ListNewsView;
import com.appian.manutdvietnam.data.interactor.NewsInteractor;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appnet.android.football.fbvn.data.News;

import java.util.List;

public class ListNewsPresenter extends BasePresenter<ListNewsView> implements OnResponseListener<List<News>> {
    public static final int TYPE_APP = 1;
    public static final int TYPE_CATEGORY = 2;
    public static final int TYPE_MATCH = 3;
    public static final int TYPE_VIDEO_MATCH = 4;

    private final NewsInteractor mInteractor;

    public ListNewsPresenter(NewsInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onSuccess(List<News> data) {
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

    public void loadListNews(int appId, int type, int id, String language, int page, int limit) {
        switch (type) {
            case TYPE_APP:
                mInteractor.loadNewsByApp(appId, language, page, limit, this);
                break;
            case TYPE_CATEGORY:
                mInteractor.loadNewsByCategory(appId, id, language, page, limit, this);
                break;
            case TYPE_MATCH:
                mInteractor.loadNewsByMatch(id, language, page, limit, this);
                break;
            case TYPE_VIDEO_MATCH:
                mInteractor.loadVideoOfMatch(id, page, limit, this);
                break;
        }
    }
}
