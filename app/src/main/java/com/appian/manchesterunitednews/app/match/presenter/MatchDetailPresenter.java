package com.appian.manchesterunitednews.app.match.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.match.view.MatchDetailView;
import com.appian.manchesterunitednews.data.interactor.MatchInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appnet.android.football.sofa.data.Event;

public class MatchDetailPresenter extends BasePresenter<MatchDetailView> implements OnResponseListener<Event> {
    private final MatchInteractor mInteractor;

    public MatchDetailPresenter(MatchInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadMatchDetail(int matchId) {
        if(matchId == 0 || getView() == null) {
            return;
        }
        mInteractor.loadMatchDetail(matchId, this);
    }

    @Override
    public void onSuccess(Event data) {
        if(getView() != null) {
            getView().showMatchDetail(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
