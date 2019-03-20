package com.appian.manchesterunitednews.app.match.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.match.view.MatchVideoView;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.data.interactor.OnDetailNewsResponseListener;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;

public class MatchVideoPresenter extends BasePresenter<MatchVideoView> implements OnDetailNewsResponseListener<DetailNewsAuto> {
    private final NewsInteractor mInteractor;

    public MatchVideoPresenter(NewsInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadMatchVideo(String home, String away) {
        if(getView() == null) {
            return;
        }
        mInteractor.loadMatchVideo(home, away, this);
    }


    @Override
    public void onSuccess(DetailNewsAuto data) {
        if(getView() != null) {
            getView().showMatchVideo(data);
        }
    }

    @Override
    public void onFailure(String error) {
    }
}
