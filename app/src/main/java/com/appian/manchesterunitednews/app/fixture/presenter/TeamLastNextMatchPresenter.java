package com.appian.manchesterunitednews.app.fixture.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.fixture.view.TeamLastNextMatchView;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.data.interactor.TeamInteractor;
import com.appnet.android.football.sofa.data.Event;

import java.util.List;

public class TeamLastNextMatchPresenter extends BasePresenter<TeamLastNextMatchView> implements OnResponseListener<List<Event>> {
    private final TeamInteractor mTeamInteractor;

    public TeamLastNextMatchPresenter() {
        mTeamInteractor = new TeamInteractor();
    }

    public void loadMatchLastNext(int teamId) {
        mTeamInteractor.loadLastNextMatch(teamId, this);
    }

    @Override
    public void onSuccess(List<Event> data) {
        if(getView() != null) {
            getView().showMatchLastNext(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
