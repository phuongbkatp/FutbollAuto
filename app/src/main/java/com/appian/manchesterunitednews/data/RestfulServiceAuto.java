package com.appian.manchesterunitednews.data;

import android.content.Context;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.MainApplication;
import com.appian.manchesterunitednews.network.NetworkHelper;
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
                .cache(createCache(context))
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineResponseCacheInterceptor())
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        OkHttpClient client = builder.build();
        Retrofit retrofit = (new Retrofit.Builder())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(RestfulApiFootballAuto.class);
    }

    private static Cache createCache(Context context) {
        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "httpCache");
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        return new Cache(httpCacheDirectory, cacheSize);
    }

    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            CacheControl cacheControl = chain.request().cacheControl();
            if (cacheControl != null && cacheControl.maxAgeSeconds() > 0 && cacheControl.isPublic()) {
                // Remain override cache control in each request
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + cacheControl.maxAgeSeconds())
                        .build();
            }
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=2")   // 2 sec
                    .build();
        }
    }

    private static class OfflineResponseCacheInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkHelper.isNetworkAvailable(MainApplication.getApplication().getApplicationContext())) {
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 15) // 15 days
                        .build();
            }
            return chain.proceed(request);
        }
    }

    public static RestfulServiceAuto getInstance() {
        if (sInstance == null) {
            sInstance = new RestfulServiceAuto();
        }
        return sInstance;
    }

    public Call<NewsDataAuto> loadNewsLatest() {
        return mRestfulApiFootball.loadNewsLatest();
    }

/*    public Call<NewsData> loadNewsDetail(int newsId) {
        return mRestfulApiFootball.loadNewsDetail(MAX_AGE_CACHE_NEWS_DETAIL, newsId);
    }*/

}
