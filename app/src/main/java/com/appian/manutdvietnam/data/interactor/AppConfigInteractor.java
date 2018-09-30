package com.appian.manutdvietnam.data.interactor;

import com.appian.manutdvietnam.data.RestfulService;
import com.appnet.android.football.fbvn.data.AppConfigData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppConfigInteractor {
    public void loadAppConfig(String appKey, final OnResponseListener<AppConfigData> listener) {
        if (listener == null) {
            return;
        }
        Call<AppConfigData> call = RestfulService.getInstance().loadAppConfigData(appKey);
        call.enqueue(new Callback<AppConfigData>() {
            @Override
            public void onResponse(Call<AppConfigData> call, Response<AppConfigData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AppConfigData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
