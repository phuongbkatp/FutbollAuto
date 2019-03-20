package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulServiceAuto;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.appnet.android.football.fbvn.data.DetailNewsDataAuto;
import com.appnet.android.football.fbvn.data.NewsAuto;
import com.appnet.android.football.fbvn.data.NewsDataAuto;

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
                if (response == null) {
                    return;
                }
                if(response.body() == null) {
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

    public void loadNews(final OnResponseListener<List<NewsAuto>> listener, String team, String category, String language) {
        if (listener == null) {
            return;
        }
        Call<NewsDataAuto> call = RestfulServiceAuto.getInstance().loadNews(team, category, language);
        enqueue(call, listener);
    }

    public void loadMatchVideo(String home, String away, final OnDetailNewsResponseListener<DetailNewsAuto> listener) {
        if(listener == null) {
            return;
        }
        Call<DetailNewsDataAuto> call = RestfulServiceAuto.getInstance().loadMatchVideo(home, away);
        call.enqueue(new Callback<DetailNewsDataAuto>() {
            @Override
            public void onResponse(Call<DetailNewsDataAuto> call, Response<DetailNewsDataAuto> response) {
                if (response == null) {
                    return;
                }
                if(response.body() == null) {
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

    private void enqueue(Call<NewsDataAuto> call, final OnResponseListener<List<NewsAuto>> listener) {
        call.enqueue(new Callback<NewsDataAuto>() {
            @Override
            public void onResponse(Call<NewsDataAuto> call, Response<NewsDataAuto> response) {
                if (response == null) {
                    return;
                }
                if (response.body() == null) {
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
