package com.appian.manchesterunitednews.app.coach;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.coach.presenter.CoachDetailPresenter;
import com.appian.manchesterunitednews.app.coach.view.CoachDetailView;
import com.appian.manchesterunitednews.app.team.TeamDetailsActivity;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.ItemClickSupport;
import com.appian.manchesterunitednews.util.Utils;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.sofa.data.GeneralInfo;
import com.appnet.android.football.sofa.data.Manager;
import com.appnet.android.football.sofa.data.ManagerCareer;
import com.appnet.android.football.sofa.data.ManagerPerformance;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.helper.FlagHelper;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

import java.util.Date;

import static com.appian.manchesterunitednews.Constant.EXTRA_KEY_MANAGER_NAME;

public class CoachDetailFragment extends BaseStateFragment implements CoachDetailView {
    private ImageView mImgTeamLogo;
    private TextView mTvTeamName;
    private ImageView mImgCoach;
    private TextView mTvNationality;
    private ImageView mImgNationalityLogo;
    private TextView mTvAge;
    private TextView mTvBirthday;
    private TextView mTvPrefFormation;
    private TextView mTvMatches;
    private TextView mTvPointsMatch;
    private RecyclerView mRecyclerViewCareer;
    private TextView mTvPlayerName;

    private ManagerCareerAdapter mAdapter;

    private int mCoachId;
    private String mCoachName;

    private CoachDetailPresenter mCoachDetailPresenter;

    public static CoachDetailFragment newInstance(int managerId, String managerName) {
        CoachDetailFragment fragment = new CoachDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRA_KEY_SOFA_MANAGER_ID, managerId);
        args.putString(Constant.EXTRA_KEY_MANAGER_NAME, managerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mCoachId = args.getInt(Constant.EXTRA_KEY_SOFA_MANAGER_ID);
            mCoachName = args.getString(EXTRA_KEY_MANAGER_NAME);
        }
        mAdapter = new ManagerCareerAdapter(getContext());
        mCoachDetailPresenter = new CoachDetailPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mRecyclerViewCareer.setNestedScrollingEnabled(false);
        mRecyclerViewCareer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewCareer.setAdapter(mAdapter);
        ImageLoader.displayImage(SofaImageHelper.getSofaImgManager(mCoachId), mImgCoach);
        mTvPlayerName.setText(this.mCoachName);
        mCoachDetailPresenter.attachView(this);
        mCoachDetailPresenter.loadCoachDetail(mCoachId);
    }

    private void initView(View view) {
        mImgTeamLogo = view.findViewById(R.id.img_coach_detail_team_logo);
        mTvTeamName = view.findViewById(R.id.tv_coach_detail_team_name);
        mImgCoach = view.findViewById(R.id.img_coach_detail_image);
        mTvNationality = view.findViewById(R.id.tv_coach_detail_nationality);
        mImgNationalityLogo = view.findViewById(R.id.img_coach_detail_nationality_logo);
        mTvAge = view.findViewById(R.id.tv_coach_detail_age);
        mTvBirthday = view.findViewById(R.id.tv_coach_detail_birthday);
        mTvPrefFormation = view.findViewById(R.id.tv_coach_detail_pref_formation);
        mTvMatches = view.findViewById(R.id.tv_coach_detail_matches);
        mTvPointsMatch = view.findViewById(R.id.tv_coach_detail_points_match);
        mRecyclerViewCareer = view.findViewById(R.id.list_coach_detail_career);
        mTvPlayerName = view.findViewById(R.id.tv_coach_detail_name);
        View btnBack = view.findViewById(R.id.img_back_arrow);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail_coach;
    }

    @Override
    public void showCoachDetail(Manager coach) {
        if (coach == null) {
            return;
        }
        Resources res = getResources();
        Team team = coach.getTeam();
        if (team != null) {
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(team.getId()), mImgTeamLogo);
            mTvTeamName.setText(team.getName());
        }
        mTvNationality.setText(coach.getNationality());
        mImgNationalityLogo.setImageDrawable(FlagHelper.getFlag(res, coach.getFlag()));
        mTvAge.setText(res.getString(R.string.squad_player_year_old, coach.getAge()));
        mTvBirthday.setText(Utils.formatDateMonthYear(new Date(coach.getDateOfBirthTimestamp())));
        GeneralInfo generalInfo = coach.getGeneralInfo();
        if (generalInfo != null) {
            mTvPrefFormation.setText(generalInfo.getPreferredFormation());
        }
        ManagerPerformance performance = coach.getPerformance();
        if (performance != null) {
            mTvMatches.setText(String.valueOf(performance.getTotal()));
            mTvPointsMatch.setText(ViewHelper.formatFloat(1.0f * performance.getTotalPoints() / performance.getTotal()));
        }
        if(coach.getCareerHistory() != null) {
            mAdapter.updateData(coach.getCareerHistory());
        }
        ItemClickSupport.addTo(mRecyclerViewCareer).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                ManagerCareer item = mAdapter.getItem(position);
                if(item == null) {
                    return;
                }
                displayTeam(item.getTeam());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCoachDetailPresenter.detachView();
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
}
