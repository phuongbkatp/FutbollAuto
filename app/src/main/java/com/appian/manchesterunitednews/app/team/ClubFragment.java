package com.appian.manchesterunitednews.app.team;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.coach.CoachDetailsActivity;
import com.appian.manchesterunitednews.app.player.PlayerDetailsActivity;
import com.appian.manchesterunitednews.app.team.presenter.TeamDetailPresenter;
import com.appian.manchesterunitednews.app.team.presenter.TeamPerformancePresenter;
import com.appian.manchesterunitednews.app.team.view.TeamDetailView;
import com.appian.manchesterunitednews.app.team.view.TeamPerformanceView;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.interactor.TeamInteractor;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.sofa.data.City;
import com.appnet.android.football.sofa.data.Manager;
import com.appnet.android.football.sofa.data.Opponent;
import com.appnet.android.football.sofa.data.Performance;
import com.appnet.android.football.sofa.data.Player;
import com.appnet.android.football.sofa.data.PlayerTransfer;
import com.appnet.android.football.sofa.data.Stadium;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.data.TeamTransfer;
import com.appnet.android.football.sofa.data.Venue;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

import java.util.Date;
import java.util.List;

public class ClubFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        TeamPerformanceView, TeamDetailView {
    private LinearLayout mLvTeamPerformances;
    private LinearLayout mLvArrival;
    private LinearLayout mLvDeparture;
    private ImageView mImgManager;
    private TextView mTvManagerName;
    private TextView mTvFoundDate;
    private TextView mTvStadiumName;
    private TextView mTvStadiumCapacity;
    private TextView mTvCity;

    private Team mTeam;
    private TeamPerformancePresenter mTeamPerformancePresenter;
    private TeamDetailPresenter mTeamDetailPresenter;

    private View.OnClickListener mOnPlayerClickListener;

    public static ClubFragment newInstance(int teamId) {
        ClubFragment fragment = new ClubFragment();
        Bundle args = new Bundle();
        args.putInt("team_id", teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int teamId = 0;
        if(getArguments() != null) {
            teamId = getArguments().getInt("team_id");
        }
        TeamInteractor teamInteractor = new TeamInteractor();
        mTeamPerformancePresenter = new TeamPerformancePresenter(teamInteractor);
        mTeamPerformancePresenter.attachView(this);
        mTeamPerformancePresenter.loadTeamPerformance(teamId);
        mTeamDetailPresenter = new TeamDetailPresenter(teamInteractor);
        mTeamDetailPresenter.attachView(this);
        mTeamDetailPresenter.loadTeamDetail(teamId);
        mOnPlayerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player player = (Player) view.getTag();
                if(player == null) {
                    return;
                }
                displayPlayer(player);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mLvTeamPerformances = view.findViewById(R.id.lv_team_detail_performances);
        mLvArrival = view.findViewById(R.id.lv_team_detail_latest_arrival);
        mLvDeparture = view.findViewById(R.id.lv_team_detail_latest_departure);
        mTvManagerName = view.findViewById(R.id.tv_team_detail_manager_name);
        mImgManager = view.findViewById(R.id.img_team_detail_manager);
        mTvFoundDate = view.findViewById(R.id.tv_team_detail_found_date);
        mTvStadiumName = view.findViewById(R.id.tv_team_detail_stadium_name);
        mTvStadiumCapacity = view.findViewById(R.id.tv_team_detail_stadium_capacity);
        mTvCity = view.findViewById(R.id.tv_team_detail_city);
        View btnDetailCoach = view.findViewById(R.id.btn_team_detail_manager);
        btnDetailCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTeam == null) {
                    return;
                }
                Manager manager = mTeam.getManager();
                if(manager == null) {
                    return;
                }
                displayManager(manager);
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_club;
    }

    @Override
    public void onRefresh() {
        /* Called when a user swipes to refresh the list */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTeamDetailPresenter.detachView();
        mTeamPerformancePresenter.detachView();
    }

