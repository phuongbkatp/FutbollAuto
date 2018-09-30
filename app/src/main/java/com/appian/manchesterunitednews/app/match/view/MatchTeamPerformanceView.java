package com.appian.manchesterunitednews.app.match.view;


import com.appnet.android.football.sofa.data.Performance;

import java.util.List;

public interface MatchTeamPerformanceView {
    void showHomeTeamPerformance(List<Performance> data);

    void showAwayTeamPerformance(List<Performance> data);
}
