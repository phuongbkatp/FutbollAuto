package com.appian.manchesterunitednews.app.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

import java.util.Date;

public class BannerFragment extends BaseFragment {

    private LinearLayout mBannerLayout;
    private TextView mTvTournament;
    private TextView mTvStadium;
    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvHomeTeamName;
    private ImageView mImgHomeTeamLogo;
    private TextView mTvAwayTeamName;
    private ImageView mImgAwayTeamLogo;
    private TextView mTvHomeTeamGold;
    private TextView mTvAwayTeamGold;
    private LinearLayout mLLMatchDetailGoals;
    private TextView mTvTimeRemaining;
    private TextView mTvMatchStt;

    private int mSofaMatchId = 0;

    public static BannerFragment newInstance(String tour, String stadium, long time, String stt_des, String status, int sofaMatchId,
                                             String homeTeamName, int homeTeamId,
                                             String awayTeamName, int awayTeamId,
                                             int homeTeamGoal, int awayTeamGoal) {
        Bundle args = new Bundle();
        args.putString("tour", tour);
        args.putString("stadium", stadium);
        args.putLong("time", time);
        args.putString("stt_des", stt_des);
        args.putString("status", status);
        args.putInt("sofaMatchId", sofaMatchId);
        args.putString("homeTeamName", homeTeamName);
        args.putInt("homeTeamId", homeTeamId);
        args.putInt("awayTeamId", awayTeamId);
        args.putInt("homeTeamGoal", homeTeamGoal);
        args.putInt("awayTeamGoal", awayTeamGoal);
        args.putString("awayTeamName", awayTeamName);
        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBannerLayout = view.findViewById(R.id.banner_layout);
        mTvTournament = view.findViewById(R.id.tv_match_detail_tournament);
        mTvStadium = view.findViewById(R.id.tv_match_detail_stadium);
        mTvDate = view.findViewById(R.id.tv_match_detail_date);
        mTvTime = view.findViewById(R.id.tv_match_detail_time);
        mTvHomeTeamName = view.findViewById(R.id.tv_match_detail_home_team_name);
        mImgHomeTeamLogo = view.findViewById(R.id.img_match_detail_home_team_logo);
        mTvAwayTeamName = view.findViewById(R.id.tv_match_detail_away_team_name);
        mImgAwayTeamLogo = view.findViewById(R.id.img_match_detail_away_team_logo);
        mTvHomeTeamGold = view.findViewById(R.id.tv_match_detail_home_team_goals);
        mTvAwayTeamGold = view.findViewById(R.id.tv_match_detail_away_team_goals);
        mLLMatchDetailGoals = view.findViewById(R.id.ll_match_detail_goals);
        mTvTimeRemaining = view.findViewById(R.id.tv_time_remaining);
        mTvMatchStt = view.findViewById(R.id.tv_match_stt);
    }

    private void fillData(Bundle arg) {
        String tour = arg.getString("tour");
        if (!TextUtils.isEmpty(tour)) {
            String[] text = tour.split(",");
            if (text.length > 1) {
                mTvTournament.setText(text[0]);
            } else {
                mTvTournament.setText(tour);
            }
        }
        mTvStadium.setText(arg.getString("stadium"));
        mTvDate.setText(Utils.formatWeekTime(getContext(), arg.getLong("time")));
        mTvTime.setText(Utils.formatDateMonthYear(new Date(arg.getLong("time"))));

        mTvHomeTeamName.setText(arg.getString("homeTeamName"));
        ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(arg.getInt("homeTeamId")), mImgHomeTeamLogo);
        mTvAwayTeamName.setText(arg.getString("awayTeamName"));
        ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(arg.getInt("awayTeamId")), mImgAwayTeamLogo);
        int currentHomeGoal = arg.getInt("homeTeamGoal");
        int currentAwayGoal = arg.getInt("awayTeamGoal");
        mSofaMatchId = arg.getInt("sofaMatchId");
        String status = arg.getString("status");
        String statusDes = arg.getString("stt_des");
        if (Constant.SOFA_MATCH_STATUS_CANCELED.equals(status) || Constant.SOFA_MATCH_STATUS_POSTPONED.equals(status)) {
            mLLMatchDetailGoals.setVisibility(View.GONE);
            mTvTimeRemaining.setVisibility(View.VISIBLE);
            mTvTimeRemaining.setText(getText(R.string.cancel_match));
            mTvMatchStt.setVisibility(View.GONE);
        } else if (!Constant.SOFA_MATCH_STATUS_NOT_STARTED.equals(arg.getString("status"))) {
            mLLMatchDetailGoals.setVisibility(View.VISIBLE);
            mTvTimeRemaining.setVisibility(View.GONE);
            mTvHomeTeamGold.setText(String.valueOf(currentHomeGoal));
            mTvAwayTeamGold.setText(String.valueOf(currentAwayGoal));
            mTvMatchStt.setVisibility(View.VISIBLE);
            mTvMatchStt.setText(statusDes);
        } else {
            mLLMatchDetailGoals.setVisibility(View.GONE);
            mTvTimeRemaining.setVisibility(View.VISIBLE);
            mTvTimeRemaining.setText(Utils.calculateRemainTime(getContext(), arg.getLong("time")));
            mTvMatchStt.setVisibility(View.GONE);
        }
        int color = (Constant.SOFA_MATCH_STATUS_IN_PROGRESS.equalsIgnoreCase(status)) ? R.drawable.circle_red : R.drawable.circle_black;
        mTvHomeTeamGold.setBackgroundResource(color);
        mTvAwayTeamGold.setBackgroundResource(color);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_banner;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillData(getArguments());
        mBannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MatchActivity.class);
                intent.putExtra(Constant.KEY_SOFA_MATCH_ID, mSofaMatchId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