    private void displayManager(Manager manager) {
        Intent intent = new Intent(getActivity(), CoachDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_SOFA_MANAGER_ID, manager.getId());
        intent.putExtra(Constant.EXTRA_KEY_MANAGER_NAME, manager.getName());
        startActivity(intent);
    }

    @Override
    public void showTeamPerformance(List<Performance> data) {
        if(data == null || getView() == null) {
            return;
        }
        Resources res = getResources();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        for(Performance item : data) {
            View view = View.inflate(getContext(), R.layout.item_team_performance, null);
            TextView tvWinFlag = view.findViewById(R.id.tv_win_flag);
            ImageView imgTeamLogo = view.findViewById(R.id.img_team_logo);
            String winFlag = item.getWinFlag();
            tvWinFlag.setText(winFlag);
            if ("W".equals(winFlag)) {
                ViewHelper.setBackground(tvWinFlag, res.getDrawable(R.drawable.circle_green));
            } else if ("L".equals(winFlag)) {
                ViewHelper.setBackground(tvWinFlag, res.getDrawable(R.drawable.circle_red));
            } else {
                ViewHelper.setBackground(tvWinFlag, res.getDrawable(R.drawable.circle_gray));
            }
            Opponent opponent = item.getOpponent();
            if(opponent != null) {
                ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(opponent.getId()), imgTeamLogo);
            }
            mLvTeamPerformances.addView(view, params);
        }
    }

    @Override
    public void showTeamDetail(Team data) {
        mTeam = data;
        if (mTeam == null || getView() == null) {
            return;
        }
        Manager manager = mTeam.getManager();
        if (manager != null) {
            mTvManagerName.setText(manager.getName());
            ImageLoader.displayImage(SofaImageHelper.getSofaImgManager(manager.getId()), mImgManager);
        }
        mTvFoundDate.setText(Utils.formatDateMonthYear(new Date(mTeam.getFoundationDateTimestamp())));
        Venue venue = mTeam.getVenue();
        if (venue != null) {
            Stadium stadium = venue.getStadium();
            if (stadium != null) {
                mTvStadiumName.setText(stadium.getName());
                mTvStadiumCapacity.setText(String.valueOf(stadium.getCapacity()));
            }
            City city = venue.getCity();
            if (city != null) {
                mTvCity.setText(city.getName());
            }
        }
        // Transfers
        TeamTransfer transfers = mTeam.getTransfers();
        if(transfers != null) {
            List<PlayerTransfer> inPlayers = transfers.getIn();
            if(inPlayers != null) {
                for(PlayerTransfer item : inPlayers) {
                    Player player = item.getPlayer();
                    if(player != null) {
                        View view = View.inflate(getContext(), R.layout.item_player_transfer, null);
                        TextView tvPlayerName = view.findViewById(R.id.tv_player_name);
                        tvPlayerName.setText(player.getName());
                        ImageView imgPlayerImage = view.findViewById(R.id.img_player_image);
                        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(player.getId()), imgPlayerImage);
                        mLvArrival.addView(view);
                        view.setTag(player);
                        view.setOnClickListener(mOnPlayerClickListener);
                    }
                }
            }
            List<PlayerTransfer> outPlayers = transfers.getOut();
            if(outPlayers != null) {
                for(PlayerTransfer item : outPlayers) {
                    Player player = item.getPlayer();
                    if(player != null) {
                        View view = View.inflate(getContext(), R.layout.item_player_transfer, null);
                        TextView tvPlayerName = view.findViewById(R.id.tv_player_name);
                        tvPlayerName.setText(player.getName());
                        ImageView imgPlayerImage = view.findViewById(R.id.img_player_image);
                        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(player.getId()), imgPlayerImage);
                        mLvDeparture.addView(view);
                        view.setTag(player);
                        view.setOnClickListener(mOnPlayerClickListener);
                    }
                }
            }
        }
    }

    private void displayPlayer(Player player) {
        Intent intent = new Intent(getActivity(), PlayerDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_SOFA_PLAYER_ID, player.getId());
        intent.putExtra(Constant.EXTRA_KEY_PLAYER_NAME, player.getName());
        startActivity(intent);
    }

}
