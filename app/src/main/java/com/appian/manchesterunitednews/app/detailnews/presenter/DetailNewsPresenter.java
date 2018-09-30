package com.appian.manchesterunitednews.app.detailnews.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.detailnews.view.DetailNewsView;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appnet.android.football.fbvn.data.News;

public class DetailNewsPresenter extends BasePresenter<DetailNewsView> implements OnResponseListener<News> {
    private final NewsInteractor mInteractor;

    public DetailNewsPresenter(NewsInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadNewsDetail(int id) {
        if(id == 0) {
            return;
        }
        mInteractor.loadNewsDetail(id, this);
    }

    @Override
    public void onSuccess(News data) {
        if(getView() == null) {
            return;
        }
        getView().showNews(data);
    }

    @Override
    public void onFailure(String error) {

    }
}
