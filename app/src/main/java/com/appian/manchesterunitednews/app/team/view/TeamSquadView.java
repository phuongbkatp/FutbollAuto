package com.appian.manchesterunitednews.app.team.view;

import com.appnet.android.football.sofa.data.Player;

import java.util.List;

public interface TeamSquadView {
    void showTeamSquad(List<Player> data);
    void onLoadTeamSquadFail();
}
