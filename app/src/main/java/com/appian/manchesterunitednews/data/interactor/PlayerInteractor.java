package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.sofa.data.Player;
import com.appnet.android.football.sofa.data.Transfer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerInteractor {
    public void loadPlayerDetail(int playerId, final OnResponseListener<Player> listener) {
        if (listener == null) {
            return;
        }
        Call<Player> call = RestfulService.getInstance().loadPlayerDetails(playerId);
        call.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadPlayerTransfers(int playerId, final OnResponseListener<List<Transfer>> listener) {
        if (listener == null) {
            return;
        }
        Call<List<Transfer>> call = RestfulService.getInstance().loadPlayerTransferHistory(playerId);
        call.enqueue(new Callback<List<Transfer>>() {
            @Override
            public void onResponse(Call<List<Transfer>> call, Response<List<Transfer>> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Transfer>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
