package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.fbvn.data.ListNewsData;
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

    public void loadNewsByApp(int appId, String language, int page, int limit, final OnResponseListener<List<News>> listener) {
        if (listener == null) {
            return;
        }
        Call<ListNewsData> call = RestfulService.getInstance().loadNewsByApp(appId, language, page, limit);
        enqueue(call, listener);
    }

    public void loadNewsByCategory(int appId, int categoryId, String language, int page, int limit, final OnResponseListener<List<News>> listener) {
        if (listener == null) {
            return;
        }
        Call<ListNewsData> call = RestfulService.getInstance().loadNewsByCategory(appId, categoryId, language, page, limit);
        enqueue(call, listener);
    }

    public void loadNewsByMatch(int sofaMatchId, String language, int page, int limit, OnResponseListener<List<News>> listener) {
        if (listener == null) {
            return;
        }
        Call<ListNewsData> call = RestfulService.getInstance().loadNewsByMatch(sofaMatchId, language, page, limit);
        enqueue(call, listener);
    }

    public void loadVideoOfMatch(int sofaMatchId, int page, int limit, OnResponseListener<List<News>> listener) {
        if (listener == null) {
            return;
        }
        Call<ListNewsData> call = RestfulService.getInstance().loadVideoOfMatch(sofaMatchId, page, limit);
        enqueue(call, listener);
    }

    private void enqueue(Call<ListNewsData> call, final OnResponseListener<List<News>> listener) {
        call.enqueue(new Callback<ListNewsData>() {
            @Override
            public void onResponse(Call<ListNewsData> call, Response<ListNewsData> response) {
                if (response.body() == null) {
                    listener.onSuccess(null);
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ListNewsData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
