package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appian.manchesterunitednews.data.RestfulServiceAuto;
import com.appnet.android.football.fbvn.data.AppConfigData;
import com.appnet.android.football.fbvn.data.UserIpData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppConfigInteractor {
    public void loadUserIp(final OnResponseListener<UserIpData> listener) {
        if (listener == null) {
            return;
        }
        Call<UserIpData> call = RestfulServiceAuto.getInstance().loadUserIp();
        call.enqueue(new Callback<UserIpData>() {
            @Override
            public void onResponse(Call<UserIpData> call, Response<UserIpData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserIpData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
