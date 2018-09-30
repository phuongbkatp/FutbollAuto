package com.appian.manchesterunitednews.app.table.view;

import com.appnet.android.football.sofa.data.TableRowsData;

import java.util.List;

public interface LeagueStandingView {
    void showLeagueStanding(List<TableRowsData> data);
    void onLoadLeagueStandingFail();
}
