package com.appian.manchesterunitednews.data;

import android.content.Context;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.MainApplication;
import com.appian.manchesterunitednews.network.NetworkHelper;
import com.appnet.android.football.fbvn.data.DetailNewsDataAuto;
import com.appnet.android.football.fbvn.data.NewsDataAuto;
import com.appnet.android.football.fbvn.service.RestfulApiFootballAuto;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulServiceAuto {
    private static final int TIME_OUT = 30; // seconds
    private static final String CACHE_CONTROL = "public, max-age=";
    private static final String MAX_AGE_CACHE_NEWS_DETAIL = CACHE_CONTROL + 60 * 10;   // 5 mins

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

}
