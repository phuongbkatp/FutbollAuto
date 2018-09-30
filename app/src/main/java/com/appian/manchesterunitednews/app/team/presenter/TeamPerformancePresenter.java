package com.appian.manchesterunitednews.app.team.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.team.view.TeamPerformanceView;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.data.interactor.TeamInteractor;
import com.appnet.android.football.sofa.data.Performance;

import java.util.List;

public class TeamPerformancePresenter extends BasePresenter<TeamPerformanceView> implements OnResponseListener<List<Performance>> {
    private final TeamInteractor mInteractor;

    public TeamPerformancePresenter(TeamInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadTeamPerformance(int teamId) {
        if(getView() == null) {
            return;
        }
        mInteractor.loadTeamPerformance(teamId, this);
    }

    @Override
    public void onSuccess(List<Performance> data) {
        if(getView() != null) {
            getView().showTeamPerformance(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
