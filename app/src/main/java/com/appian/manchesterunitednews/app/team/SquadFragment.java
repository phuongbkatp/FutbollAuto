package com.appian.manchesterunitednews.app.team;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.player.PlayerDetailsActivity;
import com.appian.manchesterunitednews.app.team.presenter.TeamSquadPresenter;
import com.appian.manchesterunitednews.app.team.view.TeamSquadView;
import com.appian.manchesterunitednews.app.widget.SectionWrapper;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.util.ItemClickSupport;
import com.appnet.android.football.sofa.data.Player;

import java.util.ArrayList;
import java.util.List;

public class SquadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        TeamSquadView {
    private static final String FRAGMENT_TITLE = "Squad";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<PositionPlayers> mPlayerList;
    private SquadAdapter mSquadAdapter;

    private TeamSquadPresenter mTeamSquadPresenter;
    private int mTeamId;

    public static SquadFragment newInstance(int teamId) {
        SquadFragment fragment = new SquadFragment();
        Bundle args = new Bundle();
        args.putInt("team_id", teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerList = new ArrayList<>();
        mSquadAdapter = new SquadAdapter(getActivity(), mPlayerList);
        if(getArguments() != null) {
            mTeamId = getArguments().getInt("team_id");
        }
        mTeamSquadPresenter = new TeamSquadPresenter();
        mTeamSquadPresenter.attachView(this);
        loadPlayer();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_player;
    }

    /* This event is triggered soon after onCreateView() */
    /* Any view setup should occur here.  E.g., view lookups and attaching view listeners */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /* Set the fragment title */
        if(getActivity() != null) {
            getActivity().setTitle(FRAGMENT_TITLE);
        }

        /* Initialise our listView */
        RecyclerView recyclerView = view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mSquadAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        /* Provide an onClickListener to RecyclerView items */
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SectionWrapper<PositionPlayers, Player> wrapper = mSquadAdapter.getItem(position);
                if(wrapper.getChild() != null) {
                    displayPlayer(wrapper.getChild());
                }
            }
        });

        /* Initialise our SwipeRefreshLayout */
        mSwipeRefreshLayout = view.findViewById(R.id.player_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadPlayer();
    }

    private void loadPlayer() {
        mTeamSquadPresenter.loadTeamSquad(mTeamId);
        showLoading(true);
    }

    private void displayPlayer(Player player) {
        /* Start a new Intent that passes player information to the SquadDetailsActivity */
        Intent intent = new Intent(getActivity(), PlayerDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_SOFA_PLAYER_ID, player.getId());
        intent.putExtra(Constant.EXTRA_KEY_PLAYER_NAME, player.getName());
        startActivity(intent);
    }

    @Override
    public void showTeamSquad(List<Player> data) {
        showLoading(false);
        if(data == null) {
            return;
        }
        mPlayerList.clear();
        mPlayerList.addAll(PositionPlayers.valueOf(data));
        mSquadAdapter.notifyDataChanged();
    }

    @Override
    public void onLoadTeamSquadFail() {
        showLoading(false);
    }

    private void showLoading(boolean isLoading) {
        if(getView() != null) {
            mSwipeRefreshLayout.setRefreshing(isLoading);
        }
    }
}
