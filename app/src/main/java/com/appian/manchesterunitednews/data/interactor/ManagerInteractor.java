package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.sofa.data.Manager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerInteractor {
    public void loadManagerDetail(int managerId, final OnResponseListener<Manager> listener) {
        if (listener == null) {
            return;
        }
        Call<Manager> call = RestfulService.getInstance().loadManagerDetails(managerId);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
