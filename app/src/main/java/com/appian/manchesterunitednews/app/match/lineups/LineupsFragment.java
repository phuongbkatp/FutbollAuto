package com.appian.manchesterunitednews.app.match.lineups;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseRecyclerViewAdapter;
import com.appian.manchesterunitednews.app.StateFragment;
import com.appian.manchesterunitednews.app.coach.CoachDetailsActivity;
import com.appian.manchesterunitednews.app.match.BaseLiveFragment;
import com.appian.manchesterunitednews.app.match.OnMatchUpdatedListener;
import com.appian.manchesterunitednews.app.match.presenter.MatchLineupsPresenter;
import com.appian.manchesterunitednews.app.match.view.MatchLineupsView;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.LineupsData;
import com.appnet.android.football.sofa.data.Person;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

import java.util.ArrayList;
import java.util.List;

public class LineupsFragment extends BaseLiveFragment implements OnMatchUpdatedListener,
        MatchLineupsView {
    private static final String KEY_MATCH_ID = "MATCH_ID";

    private TextView mTvHomeTeamName;
    private ImageView mImgHomeTeamCoach;
    private TextView mTvHomeTeamCoach;
    private TextView mTvHomeTeamScheme;
    private ImageView mImgHomeTeamLogo;
    private TextView mTvAwayTeamName;
    private ImageView mImgAwayTeamCoach;
    private TextView mTvAwayTeamCoach;
    private TextView mTvAwayTeamScheme;
    private ImageView mImgAwayTeamLogo;

    private RecyclerView mLvSubstitutions;
    private LinearLayout mViewLineUp;
    private LinearLayout mViewNoInfo;
    private LinearLayout mViewLineUp33;
    private LinearLayout mViewLineUp34;
    private LinearLayout mViewLineUp43;
    private LinearLayout mViewLineUp44;

    private String[] mStrArray, mStrArray2;
    private int mDefend;
    private int mMidfield, mMidfield2;

    private BaseRecyclerViewAdapter mSubstitutionAdapter;

    private List<LineupItem> mLineups;
    private List<LineupItem> mSubstitutions;
    private LineupsData mLineupsData;
    private Event mMatchEvent;
    private int mMatchId;

    private Person mHomeCoach;
    private Person mAwayCoach;

    private MatchLineupsPresenter mMatchLineupsPresenter;

    public static LineupsFragment newInstance(int matchId, StateFragment stateFragment) {
        Bundle args = new Bundle();
        args.putInt(KEY_MATCH_ID, matchId);
        LineupsFragment fragment = new LineupsFragment();
        fragment.setArguments(args);
        fragment.setStateFragment(stateFragment);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.lineup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mLvSubstitutions.setNestedScrollingEnabled(false);
        mSubstitutionAdapter = new LineupAdapter(getContext(), mSubstitutions);
        mLvSubstitutions.setAdapter(mSubstitutionAdapter);
        mLvSubstitutions.setLayoutManager(new LinearLayoutManager(getContext()));

        fillTeamInfo();
        fillData();
    }

    private void initView(View view) {
        mTvHomeTeamName = view.findViewById(R.id.tv_match_detail_lineups_home_team_name);
        mImgHomeTeamCoach = view.findViewById(R.id.img_match_detail_lineups_home_team_coach);
        mTvHomeTeamCoach = view.findViewById(R.id.tv_match_detail_lineups_home_team_coach);
        mTvHomeTeamScheme = view.findViewById(R.id.tv_match_detail_lineups_home_team_scheme);
        mImgHomeTeamLogo = view.findViewById(R.id.img_match_detail_lineups_home_team_logo);
        mTvAwayTeamName = view.findViewById(R.id.tv_match_detail_lineups_away_team_name);
        mImgAwayTeamCoach = view.findViewById(R.id.img_match_detail_lineups_away_team_coach);
        mTvAwayTeamCoach = view.findViewById(R.id.tv_match_detail_lineups_away_team_coach);
        mTvAwayTeamScheme = view.findViewById(R.id.tv_match_detail_lineups_away_team_scheme);
        mImgAwayTeamLogo = view.findViewById(R.id.img_match_detail_lineups_away_team_logo);

        mLvSubstitutions = view.findViewById(R.id.lv_match_detail_substitution);
        mViewLineUp = view.findViewById(R.id.ll_line_up);
        mViewNoInfo = view.findViewById(R.id.ll_noinfo);

        mViewLineUp33 = view.findViewById(R.id.lineup33);
        mViewLineUp34 = view.findViewById(R.id.lineup34);
        mViewLineUp43 = view.findViewById(R.id.lineup43);
        mViewLineUp44 = view.findViewById(R.id.lineup44);

        View viewHomeCoach = view.findViewById(R.id.group_view_home_coach);
        View viewAwayCoach = view.findViewById(R.id.group_view_away_coach);
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.group_view_home_coach:
                        displayManager(mHomeCoach);
                        break;
                    case R.id.group_view_away_coach:
                        displayManager(mAwayCoach);
                        break;
                }
            }
        };
        viewHomeCoach.setOnClickListener(btnClickListener);
        viewAwayCoach.setOnClickListener(btnClickListener);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLineups = new ArrayList<>();
        mSubstitutions = new ArrayList<>();
        mMatchLineupsPresenter = new MatchLineupsPresenter();
        mMatchLineupsPresenter.attachView(this);
        Bundle args = getArguments();
        if (args != null) {
            mMatchId = args.getInt(KEY_MATCH_ID, 0);
            loadLineups(mMatchId);
        }
    }

    private void loadLineups(int matchId) {
        mMatchLineupsPresenter.loadMatchLineups(matchId);
    }

    private void fillData() {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        if (mLineupsData == null) {
            return;
        }
        String formationHome = mLineupsData.getHomeFormation();
        String formationAway = mLineupsData.getAwayFormation();
        if (formationHome.equals("") && formationAway.equals("")) {
            mViewNoInfo.setVisibility(View.VISIBLE);
            mViewLineUp.setVisibility(View.GONE);
            return;
        } else {
            mViewNoInfo.setVisibility(View.GONE);
            mViewLineUp.setVisibility(View.VISIBLE);
        }
        Resources res = getResources();
        mTvHomeTeamScheme.setText(res.getString(R.string.match_detail_lineups_team_scheme_s, formationHome));
        mTvAwayTeamScheme.setText(res.getString(R.string.match_detail_lineups_team_scheme_s, formationAway));

        formationHome = mLineupsData.getHomeFormation();
        formationAway = mLineupsData.getAwayFormation();
        mDefend = 0;
        mMidfield2 = 0;
        mMidfield = 0;
        mStrArray = formationHome.split("-");
        mStrArray2 = formationAway.split("-");
        if (mStrArray.length == 4 && mStrArray2.length == 4) {
            fillLineUp(mLineups, mViewLineUp44, mViewLineUp33, mViewLineUp34, mViewLineUp43);
        } else if (mStrArray.length == 4 && mStrArray2.length == 3) {
            fillLineUp(mLineups, mViewLineUp43, mViewLineUp33, mViewLineUp34, mViewLineUp44);
        } else if (mStrArray.length == 3 && mStrArray2.length == 4) {
            fillLineUp(mLineups, mViewLineUp34, mViewLineUp33, mViewLineUp44, mViewLineUp43);
        } else {
            fillLineUp(mLineups, mViewLineUp33, mViewLineUp44, mViewLineUp34, mViewLineUp43);

        }

        mSubstitutionAdapter.notifyDataSetChanged();
        if (mLineupsData.getHomeManager() != null) {
            mHomeCoach = mLineupsData.getHomeManager();
            mTvHomeTeamCoach.setText(res.getString(R.string.match_detail_lineups_coach_name_s, mLineupsData.getHomeManager().getName()));
            ImageLoader.displayImage(SofaImageHelper.getSofaImgManager(mLineupsData.getHomeManager().getId()), mImgHomeTeamCoach);
        }
        if (mLineupsData.getAwayManager() != null) {
            mAwayCoach = mLineupsData.getAwayManager();
            mTvAwayTeamCoach.setText(res.getString(R.string.match_detail_lineups_coach_name_s, mLineupsData.getAwayManager().getName()));
            ImageLoader.displayImage(SofaImageHelper.getSofaImgManager(mLineupsData.getAwayManager().getId()), mImgAwayTeamCoach);
        }
    }

    private void fillTeamInfo() {
        if (mMatchEvent == null) {
            return;
        }
        if (mMatchEvent.getHomeTeam() != null && mMatchEvent.getAwayTeam() != null) {
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(mMatchEvent.getHomeTeam().getId()), mImgHomeTeamLogo);
            mTvHomeTeamName.setText(mMatchEvent.getHomeTeam().getShortName());
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(mMatchEvent.getAwayTeam().getId()), mImgAwayTeamLogo);
            mTvAwayTeamName.setText(mMatchEvent.getAwayTeam().getShortName());
        }
    }

    @Override
    public void onMatchUpdated(Event event) {
        mMatchEvent = event;
        fillTeamInfo();
        checkLiveScore(event);
    }

    private void checkLiveScore(Event event) {
        if (event.getStatus() == null || mLineupsData == null) {
            return;
        }
        // Live score
        if (!isLive() && !mLineupsData.isConfirmed() && Constant.SOFA_MATCH_STATUS_NOT_STARTED.equals(event.getStatus().getType())) {
            startLive();
        } else if (isLive() && Constant.SOFA_MATCH_STATUS_FINISHED.equals(event.getStatus().getType())) {
            stopLive();
        } else if (isLive() && mLineupsData.isConfirmed()) {
            stopLive();
        }
    }

    @Override
    protected int getTimeLive() {
        return 30000;
    }

    @Override
    protected void onLive() {
        super.onLive();
        loadLineups(mMatchId);
    }

    @Override
    protected void onLiveStopped() {
        super.onLiveStopped();
        loadLineups(mMatchId);
    }

    private void fillLineUp(List<LineupItem> lineupList, LinearLayout lin1, LinearLayout lin2, LinearLayout lin3,
                            LinearLayout lin4) {
        lin1.setVisibility(View.VISIBLE);
        lin2.setVisibility(View.GONE);
        lin3.setVisibility(View.GONE);
        lin4.setVisibility(View.GONE);

        LinearLayout viewAttackers = lin1.findViewById(R.id.attackers);
        LinearLayout viewMidfielders = lin1.findViewById(R.id.midfielders);
        LinearLayout viewMidfielders2 = lin1.findViewById(R.id.midfielders2);
        LinearLayout viewDefenders = lin1.findViewById(R.id.defenders);
        mDefend = Integer.valueOf(mStrArray[0]);
        mMidfield = mDefend + Integer.valueOf(mStrArray[1]);
        if (mStrArray.length == 4) {
            mMidfield2 = mMidfield + Integer.valueOf(mStrArray[2]);
        }
        ImageView imgGoalKeeperHome = lin1.findViewById(R.id.img_player_image_gk_home);
        TextView tvNameGoalHome = lin1.findViewById(R.id.tv_player_name_gk_home);
        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(lineupList.get(0).getHomePlayer().getId()), imgGoalKeeperHome);
        tvNameGoalHome.setText(lineupList.get(0).getHomePlayer().getName());
        View viewGoalKeeperHome = lin1.findViewById(R.id.fl_player_image);
        showPlayerDetail(viewGoalKeeperHome, lineupList.get(0).getHomePlayer());
        for (int i = 0; i < 10; i++) {
            LineupItem item = lineupList.get(i + 1);
            View v = View.inflate(getContext(), R.layout.item_player_lineup, null);
            TextView tvName = v.findViewById(R.id.tv_player_name);
            TextView tvNumber = v.findViewById(R.id.tv_player_number);
            tvName.setText(item.getHomePlayer().getName());
            tvNumber.setText(String.valueOf(item.getHomePlayer().getNumber()));
            ImageView imgPlayer = v.findViewById(R.id.img_player_image);
            ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(item.getHomePlayer().getId()), imgPlayer);
            if (i < mDefend) {
                viewDefenders.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            } else if (i < mMidfield) {
                viewMidfielders.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            } else if (mStrArray.length == 4 && i < mMidfield2) {
                viewMidfielders2.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            } else {
                viewAttackers.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            }
            showPlayerDetail(v, item.getHomePlayer());
        }

        LinearLayout viewAttackerAways = lin1.findViewById(R.id.attackers_away);
        LinearLayout viewMidfielderAways = lin1.findViewById(R.id.midfielders_away);
        LinearLayout viewMidfielder2Aways = lin1.findViewById(R.id.midfielders2_away);
        LinearLayout viewDefenderAways = lin1.findViewById(R.id.defenders_away);
        RelativeLayout viewGoalKeeperAway = lin1.findViewById(R.id.fl_player_image_away);
        mDefend = 0;
        mMidfield = 0;
        mMidfield2 = 0;
        mDefend = Integer.valueOf(mStrArray2[0]);
        mMidfield = mDefend + Integer.valueOf(mStrArray2[1]);
        if (mStrArray2.length == 4) {
            mMidfield2 = mMidfield + Integer.valueOf(mStrArray2[2]);
        }
        ImageView imgGoalKeeperAway = lin1.findViewById(R.id.img_player_image_gk_away);
        TextView tvNameGoalAway = lin1.findViewById(R.id.tv_player_name_gk_away);
        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(lineupList.get(0).getAwayPlayer().getId()), imgGoalKeeperAway);
        tvNameGoalAway.setText(lineupList.get(0).getAwayPlayer().getName());
        showPlayerDetail(viewGoalKeeperAway, lineupList.get(0).getAwayPlayer());
        for (int i = 0; i < 10; i++) {
            LineupItem item = lineupList.get(i + 1);
            View v = View.inflate(getContext(), R.layout.item_player_lineup, null);
            TextView tvName = v.findViewById(R.id.tv_player_name);
            TextView tvNumber = v.findViewById(R.id.tv_player_number);
            tvName.setText(item.getAwayPlayer().getName());
            tvNumber.setText(String.valueOf(item.getAwayPlayer().getNumber()));
            ImageView imgPlayer = v.findViewById(R.id.img_player_image);
            ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(item.getAwayPlayer().getId()), imgPlayer);
            if (i < mDefend) {
                viewDefenderAways.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            } else if (i < mMidfield) {
                viewMidfielderAways.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            } else if (mStrArray2.length == 4 && i < mMidfield2) {
                viewMidfielder2Aways.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            } else {
                viewAttackerAways.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            }
            showPlayerDetail(v, item.getAwayPlayer());
        }
    }

    @Override
    public void showMatchLineups(LineupsData data) {
        mLineupsData = data;
        mLineups.clear();
        mLineups.addAll(LineupItem.getLineup(mLineupsData));
        mSubstitutions.clear();
        mSubstitutions.addAll(LineupItem.getSubstitution(mLineupsData));
        fillData();
    }

    private void showPlayerDetail(View view, PlayerItem player) {
        view.setTag(player);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerItem item = (PlayerItem) view.getTag();
                if (item == null) {
                    return;
                }
                ViewHelper.displayPlayerDetail(getContext(), item.getId(), item.getName());
            }
        });
    }

    private void displayManager(Person manager) {
        if(manager == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), CoachDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_SOFA_MANAGER_ID, manager.getId());
        intent.putExtra(Constant.EXTRA_KEY_MANAGER_NAME, manager.getName());
        startActivity(intent);
    }

}
