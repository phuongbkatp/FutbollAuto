package com.appian.manutdvietnam.app.team.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.team.view.TeamDetailView;
import com.appian.manutdvietnam.app.team.view.TeamPerformanceView;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appian.manutdvietnam.data.interactor.TeamInteractor;
import com.appnet.android.football.sofa.data.Performance;
import com.appnet.android.football.sofa.data.Team;

import java.util.List;

public class TeamDetailPresenter extends BasePresenter<TeamDetailView> implements OnResponseListener<Team> {
    private final TeamInteractor mInteractor;

    public TeamDetailPresenter(TeamInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadTeamDetail(int teamId) {
        if(getView() == null) {
            return;
        }
        mInteractor.loadTeamDetail(teamId, this);
    }

    @Override
    public void onSuccess(Team data) {
        if(getView() != null) {
            getView().showTeamDetail(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
