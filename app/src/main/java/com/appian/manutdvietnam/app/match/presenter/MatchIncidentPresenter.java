package com.appian.manutdvietnam.app.match.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.match.view.MatchIncidentView;
import com.appian.manutdvietnam.data.interactor.MatchInteractor;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appnet.android.football.sofa.data.Incident;

import java.util.List;

public class MatchIncidentPresenter extends BasePresenter<MatchIncidentView> implements OnResponseListener<List<Incident>> {
    private final MatchInteractor mInteractor;

    public MatchIncidentPresenter(MatchInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadMatchIncident(int matchId) {
        if(matchId == 0 || getView() == null) {
            return;
        }
        mInteractor.loadIncidents(matchId, this);
    }

    @Override
    public void onSuccess(List<Incident> data) {
        if(getView() != null) {
            getView().showIncident(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
