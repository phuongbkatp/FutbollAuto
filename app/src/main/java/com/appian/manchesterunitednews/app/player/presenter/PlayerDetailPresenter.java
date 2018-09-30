package com.appian.manchesterunitednews.app.player.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.player.view.PlayerDetailView;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.data.interactor.PlayerInteractor;
import com.appnet.android.football.sofa.data.Player;

public class PlayerDetailPresenter extends BasePresenter<PlayerDetailView> implements OnResponseListener<Player> {
    private final PlayerInteractor mPlayerInteractor;

    public PlayerDetailPresenter(PlayerInteractor playerInteractor) {
        mPlayerInteractor = playerInteractor;
    }

    public void loadPlayerDetail(int playerId) {
        if(playerId == 0) {
            return;
        }
        mPlayerInteractor.loadPlayerDetail(playerId, this);
    }

    @Override
    public void onSuccess(Player data) {
        if(getView() != null) {
            getView().showPlayerDetail(data);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
