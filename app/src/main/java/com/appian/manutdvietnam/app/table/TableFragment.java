package com.appian.manutdvietnam.app.table;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseStateFragment;
import com.appian.manutdvietnam.app.StateFragment;
import com.appian.manutdvietnam.app.league.OnLeagueUpdatedListener;
import com.appian.manutdvietnam.app.table.presenter.LeagueStandingPresenter;
import com.appian.manutdvietnam.app.table.view.LeagueStandingView;
import com.appnet.android.football.sofa.data.TableRowsData;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends BaseStateFragment implements SwipeRefreshLayout.OnRefreshListener, OnLeagueUpdatedListener,
        LeagueStandingView {
    private RecyclerView mRecyclerView;
    private LinearLayout mEmptyDataView;
    private SwipeRefreshLayout mRefreshLayout;

    private TableGroupAdapter mTableAdapter;
    private List<TableRowsSection> mTableRows;

    private int mLeagueId;
    private int mSeasonId;

    private LeagueStandingPresenter mLeagueStandingPresenter;

    public static TableFragment newInstance(int leagueId, int seasonId, StateFragment stateFragment) {

        Bundle args = new Bundle();
        args.putInt("league_id", leagueId);
        args.putInt("season_id", seasonId);
        TableFragment fragment = new TableFragment();
        fragment.setStateFragment(stateFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTableRows = new ArrayList<>();
        mTableAdapter = new TableGroupAdapter(getActivity(), mTableRows);
        Bundle args = getArguments();
        if (args != null) {
            mLeagueId = args.getInt("league_id");
            mSeasonId = args.getInt("season_id");
        }
        mLeagueStandingPresenter = new LeagueStandingPresenter();
        mLeagueStandingPresenter.attachView(this);
        loadData();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_table;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setRefreshing(false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mTableAdapter);
        mEmptyDataView = view.findViewById(R.id.linEmpty);
    }

    private void loadData() {
        if (mLeagueId == 0 && mSeasonId == 0) {
            return;
        }
        showLoading(true);
        mLeagueStandingPresenter.loadStanding(mLeagueId, mSeasonId);
    }

    private void fillData(List<TableRowsData> data) {
        if (data == null || data.isEmpty()) {
            mEmptyDataView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyDataView.setVisibility(View.GONE);
        }
        mTableRows.clear();
        mTableRows.addAll(TableRowsSection.valueOf(data));
        mTableAdapter.notifyDataChanged();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLeagueUpdated(int leagueId, int seasonId) {
        mLeagueId = leagueId;
        mSeasonId = seasonId;
        loadData();
    }

    @Override
    public void showLeagueStanding(List<TableRowsData> data) {
        showLoading(false);
        fillData(data);
    }

    @Override
    public void onLoadLeagueStandingFail() {
        showLoading(false);
    }

    private void showLoading(boolean isLoading) {
        if (getView() != null) {
            mRefreshLayout.setRefreshing(isLoading);
        }
    }
}
