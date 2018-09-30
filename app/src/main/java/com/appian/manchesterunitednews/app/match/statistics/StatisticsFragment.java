package com.appian.manchesterunitednews.app.match.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.StateFragment;
import com.appian.manchesterunitednews.app.match.BaseLiveFragment;
import com.appian.manchesterunitednews.app.match.OnMatchUpdatedListener;
import com.appian.manchesterunitednews.app.match.presenter.MatchStatisticPresenter;
import com.appian.manchesterunitednews.app.match.view.MatchStatisticView;
import com.appian.manchesterunitednews.data.interactor.MatchInteractor;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.StatisticsData;

public class StatisticsFragment extends BaseLiveFragment implements OnMatchUpdatedListener, MatchStatisticView {
    private static final String KEY_MATCH_ID = "MATCH_ID";

    private TextView
            mTvHomeShotsOnGoal,
            mTvHomeShotsTotal,
            mTvHomeFoulsTotal,
            mTvHomeCornersTotal,
            mTvHomeOffsidesTotal,
            mTvHomePossession,
            mTvHomeYellowCards,
            mTvHomeSavers,
            mTvAwayShotsOnGoal,
            mTvAwayShotsTotal,
            mTvAwayFoulsTotal,
            mTvAwayCornersTotal,
            mTvAwayOffsidesTotal,
            mTvAwayPossession,
            mTvAwayYellowCards,
            mTvAwaySavers;
    private LinearLayout mLayoutHidden;
    private LinearLayout mTvNoData;

    private MatchStatisticPresenter mStatisticPresenter;

    private int mMatchId;

    public StatisticsFragment() {
    }

    public static StatisticsFragment newInstance(int matchId, StateFragment stateFragment) {
        Bundle args = new Bundle();
        args.putInt(KEY_MATCH_ID, matchId);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setStateFragment(stateFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            mMatchId = args.getInt(KEY_MATCH_ID, 0);
        }
        mStatisticPresenter = new MatchStatisticPresenter(new MatchInteractor());
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        mTvHomePossession = rootView.findViewById(R.id.possision_home);
        mTvAwayPossession = rootView.findViewById(R.id.possision_away);
        mTvHomeShotsOnGoal = rootView.findViewById(R.id.shots_on_goals_home);
        mTvHomeShotsTotal = rootView.findViewById(R.id.shots_total_home);
        mTvHomeFoulsTotal = rootView.findViewById(R.id.fouls_home);
        mTvHomeCornersTotal = rootView.findViewById(R.id.corners_home);
        mTvHomeOffsidesTotal = rootView.findViewById(R.id.offsides_home);
        mTvHomeYellowCards = rootView.findViewById(R.id.yellow_cards_home);
        mTvHomeSavers = rootView.findViewById(R.id.saves_home);
        mTvAwayShotsTotal = rootView.findViewById(R.id.shots_total_away);
        mTvAwayShotsOnGoal = rootView.findViewById(R.id.shots_on_goals_away);
        mTvAwayFoulsTotal = rootView.findViewById(R.id.fousls_away);
        mTvAwayCornersTotal = rootView.findViewById(R.id.corners_away);
        mTvAwayOffsidesTotal = rootView.findViewById(R.id.offsides_away);
        mTvAwayYellowCards = rootView.findViewById(R.id.yellow_cards_away);
        mTvAwaySavers = rootView.findViewById(R.id.saves_away);
        mTvNoData = rootView.findViewById(R.id.ll_nodatafound);
        mLayoutHidden = rootView.findViewById(R.id.hidden_layout);

        mStatisticPresenter.attachView(this);
        mStatisticPresenter.loadMatchStatic(mMatchId);
    }

    @Override
    protected int getLayout() {
        return R.layout.statstics_layout;
    }

    @Override
    public void onMatchUpdated(Event event) {
        mMatchId = event.getId();
        checkLiveScore(event);
    }

    private void checkLiveScore(Event event) {
        if(event.getStatus() == null) {
            return;
        }
        // Live score
        if(!isLive() && Constant.SOFA_MATCH_STATUS_IN_PROGRESS.equals(event.getStatus().getType())) {
            startLive();
        } else if(isLive() && Constant.SOFA_MATCH_STATUS_FINISHED.equals(event.getStatus().getType())) {
            stopLive();
        }
    }

