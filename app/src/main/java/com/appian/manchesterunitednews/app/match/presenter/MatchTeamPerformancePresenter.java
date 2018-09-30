package com.appian.manchesterunitednews.app.match.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.match.view.MatchTeamPerformanceView;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.data.interactor.TeamInteractor;
import com.appnet.android.football.sofa.data.Performance;

import java.util.List;

public class MatchTeamPerformancePresenter extends BasePresenter<MatchTeamPerformanceView> {
    private final TeamInteractor mInteractor;
    private OnResponseListener<List<Performance>> mHomeListener;
    private OnResponseListener<List<Performance>> mAwayListener;

    public MatchTeamPerformancePresenter(TeamInteractor interactor) {
        mInteractor = interactor;
        mHomeListener = new OnResponseListener<List<Performance>>() {
            @Override
            public void onSuccess(List<Performance> data) {
                if(getView() == null) {
                    return;
                }
                getView().showHomeTeamPerformance(data);
            }

            @Override
            public void onFailure(String error) {

            }
        };

        mAwayListener = new OnResponseListener<List<Performance>>() {
            @Override
            public void onSuccess(List<Performance> data) {
                if(getView() == null) {
                    return;
                }
                getView().showAwayTeamPerformance(data);
            }

            @Override
            public void onFailure(String error) {

            }
        };
    }

    public void loadHomeTeam(int teamId) {
        if (teamId == 0 || getView() == null) {
            return;
        }
        mInteractor.loadTeamPerformance(teamId, mHomeListener);
    }

    public void loadAwayTeam(int teamId) {
        if (teamId == 0 || getView() == null) {
            return;
        }
        mInteractor.loadTeamPerformance(teamId, mAwayListener);
    }
}
