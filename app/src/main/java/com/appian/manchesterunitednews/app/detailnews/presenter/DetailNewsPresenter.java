package com.appian.manchesterunitednews.app.detailnews.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.detailnews.view.DetailNewsView;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.data.interactor.OnDetailNewsResponseListener;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.appnet.android.football.fbvn.data.News;

import java.util.List;

public class DetailNewsPresenter extends BasePresenter<DetailNewsView> implements OnDetailNewsResponseListener<DetailNewsAuto> {
    private final NewsInteractor mInteractor;

    public DetailNewsPresenter(NewsInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadNewsDetail(String link) {
        if(link == "") {
            return;
        }
        mInteractor.loadNewsDetail(link, this);
    }

    @Override
    public void onSuccess(DetailNewsAuto data) {
        if(getView() == null) {
            return;
        }
        getView().showNews(data);
    }

    @Override
    public void onFailure(String error) {

    }
}
