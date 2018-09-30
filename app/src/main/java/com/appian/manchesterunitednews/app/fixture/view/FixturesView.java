package com.appian.manchesterunitednews.app.fixture.view;

import android.support.annotation.Nullable;

import com.appnet.android.football.sofa.data.GameTournament;

import java.util.List;

public interface FixturesView {
    void showFixtures(@Nullable List<GameTournament> data);
    void onLoadFixturesFail();
}
