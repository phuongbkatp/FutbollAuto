package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.EventDetail;
import com.appnet.android.football.sofa.data.GameTournament;
import com.appnet.android.football.sofa.data.Incident;
import com.appnet.android.football.sofa.data.LineupsData;
import com.appnet.android.football.sofa.data.Statistic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchInteractor {
    public void loadMatchDetail(final int matchId, final OnResponseListener<Event> listener) {
        if (listener == null) {
            return;
        }
        Call<EventDetail> call = RestfulService.getInstance().loadMatchDetail(matchId);
        call.enqueue(new Callback<EventDetail>() {
            @Override
            public void onResponse(Call<EventDetail> call, Response<EventDetail> response) {
                EventDetail eventDetail = response.body();
                if (eventDetail == null || eventDetail.getGameTournament() == null) {
                    return;
                }
                GameTournament data = eventDetail.getGameTournament();

                List<Event> events = data.getEvents();
                if (events == null || events.isEmpty()) {
                    return;
                }
                Event event = events.get(0);
                listener.onSuccess(event);
            }

            @Override
            public void onFailure(Call<EventDetail> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadIncidents(final int matchId, final OnResponseListener<List<Incident>> listener) {
        if (listener == null) {
            return;
        }
        Call<List<Incident>> call = RestfulService.getInstance().loadIncidents(matchId);
        call.enqueue(new Callback<List<Incident>>() {
            @Override
            public void onResponse(Call<List<Incident>> call, Response<List<Incident>> response) {
                if (response.body() != null) {
                    listener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Incident>> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadStatistics(int matchId, final OnResponseListener<Statistic> listener) {
        if (listener == null) {
            return;
        }
        Call<Statistic> call = RestfulService.getInstance().loadStatistics(matchId);
        call.enqueue(new Callback<Statistic>() {
            @Override
            public void onResponse(Call<Statistic> call, Response<Statistic> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Statistic> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadLineups(int matchId, final OnResponseListener<LineupsData> listener) {
        if (listener == null) {
            return;
        }
        Call<LineupsData> call = RestfulService.getInstance().loadLineups(matchId);
        call.enqueue(new Callback<LineupsData>() {
            @Override
            public void onResponse(Call<LineupsData> call, Response<LineupsData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LineupsData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
