package com.appian.manutdvietnam.app.match.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.match.view.MatchStatisticView;
import com.appian.manutdvietnam.data.interactor.MatchInteractor;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appnet.android.football.sofa.data.StatisticsData;

public class MatchStatisticPresenter extends BasePresenter<MatchStatisticView> implements OnResponseListener<StatisticsData> {
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
    public void onSuccess(StatisticsData data) {
        if(getView() != null) {
            getView().showMatchStatistic(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