    @Override
    public void showMatchStatistic(StatisticsData data) {
        if (data == null) {
            return;
        }
        mLayoutHidden.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.GONE);

        final float totalPossession = data.getAwayBallPossession() + data.getHomeBallPossession();
        final float totalShotsOnGoal = data.getAwayShotsOnGoal() + data.getHomeShotsOnGoal();
        final float totalShots = data.getAwayTotalShotsOnGoal() + data.getHomeTotalShotsOnGoal();
        final float totalFouls = data.getAwayFouls() + data.getHomeFouls();
        final float totalCorner = data.getAwayCornerKicks() + data.getHomeCornerKicks();
        final float totalOffSides = data.getAwayOffsides() + data.getHomeOffsides();
        final float totalYellowCards = data.getAwayYellowCards() + data.getHomeYellowCards();
        final float totalGoalkeeperSavers = data.getAwayGoalkeeperSavers() + data.getHomeGoalkeeperSavers();

        final View rootView = getView();
        if(rootView == null) {
            return;
        }
        RoundCornerProgressBar home = rootView.findViewById(R.id.home_pos_progress);
        home.setProgress((data.getHomeBallPossession() * 100) / totalPossession);

        RoundCornerProgressBar home1 = rootView.findViewById(R.id.progress_shots_on_goals_home);
        home1.setProgress((data.getHomeShotsOnGoal()* 100) / totalShotsOnGoal);

        RoundCornerProgressBar home2 = rootView.findViewById(R.id.progress_shots_total_home);
        home2.setProgress((data.getHomeTotalShotsOnGoal()* 100) / totalShots);

        RoundCornerProgressBar home3 = rootView.findViewById(R.id.fouls_progress);
        home3.setProgress((data.getHomeFouls()* 100) / totalFouls);

        RoundCornerProgressBar home4 = rootView.findViewById(R.id.progress_corners_home);
        home4.setProgress((data.getHomeCornerKicks()* 100) / totalCorner);

        RoundCornerProgressBar home5 = rootView.findViewById(R.id.progress_offsides_home);
        home5.setProgress((data.getHomeOffsides()* 100) / totalOffSides);

        RoundCornerProgressBar home6 = rootView.findViewById(R.id.yellow_cards_progress_home);
        home6.setProgress((data.getHomeYellowCards()* 100) / totalYellowCards);

        RoundCornerProgressBar home8 = rootView.findViewById(R.id.progress_saves_home);
        home8.setProgress((data.getHomeGoalkeeperSavers()* 100) / totalGoalkeeperSavers);

        mTvHomePossession.setText(String.valueOf(data.getHomeBallPossession()));
        mTvHomeShotsOnGoal.setText(String.valueOf(data.getHomeShotsOnGoal()));
        mTvHomeShotsTotal.setText(String.valueOf(data.getHomeTotalShotsOnGoal()));
        mTvHomeFoulsTotal.setText(String.valueOf(data.getHomeFouls()));
        mTvHomeCornersTotal.setText(String.valueOf(data.getHomeCornerKicks()));
        mTvHomeOffsidesTotal.setText(String.valueOf(data.getHomeOffsides()));
        mTvHomeYellowCards.setText(String.valueOf(data.getHomeYellowCards()));
        mTvHomeSavers.setText(String.valueOf(data.getHomeGoalkeeperSavers()));

        mTvAwayPossession.setText(String.valueOf(data.getAwayBallPossession()));
        mTvAwayShotsOnGoal.setText(String.valueOf(data.getAwayShotsOnGoal()));
        mTvAwayShotsTotal.setText(String.valueOf(data.getAwayTotalShotsOnGoal()));
        mTvAwayFoulsTotal.setText(String.valueOf(data.getAwayFouls()));
        mTvAwayCornersTotal.setText(String.valueOf(data.getAwayCornerKicks()));
        mTvAwayOffsidesTotal.setText(String.valueOf(data.getAwayOffsides()));
        mTvAwayYellowCards.setText(String.valueOf(data.getAwayYellowCards()));
        mTvAwaySavers.setText(String.valueOf(data.getAwayGoalkeeperSavers()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStatisticPresenter.detachView();
    }
}
