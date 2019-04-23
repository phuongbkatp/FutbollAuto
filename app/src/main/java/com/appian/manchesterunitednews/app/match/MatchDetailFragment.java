package com.appian.manchesterunitednews.app.match;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.StateFragment;
import com.appian.manchesterunitednews.app.ToolbarViewListener;
import com.appian.manchesterunitednews.app.match.presenter.MatchDetailPresenter;
import com.appian.manchesterunitednews.app.match.presenter.MatchIncidentPresenter;
import com.appian.manchesterunitednews.app.match.presenter.MatchTeamPerformancePresenter;
import com.appian.manchesterunitednews.app.match.presenter.MatchVideoPresenter;
import com.appian.manchesterunitednews.app.match.view.MatchDetailView;
import com.appian.manchesterunitednews.app.match.view.MatchIncidentView;
import com.appian.manchesterunitednews.app.match.view.MatchTeamPerformanceView;
import com.appian.manchesterunitednews.app.match.view.MatchVideoView;
import com.appian.manchesterunitednews.app.player.PlayerDetailsActivity;
import com.appian.manchesterunitednews.app.team.TeamDetailsActivity;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.interactor.MatchInteractor;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.data.interactor.TeamInteractor;
import com.appian.manchesterunitednews.util.EventHelper;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.fb.FacebookNativeAd;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.appnet.android.football.fbvn.data.MetaDetailNewsAuto;
import com.appnet.android.football.sofa.data.BestPlayer;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.Incident;
import com.appnet.android.football.sofa.data.Performance;
import com.appnet.android.football.sofa.data.Person;
import com.appnet.android.football.sofa.data.Round;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.data.Tournament;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

import java.util.Date;
import java.util.List;

