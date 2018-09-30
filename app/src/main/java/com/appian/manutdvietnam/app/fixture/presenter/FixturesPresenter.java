package com.appian.manutdvietnam.app.fixture.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.fixture.view.FixturesView;
import com.appian.manutdvietnam.data.interactor.LeagueInteractor;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appnet.android.football.sofa.data.GameTournament;

import java.util.List;

public class FixturesPresenter extends BasePresenter<FixturesView> implements OnResponseListener<List<GameTournament>> {
    private final LeagueInteractor mInteractor;

    public FixturesPresenter(LeagueInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadFixtures(int leagueId, int seasonId) {
        mInteractor.loadFixtures(leagueId, seasonId, this);
    }

    @Override
    public void onSuccess(List<GameTournament> data) {
        if(getView() != null) {
            getView().showFixtures(data);
        }
    }

    @Override
    public void onFailure(String error) {
        if(getView() != null) {
            getView().onLoadFixturesFail();
        }
    }
}
