package com.appian.manchesterunitednews.app.match.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.match.view.MatchStatisticView;
import com.appian.manchesterunitednews.data.interactor.MatchInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appnet.android.football.sofa.data.Statistic;

public class MatchStatisticPresenter extends BasePresenter<MatchStatisticView> implements OnResponseListener<Statistic> {
    private final MatchInteractor mInteractor;

    public MatchStatisticPresenter(MatchInteractor interactor ) {
        mInteractor = interactor;
    }


    public void loadMatchStatic(int matchId) {
        if(matchId == 0 || getView() == null) {
            return;
        }
        mInteractor.loadStatistics(matchId, this);
    }

    @Override
    public void onSuccess(Statistic data) {
        if(getView() != null) {
            getView().showMatchStatistic(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
