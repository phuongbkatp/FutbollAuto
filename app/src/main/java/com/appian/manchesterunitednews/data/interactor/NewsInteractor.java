package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appian.manchesterunitednews.data.RestfulServiceAuto;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.appnet.android.football.fbvn.data.DetailNewsDataAuto;
import com.appnet.android.football.fbvn.data.NewsAuto;
import com.appnet.android.football.fbvn.data.NewsDataAuto;
import com.appnet.android.football.fbvn.data.News;
import com.appnet.android.football.fbvn.data.NewsData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsInteractor {

    public void loadNewsDetail(String link, final OnDetailNewsResponseListener<DetailNewsAuto> listener) {
        if(listener == null) {
            return;
        }
        Call<DetailNewsDataAuto> call = RestfulServiceAuto.getInstance().loadNewsDetail(link);
        call.enqueue(new Callback<DetailNewsDataAuto>() {
            @Override
            public void onResponse(Call<DetailNewsDataAuto> call, Response<DetailNewsDataAuto> response) {
                if(response.body() == null && response.body().getStatus() != 0) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<DetailNewsDataAuto> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loadNewsLatest(final OnResponseListener<List<NewsAuto>> listener) {
        if (listener == null) {
            return;
        }
        Call<NewsDataAuto> call = RestfulServiceAuto.getInstance().loadNewsLatest();
        enqueue(call, listener);
    }

    public void loadNewsTrend(final OnResponseListener<List<NewsAuto>> listener) {
        if (listener == null) {
            return;
        }
        Call<NewsDataAuto> call = RestfulServiceAuto.getInstance().loadNewsTrend();
        enqueue(call, listener);
    }
    public void loadNewsVideo(final OnResponseListener<List<NewsAuto>> listener) {
        if (listener == null) {
            return;
        }
        Call<NewsDataAuto> call = RestfulServiceAuto.getInstance().loadNewsVideo();
        enqueue(call, listener);
    }
    private void enqueue(Call<NewsDataAuto> call, final OnResponseListener<List<NewsAuto>> listener) {
        call.enqueue(new Callback<NewsDataAuto>() {
            @Override
            public void onResponse(Call<NewsDataAuto> call, Response<NewsDataAuto> response) {
                if (response.body() == null && response.body().getStatus() != 0) {
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
