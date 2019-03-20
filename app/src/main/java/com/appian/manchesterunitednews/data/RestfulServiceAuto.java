package com.appian.manchesterunitednews.data;

import android.content.Context;

import com.appian.manchesterunitednews.MainApplication;
import com.appnet.android.football.fbvn.data.DetailNewsDataAuto;
import com.appnet.android.football.fbvn.data.NewsDataAuto;
import com.appnet.android.football.fbvn.data.UserIpData;
import com.appnet.android.football.fbvn.service.RestfulApiFootballAuto;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulServiceAuto {
    private static final int TIME_OUT = 30; // seconds
    private static final String CACHE_CONTROL = "public, max-age=";
    private static final String MAX_AGE_CACHE_NEWS_DETAIL = CACHE_CONTROL + 60 * 10;   // 5 mins
    private static final String MAX_AGE_CACHE_APP_CONFIG = CACHE_CONTROL + 60 * 30;   // 30 minutes

    private static final String BASE_URL = "http://footballlivenews.com:8080/";
    private static RestfulServiceAuto sInstance;
    private RestfulApiFootballAuto mRestfulApiFootball;

    private RestfulServiceAuto() {
        Context context = MainApplication.getApplication().getApplicationContext();
        mRestfulApiFootball = createRestfulApiFootball(context, BASE_URL);
    }

    private static RestfulApiFootballAuto createRestfulApiFootball(final Context context, String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS);

        OkHttpClient client = builder.build();
        Retrofit retrofit = (new Retrofit.Builder())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(RestfulApiFootballAuto.class);
    }

    public static RestfulServiceAuto getInstance() {
        if (sInstance == null) {
            sInstance = new RestfulServiceAuto();
        }
        return sInstance;
    }

    public Call<NewsDataAuto> loadNews(String team, String category, String language) {
        return mRestfulApiFootball.loadNews(team, category, language);
    }

    public Call<DetailNewsDataAuto> loadNewsDetail(String link) {
        return mRestfulApiFootball.loadNewsDetail(link);
    }

    public Call<DetailNewsDataAuto> loadMatchVideo(String home, String awway) {
        return mRestfulApiFootball.loadMatchVideo(home, awway);
    }

    public Call<UserIpData> loadUserIp() {
        return mRestfulApiFootball.loadUserIp(MAX_AGE_CACHE_APP_CONFIG);
    }

}
