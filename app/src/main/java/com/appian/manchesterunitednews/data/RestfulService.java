package com.appian.manchesterunitednews.data;

import android.content.Context;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.MainApplication;
import com.appian.manchesterunitednews.network.NetworkHelper;
import com.appnet.android.football.fbvn.data.AppConfigData;
import com.appnet.android.football.fbvn.data.CommentsData;
import com.appnet.android.football.fbvn.data.DefaultData;
import com.appnet.android.football.fbvn.data.DeviceTokenData;
import com.appnet.android.football.fbvn.data.LeagueData;
import com.appnet.android.football.fbvn.data.LeagueSeasonData;
import com.appnet.android.football.fbvn.data.ListNewsData;
import com.appnet.android.football.fbvn.data.ListNotificationConfigData;
import com.appnet.android.football.fbvn.data.NewsData;
import com.appnet.android.football.fbvn.data.NotificationConfigData;
import com.appnet.android.football.fbvn.data.OnOffAllNotificationData;
import com.appnet.android.football.fbvn.data.SignInAccountData;
import com.appnet.android.football.fbvn.data.UserProfileData;
import com.appnet.android.football.fbvn.service.RestfulApiFootball;
import com.appnet.android.football.sofa.data.Bet365Odds;
import com.appnet.android.football.sofa.data.EventDetail;
import com.appnet.android.football.sofa.data.Game;
import com.appnet.android.football.sofa.data.Incident;
import com.appnet.android.football.sofa.data.LineupsData;
import com.appnet.android.football.sofa.data.Manager;
import com.appnet.android.football.sofa.data.Performance;
import com.appnet.android.football.sofa.data.Player;
import com.appnet.android.football.sofa.data.Statistic;
import com.appnet.android.football.sofa.data.TableRowsData;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.data.TeamLastNextData;
import com.appnet.android.football.sofa.data.Transfer;
import com.appnet.android.football.sofa.service.RestfulApiSofa;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulService {
    private static final int TIME_OUT = 30; // seconds
    private static final String CACHE_CONTROL = "public, max-age=";
    private static final String MAX_AGE_CACHE_NEWS_DETAIL = CACHE_CONTROL + 60 * 10;   // 5 mins
    private static final String MAX_AGE_CACHE_TEAM_LEAGUE_SEASON = CACHE_CONTROL + 60 * 60 * 24 * 2;   // 2 days
    private static final String MAX_AGE_CACHE_APP_CONFIG = CACHE_CONTROL + 60 * 30;   // 30 minutes
    private static final String MAX_AGE_CACHE_LIST_NEWS = CACHE_CONTROL + 60 * 5;   // 5 minutes

    private static final int NEWS_ACTIVE = 1;
    private static RestfulService sInstance;
    private RestfulApiFootball mRestfulApiFootball;
    private RestfulApiSofa mRestfulApiSofa;

    private RestfulService() {
        Context context = MainApplication.getApplication().getApplicationContext();
        mRestfulApiFootball = createRestfulApiFootball(context, Constant.BASE_URL);
        mRestfulApiSofa = createRestfulApiSofa(context);
    }

    private static RestfulApiFootball createRestfulApiFootball(final Context context, String baseUrl) {
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
        Retrofit retrofit = (new retrofit2.Retrofit.Builder())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(RestfulApiFootball.class);
    }

    private static Cache createCache(Context context) {
        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "httpCache");
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        return new Cache(httpCacheDirectory, cacheSize);
    }

    private static RestfulApiSofa createRestfulApiSofa(final Context context) {
        // Create Interceptor:
        //add cache to the client
        OkHttpClient client = (new OkHttpClient.Builder().connectTimeout(TIME_OUT, TimeUnit.SECONDS))
                .cache(createCache(context))
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineResponseCacheInterceptor())
                .build();
        Retrofit retrofit = (new retrofit2.Retrofit.Builder())
                .baseUrl(RestfulApiSofa.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(RestfulApiSofa.class);
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

    public static RestfulService getInstance() {
        if (sInstance == null) {
            sInstance = new RestfulService();
        }
        return sInstance;
    }

    public Call<ListNewsData> loadNewsByMatch(int sofaMatchId, String language, int page, int limit) {
        return mRestfulApiFootball.loadNewsBySofa("match", sofaMatchId, NEWS_ACTIVE, language, page, limit, "order_time");
    }

    public Call<ListNewsData> loadVideoOfMatch(int sofaMatchId, int page, int limit) {
        return mRestfulApiFootball.loadNewsSofaByType(MAX_AGE_CACHE_LIST_NEWS, "match", sofaMatchId, NEWS_ACTIVE, "video", page, limit, "order_time");
    }

    public Call<ListNewsData> loadNewsByApp(int appId, String language, int page, int limit) {
        return mRestfulApiFootball.loadNewsById(MAX_AGE_CACHE_LIST_NEWS, "app", appId, NEWS_ACTIVE, language, page, limit, "order_time");
    }

    public Call<ListNewsData> loadNewsByCategory(int appId, int categoryId, String language, int page, int limit) {
        return mRestfulApiFootball.loadNewsByCategory(MAX_AGE_CACHE_LIST_NEWS, "app", appId, categoryId, NEWS_ACTIVE, language, page, limit, "order_time");
    }

    public Call<NewsData> loadNewsDetail(int newsId) {
        return mRestfulApiFootball.loadNewsDetail(MAX_AGE_CACHE_NEWS_DETAIL, newsId);
    }

    public Call<LeagueData> loadLeaguesByTeam(int teamId, int seasonId) {
        return mRestfulApiFootball.loadLeaguesByTeam(teamId, seasonId);
    }

    public Call<LeagueSeasonData> loadSeasonLeaguesByTeam(int seasonId, int teamId) {
        return mRestfulApiFootball.loadSeasonLeaguesByTeam(MAX_AGE_CACHE_TEAM_LEAGUE_SEASON, seasonId, teamId);
    }

    public Call<SignInAccountData> loginSocial(String email, String socialId, String socialType, String fullName, String avatar) {
        return mRestfulApiFootball.loginSocial(email, socialId, socialType, fullName, avatar);
    }

    public Call<SignInAccountData> register(String email, String password, String confirmPassword, String fullName) {
        return mRestfulApiFootball.register(email, password, confirmPassword, fullName);
    }

    public Call<SignInAccountData> login(String email, String password) {
        return mRestfulApiFootball.login(email, password);
    }

    public Call<DeviceTokenData> registerDeviceToken(int userId, int appId, String deviceTokenId) {
        if (userId > 0) {
            return mRestfulApiFootball.registerDeviceToken(userId, appId, deviceTokenId);
        }
        return mRestfulApiFootball.registerDeviceToken(appId, deviceTokenId);
    }

    public Call<DeviceTokenData> followTeam(int userId, int appId, int teamId, String deviceTokenId) {
        if (userId > 0) {
            return mRestfulApiFootball.followTeam(userId, appId, teamId, deviceTokenId);
        }
        return mRestfulApiFootball.followTeam(appId, teamId, deviceTokenId);
    }

    public Call<EventDetail> loadMatchDetail(int matchId) {
        return mRestfulApiSofa.loadMatchDetail("public, max-age=0", matchId);
    }

    public Call<List<Incident>> loadIncidents(int matchId) {
        return mRestfulApiSofa.loadIncidents("public, max-age=0", matchId);
    }

    public Call<LineupsData> loadLineups(int matchId) {
        return mRestfulApiSofa.loadLineups(matchId);
    }

    public Call<Statistic> loadStatistics(int matchId) {
        return mRestfulApiSofa.loadMatchStatistics(matchId);
    }

    public Call<Team> loadTeamDetails(int teamId) {
        return mRestfulApiSofa.loadTeamDetails(teamId);
    }

    public Call<TeamLastNextData> loadTeamLastNext(int teamId) {
        return mRestfulApiSofa.loadTeamLastNext(teamId);
    }

    public Call<List<Player>> loadSquadByTeam(int teamId) {
        return mRestfulApiSofa.loadSquad(teamId);
    }

    public Call<List<Performance>> loadTeamPerformance(int teamId) {
        return mRestfulApiSofa.loadTeamPerformance(teamId);
    }

    public Call<List<Bet365Odds>> loadOdds(int matchId) {
        return mRestfulApiSofa.loadOdds(matchId);
    }

    public Call<List<TableRowsData>> loadTournamentStanding(int leagueId, int seasonId) {
        return mRestfulApiSofa.loadTournamentStanding(leagueId, seasonId);
    }

    public Call<Game> loadTournamentFixtures(int leagueId, int seasonId) {
        return mRestfulApiSofa.loadTournamentFixtures(leagueId, seasonId);
    }

    public Call<Player> loadPlayerDetails(int playerId) {
        return mRestfulApiSofa.loadPlayerDetails(playerId);
    }

    public Call<List<Transfer>> loadPlayerTransferHistory(int playerId) {
        return mRestfulApiSofa.loadPlayerTransferHistory(playerId);
    }

    public Call<CommentsData> loadComments(String objectType, int objectId, int page, int limit) {
        if (Constant.OBJECT_TYPE_SOFA_MATCH.equals(objectType)) {
            return mRestfulApiFootball.loadCommentsBySofa("match", objectId, page, limit);
        }
        return mRestfulApiFootball.loadComments(objectType, objectId, page, limit);
    }

    public Call<DefaultData> postComment(String authorization, int userId, String content, String objectType, int objectId) {
        if (Constant.OBJECT_TYPE_SOFA_MATCH.equals(objectType)) {
            return mRestfulApiFootball.postCommentBySofa(authorization, userId, content, "match", objectId);
        }
        return mRestfulApiFootball.postComment(authorization, userId, content, objectType, objectId);
    }

    public Call<Manager> loadManagerDetails(int managerId) {
        return mRestfulApiSofa.loadManagerDetails(managerId);
    }

    public Call<AppConfigData> loadAppConfigData(String appKey) {
        return mRestfulApiFootball.loadAppConfigs(MAX_AGE_CACHE_APP_CONFIG, appKey);
    }

    public Call<UserProfileData> loadUserProfile(String authorization) {
        return mRestfulApiFootball.loadUserProfile(authorization);
    }

    public Call<UserProfileData> updateUserProfile(String authorization, String firstName, String lastName, String email, boolean male, String address) {
        return mRestfulApiFootball.updateUserProfile(authorization, firstName, lastName, email, male, address);
    }

    public Call<UserProfileData> uploadAvatar(String authorization, File file) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        return mRestfulApiFootball.uploadAvatar(authorization, body);
    }

    public Call<ListNotificationConfigData> loadNotificationConfigs(int appId, String deviceToken) {
        return mRestfulApiFootball.loadNotificationConfigs(appId, deviceToken);
    }

    public Call<NotificationConfigData> setOnOffNotification(int id, int value) {
        return mRestfulApiFootball.setOnOffNotification(id, value);
    }

    public Call<OnOffAllNotificationData> setOnOffAllNotification(int appId, String deviceTokenId, int value) {
        return mRestfulApiFootball.setOnOffAllNotification(appId, deviceTokenId, value);
    }
}
