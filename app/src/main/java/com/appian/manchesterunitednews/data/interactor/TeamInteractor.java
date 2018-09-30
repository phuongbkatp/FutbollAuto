package com.appian.manchesterunitednews.data.interactor;


import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.Performance;
import com.appnet.android.football.sofa.data.Player;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.data.TeamLastNextData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamInteractor {
    public void loadTeamPerformance(final int teamId, final OnResponseListener<List<Performance>> listener) {
        if (listener == null) {
            return;
        }
        Call<List<Performance>> call = RestfulService.getInstance().loadTeamPerformance(teamId);
        call.enqueue(new Callback<List<Performance>>() {
            @Override
            public void onResponse(Call<List<Performance>> call, Response<List<Performance>> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Performance>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadTeamDetail(final int teamId, final OnResponseListener<Team> listener) {
        if (listener == null) {
            return;
        }
        Call<Team> call = RestfulService.getInstance().loadTeamDetails(teamId);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadLastNextMatch(int teamId, final OnResponseListener<List<Event>> listener) {
        if (listener == null) {
            return;
        }
        Call<TeamLastNextData> call = RestfulService.getInstance().loadTeamLastNext(teamId);
        call.enqueue(new Callback<TeamLastNextData>() {
            @Override
            public void onResponse(Call<TeamLastNextData> call, Response<TeamLastNextData> response) {
                TeamLastNextData data = response.body();
                if (data == null || data.getLastNextEvents() == null || data.getLastNextEvents().isEmpty()) {
                    return;
                }
                listener.onSuccess(response.body().getLastNextEvents());
            }

            @Override
            public void onFailure(Call<TeamLastNextData> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadTeamSquad(int teamId, final OnResponseListener<List<Player>> listener) {
        if (listener == null) {
            return;
        }
        Call<List<Player>> call = RestfulService.getInstance().loadSquadByTeam(teamId);
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (response.body() == null) {
                    listener.onSuccess(null);
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }
}
