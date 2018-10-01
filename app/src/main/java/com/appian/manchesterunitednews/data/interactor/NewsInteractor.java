package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appian.manchesterunitednews.data.RestfulServiceAuto;
import com.appnet.android.football.fbvn.data.NewsDataAuto;
import com.appnet.android.football.fbvn.data.News;
import com.appnet.android.football.fbvn.data.NewsData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsInteractor {

    public void loadNewsDetail(int id, final OnResponseListener<News> listener) {
        if(listener == null) {
            return;
        }
        Call<NewsData> call = RestfulService.getInstance().loadNewsDetail(id);
        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                if(response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadNewsLatest(final OnResponseListener<List<News>> listener) {
        if (listener == null) {
            return;
        }
        Call<NewsDataAuto> call = RestfulServiceAuto.getInstance().loadNewsLatest();
        enqueue(call, listener);
    }

    private void enqueue(Call<NewsDataAuto> call, final OnResponseListener<List<News>> listener) {
        call.enqueue(new Callback<NewsDataAuto>() {
            @Override
            public void onResponse(Call<NewsDataAuto> call, Response<NewsDataAuto> response) {
                if (response.body() == null && response.body().getStatus()) {
                    listener.onSuccess(null);
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<NewsDataAuto> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
