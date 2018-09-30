package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.fbvn.data.LeagueSeason;
import com.appnet.android.football.fbvn.data.LeagueSeasonData;
import com.appnet.android.football.sofa.data.Game;
import com.appnet.android.football.sofa.data.GameTournament;
import com.appnet.android.football.sofa.data.TableRowsData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueInteractor {
    public void loadFixtures(int leagueId, int seasonId, final OnResponseListener<List<GameTournament>> listener) {
        if(listener == null) {
            return;
        }
        Call<Game> call = RestfulService.getInstance().loadTournamentFixtures(leagueId, seasonId);
        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                Game game = response.body();
                if (game != null && game.getTournaments() != null) {
                    listener.onSuccess(game.getTournaments());
                } else {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadSeasonLeaguesByTeam(int seasonId, int teamId, final OnResponseListener<List<LeagueSeason>> listener) {
        if(listener == null) {
            return;
        }
        Call<LeagueSeasonData> call = RestfulService.getInstance().loadSeasonLeaguesByTeam(seasonId, teamId);
        call.enqueue(new Callback<LeagueSeasonData>() {
            @Override
            public void onResponse(Call<LeagueSeasonData> call, Response<LeagueSeasonData> response) {
                LeagueSeasonData data = response.body();
                if (data == null || data.getLeagueSeasons() == null) {
                    return;
                }
                listener.onSuccess(data.getLeagueSeasons());
            }

            @Override
            public void onFailure(Call<LeagueSeasonData> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadStanding(int leagueId, int seasonId, final OnResponseListener<List<TableRowsData>> listener) {
        if(listener == null) {
            return;
        }
        Call<List<TableRowsData>> call = RestfulService.getInstance().loadTournamentStanding(leagueId, seasonId);
        call.enqueue(new Callback<List<TableRowsData>>() {
            @Override
            public void onResponse(Call<List<TableRowsData>> call, Response<List<TableRowsData>> response) {
                List<TableRowsData> data = response.body();
                if(data != null) {
                    listener.onSuccess(data);
                } else {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<List<TableRowsData>> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }
}
