package com.appian.manchesterunitednews.app.table.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.table.view.LeagueStandingView;
import com.appian.manchesterunitednews.data.interactor.LeagueInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appnet.android.football.sofa.data.TableRowsData;

import java.util.List;

public class LeagueStandingPresenter extends BasePresenter<LeagueStandingView> implements OnResponseListener<List<TableRowsData>> {
    private final LeagueInteractor mLeagueInteractor;

    public LeagueStandingPresenter() {
        mLeagueInteractor = new LeagueInteractor();
    }

    public void loadStanding(int leagueId, int seasonId) {
        mLeagueInteractor.loadStanding(leagueId, seasonId, this);
    }

    @Override
    public void onSuccess(List<TableRowsData> data) {
        if(getView() != null) {
            getView().showLeagueStanding(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