public class MatchDetailFragment extends BaseLiveFragment implements
        MatchDetailView, MatchIncidentView, MatchTeamPerformanceView, MatchVideoView {
    private static final String KEY_MATCH_ID = "MATCH_ID";

    private TextView mTvTournament;
    private TextView mTvStadium;
    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvHomeTeamName;
    private TextView mTvAwayTeamName;
    private TextView mTvHomeTeamGold;
    private TextView mTvAwayTeamGold;
    private TextView mTvTimeRemaining;
    private LinearLayout mLlGoalMatch;
    private TextView mTvStatusDescription;
    private ImageView mImgHomeTeamLogo;
    private ImageView mImgAwayTeamLogo;

    private ViewGroup rlBestPlayer;
    private RecyclerView mLvMatchEvents;
    private ImageView mImgHomeBestPlayer;
    private ImageView mImgAwayBestPlayer;
    private TextView mTvHomeBestPlayer;
    private TextView mTvAwayBestPlayer;
    private TextView mTvHomePlayerRating;
    private TextView mTvAwayPlayerRating;
    private TextView mTvVenue;
    private TextView mTvReferee;
    private LinearLayout mLlHomePerformance;
    private LinearLayout mLlAwayPerformance;
    private LinearLayout mLLPerformance;
    // Video
    private View mVideoRow;
    private ImageView mThumbVideo;
    private TextView mTvSourceVideo;
    private TextView mTvDateCreatedVideo;

    // Live animation
    private Animation mLiveAnimation;
    private MatchDetailEventRecycleViewAdapter mMatchDetailEventAdapter;
    private int mMatchId = 0;
    private boolean mIsLoadedTeamPerformance;

    private ToolbarViewListener mToolBar;

    private MatchDetailPresenter mMatchDetailPresenter;
    private MatchIncidentPresenter mMatchIncidentPresenter;
    private MatchTeamPerformancePresenter mMatchTeamPerformancePresenter;
    private MatchVideoPresenter mMatchVideoPresenter;

    private Team mHomeTeam;
    private Team mAwayTeam;
    private Person mBestHomePlayer;
    private Person mBestAwayPlayer;

    public static MatchDetailFragment newInstance(int matchId, StateFragment stateFragment) {
        Bundle args = new Bundle();
        args.putInt(KEY_MATCH_ID, matchId);
        MatchDetailFragment fragment = new MatchDetailFragment();
        fragment.setArguments(args);
        fragment.setStateFragment(stateFragment);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_match_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMatchId = args.getInt(KEY_MATCH_ID, 0);
        }
        initLiveAnimation();
        MatchInteractor matchInteractor = new MatchInteractor();
        mMatchDetailPresenter = new MatchDetailPresenter(matchInteractor);
        mMatchIncidentPresenter = new MatchIncidentPresenter(matchInteractor);
        mMatchTeamPerformancePresenter = new MatchTeamPerformancePresenter(new TeamInteractor());
        mMatchVideoPresenter = new MatchVideoPresenter(new NewsInteractor());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mMatchDetailEventAdapter = new MatchDetailEventRecycleViewAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLvMatchEvents.setLayoutManager(layoutManager);
        mLvMatchEvents.setAdapter(mMatchDetailEventAdapter);
        mLvMatchEvents.setNestedScrollingEnabled(false);
        mLvMatchEvents.setFocusable(false);

        initAd(view);
        updateTitle();
        // Set animation for live score
        mLiveAnimation.cancel();
        mTvStatusDescription.setAnimation(mLiveAnimation);
        initMvp();
    }

    private void initView(View view) {
        mTvTournament = view.findViewById(R.id.tv_match_detail_tournament);
        mTvStadium = view.findViewById(R.id.tv_match_detail_stadium);
        mTvDate = view.findViewById(R.id.tv_match_detail_date);
        mTvTime = view.findViewById(R.id.tv_match_detail_time);
        mTvHomeTeamName = view.findViewById(R.id.tv_match_detail_home_team_name);
        mTvAwayTeamName = view.findViewById(R.id.tv_match_detail_away_team_name);
        mTvHomeTeamGold = view.findViewById(R.id.tv_match_detail_home_team_goals);
        mTvAwayTeamGold = view.findViewById(R.id.tv_match_detail_away_team_goals);
        mTvTimeRemaining = view.findViewById(R.id.tv_time_remaining);
        mLlGoalMatch = view.findViewById(R.id.ll_goal_match);
        mTvStatusDescription = view.findViewById(R.id.tv_match_detail_status_description);
        mImgHomeTeamLogo = view.findViewById(R.id.img_match_detail_home_team_logo);
        mImgAwayTeamLogo = view.findViewById(R.id.img_match_detail_away_team_logo);
        view.findViewById(R.id.view_scroll).requestFocus();

        rlBestPlayer = view.findViewById(R.id.best_player_row);
        mLvMatchEvents = view.findViewById(R.id.list_events);
        mImgHomeBestPlayer = view.findViewById(R.id.img_homeBestPlayer);
        mImgAwayBestPlayer = view.findViewById(R.id.img_awayBestPlayer);
        mTvHomeBestPlayer = view.findViewById(R.id.tv_homePlayer);
        mTvAwayBestPlayer = view.findViewById(R.id.tv_awayPlayer);
        mTvHomePlayerRating = view.findViewById(R.id.tv_homeRating);
        mTvAwayPlayerRating = view.findViewById(R.id.tv_awayRating);
        mTvVenue = view.findViewById(R.id.tv_match_detail_venue);
        mTvReferee = view.findViewById(R.id.tv_match_detail_referee);
        mLlHomePerformance = view.findViewById(R.id.ll_home_performance);
        mLlAwayPerformance = view.findViewById(R.id.ll_away_performance);
        mLLPerformance = view.findViewById(R.id.ll_performance);

        mVideoRow = view.findViewById(R.id.match_video_row);
        mThumbVideo = view.findViewById(R.id.img_thumb_news);
        mTvSourceVideo = view.findViewById(R.id.tv_source);
        mTvDateCreatedVideo = view.findViewById(R.id.tv_date);

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.group_view_home_team:
                        displayTeam(mHomeTeam);
                        break;
                    case R.id.group_view_away_team:
                        displayTeam(mAwayTeam);
                        break;
                    case R.id.group_view_home_best_player:
                        displayPlayer(mBestHomePlayer);
                        break;
                    case R.id.group_view_away_best_player:
                        displayPlayer(mBestAwayPlayer);
                        break;
                }
            }
        };
        View viewHomeTeam = view.findViewById(R.id.group_view_home_team);
        viewHomeTeam.setOnClickListener(btnClickListener);
        View viewAwayTeam = view.findViewById(R.id.group_view_away_team);
        viewAwayTeam.setOnClickListener(btnClickListener);
        View viewHomeBestPlayer = view.findViewById(R.id.group_view_home_best_player);
        viewHomeBestPlayer.setOnClickListener(btnClickListener);
        View viewAwayBestPlayer = view.findViewById(R.id.group_view_away_best_player);
        viewAwayBestPlayer.setOnClickListener(btnClickListener);

    }

    private void displayTeam(Team team) {
        if(team == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), TeamDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_TEAM_NAME, team.getName());
        intent.putExtra(Constant.EXTRA_KEY_TEAM_ID, team.getId());
        startActivity(intent);
    }

    private void displayPlayer(Person player) {
        if(player == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), PlayerDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_SOFA_PLAYER_ID, player.getId());
        intent.putExtra(Constant.EXTRA_KEY_PLAYER_NAME, player.getName());
        startActivity(intent);
    }

    private void initMvp() {
        mMatchVideoPresenter.attachView(this);
        mMatchDetailPresenter.attachView(this);
        mMatchDetailPresenter.loadMatchDetail(mMatchId);
        mMatchIncidentPresenter.attachView(this);
        mMatchIncidentPresenter.loadMatchIncident(mMatchId);
        mMatchTeamPerformancePresenter.attachView(this);
        mIsLoadedTeamPerformance = false;
    }

    @Override
    protected void onLive() {
        super.onLive();
        mMatchDetailPresenter.loadMatchDetail(mMatchId);
        mMatchIncidentPresenter.loadMatchIncident(mMatchId);
    }

    @Override
    protected void onLiveStopped() {
        super.onLiveStopped();
        mMatchDetailPresenter.loadMatchDetail(mMatchId);
        mMatchIncidentPresenter.loadMatchIncident(mMatchId);
    }

    private void fillBestPlayer(Event event) {
        if (event == null) {
            return;
        }
        BestPlayer homeBestPlayer = event.getBestHomeTeamPlayer();
        BestPlayer awayBestPlayer = event.getBestAwayTeamPlayer();

        if (homeBestPlayer != null && awayBestPlayer != null) {
            mBestHomePlayer = homeBestPlayer.getPlayer();
            mBestAwayPlayer = awayBestPlayer.getPlayer();
            ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(homeBestPlayer.getPlayer().getId()),
                    mImgHomeBestPlayer);
            mTvHomeBestPlayer.setText(homeBestPlayer.getPlayer().getName());
            mTvHomePlayerRating.setText(homeBestPlayer.getValue());
            ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(awayBestPlayer.getPlayer().getId()),
                    mImgAwayBestPlayer);
            mTvAwayBestPlayer.setText(awayBestPlayer.getPlayer().getName());
            mTvAwayPlayerRating.setText(awayBestPlayer.getValue());
            rlBestPlayer.setVisibility(View.VISIBLE);
        } else {
            rlBestPlayer.setVisibility(View.GONE);
        }
        mTvVenue.setText(event.getNameStadium());
        mTvReferee.setText(event.getRefereeName());
    }

    private void checkLiveScore(Event event) {
        if (event.getStatus() == null) {
            mLiveAnimation.cancel();
            return;
        }
        // Live score
        if (!isLive() && Constant.SOFA_MATCH_STATUS_IN_PROGRESS.equals(event.getStatus().getType())) {
            startLive();
            mLiveAnimation.start();
        } else if (!isLive() && Constant.SOFA_MATCH_STATUS_NOT_STARTED.equals(event.getStatus().getType())) {
            long matchTime = event.getStartTimestamp();
            float diffMinutes = (matchTime - System.currentTimeMillis()) / (60 * 1000f);
            if (diffMinutes >= 0 && diffMinutes <= 10) {
                // Before 10 mins
                startLive();
                mLiveAnimation.start();
            }
        } else if (isLive() && Constant.SOFA_MATCH_STATUS_FINISHED.equals(event.getStatus().getType())) {
            stopLive();
            mLiveAnimation.cancel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarViewListener) {
            mToolBar = (ToolbarViewListener) context;
        }
    }

    private void updateTitle() {
        if (mToolBar != null) {
            mToolBar.changeToolbarTitle("Live score");
        }
    }

    private void initLiveAnimation() {
        mLiveAnimation = new AlphaAnimation(0.3f, 1.0f);
        mLiveAnimation.setDuration(500);
        mLiveAnimation.setStartOffset(100);
        mLiveAnimation.setRepeatMode(Animation.REVERSE);
        mLiveAnimation.setRepeatCount(Animation.INFINITE);
    }


    private void initAd(View root) {
        AppConfig config = AppConfig.getInstance();
        final ViewGroup fbAdContainer = root.findViewById(R.id.fl_ads);
        FacebookNativeAd.Builder builder = new FacebookNativeAd.Builder(getContext(), config.getFbAdsNative2(getContext()));
        builder.addDisplayView(fbAdContainer);
        builder.setOnAdLoadListener(new OnAdLoadListener() {
            @Override
            public void onAdLoaded() {
                fbAdContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailed() {
                fbAdContainer.setVisibility(View.GONE);
            }
        });
        FacebookNativeAd fbAd = builder.build();
        fbAd.loadAd();
    }

    @Override
    public void showMatchDetail(Event event) {
        loadTeamPerformance(event);
        Resources res = getResources();
        String status = (event.getStatus() != null) ? event.getStatus().getType() : "";
        if (Constant.SOFA_MATCH_STATUS_NOT_STARTED.equals(status)) {
            mLlGoalMatch.setVisibility(View.GONE);
            mTvTimeRemaining.setVisibility(View.VISIBLE);
            mTvTimeRemaining.setText(Utils.calculateRemainTime(getContext(), event.getStartTimestamp()));
        } else if (Constant.SOFA_MATCH_STATUS_CANCELED.equals(status)
                || Constant.SOFA_MATCH_STATUS_POSTPONED.equals(status)) {
            mLlGoalMatch.setVisibility(View.GONE);
        } else {
            mLlGoalMatch.setVisibility(View.VISIBLE);
            mTvTimeRemaining.setVisibility(View.GONE);
        }
        if (!isLive()) {
            if (event.getVenue() != null) {
                mTvStadium.setText(event.getVenue().getNameStadium());
            }
            Tournament tournament = event.getTournament();
            if (tournament != null) {
                String[] texts = tournament.getName().split(",");
                String tourName = (texts.length > 0) ? texts[0] : "";
                StringBuilder strBuilder = new StringBuilder(tourName);
                Round round = event.getRoundInfo();
                if (round != null) {
                    if (TextUtils.isEmpty(round.getName())) {
                        strBuilder.append(" - ")
                                .append(res.getString(R.string.league_fixtures_round, event.getRoundInfo().getRound()));
                    } else {
                        strBuilder.append(" - ").append(round.getName());
                    }
                }
                mTvTournament.setText(strBuilder.toString());
            }
            mTvDate.setText(Utils.formatWeekTime(getContext(), event.getStartTimestamp()));
            mTvTime.setText(Utils.formatDateMonthYear(new Date(event.getStartTimestamp())));
            if (event.getHomeTeam() != null && event.getAwayTeam() != null) {
                mHomeTeam = event.getHomeTeam();
                mAwayTeam = event.getAwayTeam();
                mTvHomeTeamName.setText(event.getHomeTeam().getShortName());
                ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(event.getHomeTeam().getId()), mImgHomeTeamLogo);
                mTvAwayTeamName.setText(event.getAwayTeam().getShortName());
                ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(event.getAwayTeam().getId()), mImgAwayTeamLogo);
                loadMatchVideo(event);
            }
        }

        if (event.getAwayScore() != null || event.getHomeScore() != null) {
            mTvHomeTeamGold.setText(String.valueOf(event.getHomeScore().getCurrent()));
            mTvAwayTeamGold.setText(String.valueOf(event.getAwayScore().getCurrent()));
        }
        mTvStatusDescription.setText(event.getStatusDescription());
        fillBestPlayer(event);
        // Live score
        checkLiveScore(event);
    }

    private void loadMatchVideo(Event event) {
        if(!Constant.SOFA_MATCH_STATUS_FINISHED.equals(event.getStatus().getType())) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        long startTime = event.getStartTimestamp();
        if(currentTime - startTime > 86400000*3) {
            return;
        }
        mMatchVideoPresenter.loadMatchVideo(event.getHomeTeam().getName(), event.getAwayTeam().getName());
    }

    private void loadTeamPerformance(Event event) {
        if (!mIsLoadedTeamPerformance) {
            Team homeTeam = event.getHomeTeam();
            if (homeTeam != null) {
                mMatchTeamPerformancePresenter.loadHomeTeam(homeTeam.getId());
            }
            Team awayTeam = event.getAwayTeam();
            if (awayTeam != null) {
                mMatchTeamPerformancePresenter.loadAwayTeam(awayTeam.getId());
            }

        }
    }

    @Override
    public void showIncident(List<Incident> data) {
        if (data.size() == 0) {
            mLvMatchEvents.setVisibility(View.GONE);
        } else {

            mLvMatchEvents.setVisibility(View.VISIBLE);
        }
        mMatchDetailEventAdapter.reloadData(data);
    }

    @Override
    public void showHomeTeamPerformance(List<Performance> data) {
        if (getContext() == null) {
            return;
        }
        mIsLoadedTeamPerformance = true;
        Resources res = getResources();
        if (data != null) {
            mLLPerformance.setVisibility(View.VISIBLE);
        } else {
            mLlHomePerformance.setVisibility(View.GONE);
            return;
        }
        mLlHomePerformance.removeAllViews();
        int to = data.size();
        int from = (to - 5 >= 0) ? to - 5 : 0;
        for (int i = from; i < to; i++) {
            Performance perform = data.get(i);
            View v = View.inflate(getContext(), R.layout.item_performance, null);
            TextView tvResult = v.findViewById(R.id.tv_result);
            tvResult.setText(perform.getWinFlag());
            if ("W".equals(perform.getWinFlag())) {
                ViewHelper.setBackground(tvResult, res.getDrawable(R.drawable.circle_green));
            } else if ("L".equals(perform.getWinFlag())) {
                ViewHelper.setBackground(tvResult, res.getDrawable(R.drawable.circle_red));
            } else {
                ViewHelper.setBackground(tvResult, res.getDrawable(R.drawable.circle_gray));
            }
            mLlHomePerformance.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        }
    }

    @Override
    public void showAwayTeamPerformance(List<Performance> data) {
        if (getContext() == null) {
            return;
        }
        mIsLoadedTeamPerformance = true;
        Resources res = getResources();

        if (data != null) {
            mLLPerformance.setVisibility(View.VISIBLE);
        } else {
            mLlHomePerformance.setVisibility(View.GONE);
            return;
        }
        mLlAwayPerformance.removeAllViews();
        int from = data.size();
        int to = (from - 5 >= 0) ? from - 5 : 0;
        for (int i = from - 1; i >= to; i--) {
            Performance perform = data.get(i);
            View v = View.inflate(getContext(), R.layout.item_performance, null);
            TextView tvResult = v.findViewById(R.id.tv_result);
            tvResult.setText(perform.getWinFlag());
            if ("W".equals(perform.getWinFlag())) {
                ViewHelper.setBackground(tvResult, res.getDrawable(R.drawable.circle_green));
            } else if ("L".equals(perform.getWinFlag())) {
                ViewHelper.setBackground(tvResult, res.getDrawable(R.drawable.circle_red));
            } else {
                ViewHelper.setBackground(tvResult, res.getDrawable(R.drawable.circle_gray));
            }
            mLlAwayPerformance.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMatchDetailPresenter.detachView();
        mMatchIncidentPresenter.detachView();
        mMatchTeamPerformancePresenter.detachView();
        mMatchVideoPresenter.detachView();
    }

    @Override
    public void showMatchVideo(final DetailNewsAuto data) {
        if (data == null || TextUtils.isEmpty(data.getMetaDetailNewsAuto().getVideo())) {
            mVideoRow.setVisibility(View.GONE);
            return;
        }
        mVideoRow.setVisibility(View.VISIBLE);
        final MetaDetailNewsAuto meta = data.getMetaDetailNewsAuto();
        ImageLoader.displayImage(meta.getImage(), mThumbVideo, R.mipmap.ic_play_circle_outline_black_24dp);
        mTvSourceVideo.setText(meta.getVideo());
        mTvDateCreatedVideo.setText(Utils.formatDateMonthYear(new Date(meta.getCreatedTime())));
        mVideoRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogConfirm(meta.getVideo());
            }
        });
    }

    private void showDialogConfirm(final String link) {
        Resources res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.disclaimer_title, res.getString(R.string.app_name).toUpperCase()));
        builder.setMessage(res.getString(R.string.disclaimer_text_full, res.getString(R.string.app_name).toUpperCase(), res.getString(R.string.app_name).toUpperCase()));
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                startActivity(intent);
                EventHelper.log(getContext(), EventHelper.EVENT_TAP_VIDEO_HIGHLIGHTS, null);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
