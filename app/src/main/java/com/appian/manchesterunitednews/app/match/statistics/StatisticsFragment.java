package com.appian.manchesterunitednews.app.match.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.StateFragment;
import com.appian.manchesterunitednews.app.match.BaseLiveFragment;
import com.appian.manchesterunitednews.app.match.OnMatchUpdatedListener;
import com.appian.manchesterunitednews.app.match.presenter.MatchStatisticPresenter;
import com.appian.manchesterunitednews.app.match.view.MatchStatisticView;
import com.appian.manchesterunitednews.data.interactor.MatchInteractor;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.Statistic;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends BaseLiveFragment implements OnMatchUpdatedListener, MatchStatisticView {
    private static final String KEY_MATCH_ID = "MATCH_ID";
    private RecyclerView mRecyclerView;
    private LinearLayout mTvNoData;

    private List<StatisticsGroupSection> mGroupRow;
    private StatisticGroupAdapter mAdapter;

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
        mGroupRow = new ArrayList<>();
        mAdapter = new StatisticGroupAdapter(getContext(), mGroupRow);
        mStatisticPresenter = new MatchStatisticPresenter(new MatchInteractor());
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        mTvNoData = rootView.findViewById(R.id.ll_nodatafound);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


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
    public void showMatchStatistic(Statistic data) {
        if (data == null || data.getAll() == null) {
            mRecyclerView.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.VISIBLE);
            return;
        }
        List<Statistic.Group> groups = data.getAll().getGroups();
        if(groups == null || groups.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.VISIBLE);
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.GONE);

        final View rootView = getView();
        if(rootView == null) {
            return;
        }
        mGroupRow.clear();
        mGroupRow.addAll(StatisticsGroupSection.valueOf(data.getAll().getGroups()));
        mAdapter.notifyDataChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStatisticPresenter.detachView();
    }
}
