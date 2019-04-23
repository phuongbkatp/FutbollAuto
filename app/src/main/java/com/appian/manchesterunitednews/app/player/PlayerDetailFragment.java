package com.appian.manchesterunitednews.app.player;

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
import com.appian.manchesterunitednews.app.player.presenter.PlayerDetailPresenter;
import com.appian.manchesterunitednews.app.player.presenter.PlayerTransfersPresenter;
import com.appian.manchesterunitednews.app.player.view.PlayerDetailView;
import com.appian.manchesterunitednews.app.player.view.PlayerTransferView;
import com.appian.manchesterunitednews.app.team.TeamDetailsActivity;
import com.appian.manchesterunitednews.data.interactor.PlayerInteractor;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.ItemClickSupport;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.sofa.data.Player;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.data.Transfer;
import com.appnet.android.football.sofa.helper.FlagHelper;
import com.appnet.android.football.sofa.helper.PositionHelper;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.appian.manchesterunitednews.Constant.EXTRA_KEY_PLAYER_NAME;

public class PlayerDetailFragment extends BaseStateFragment implements PlayerDetailView, PlayerTransferView {
    private ImageView mImgTeamLogo;
    private TextView mTvTeamName;
    private TextView mTvContractUtil;
    private TextView mTvNationality;
    private ImageView mImgNationalityLogo;
    private TextView mTvAge;
    private TextView mTvBirthday;
    private TextView mTvHeight;
    private TextView mTvPreferredFoot;
    private TextView mTvPosition;
    private TextView mTvShirtNumber;

    private PlayerTransferHistoryAdapter mTransferAdapter;

    private int mPlayerId;
    private String mPlayerName;

    private Player mPlayer;
    private List<Transfer> mTransferHistory;

    private PlayerDetailPresenter mPlayerDetailPresenter;
    private PlayerTransfersPresenter mPlayerTransfersPresenter;

    public static PlayerDetailFragment newInstance(int playerId, String playerName) {
        PlayerDetailFragment fragment = new PlayerDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRA_KEY_SOFA_PLAYER_ID, playerId);
        args.putString(EXTRA_KEY_PLAYER_NAME, playerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mPlayerId = args.getInt(Constant.EXTRA_KEY_SOFA_PLAYER_ID);
            mPlayerName = args.getString(EXTRA_KEY_PLAYER_NAME);
        }

        mTransferHistory = new ArrayList<>();
        mTransferAdapter = new PlayerTransferHistoryAdapter(getContext(), mTransferHistory);
        PlayerInteractor playerInteractor = new PlayerInteractor();
        mPlayerDetailPresenter = new PlayerDetailPresenter(playerInteractor);
        mPlayerDetailPresenter.attachView(this);
        mPlayerTransfersPresenter = new PlayerTransfersPresenter(playerInteractor);
        mPlayerTransfersPresenter.attachView(this);
        loadPlayerDetailData();
        loadTransferData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImgTeamLogo = view.findViewById(R.id.img_player_detail_team_logo);
        mTvTeamName = view.findViewById(R.id.tv_player_detail_team_name);
        mTvContractUtil = view.findViewById(R.id.tv_player_detail_contract_util);
        ImageView imgPlayer = view.findViewById(R.id.img_player_detail_image);
        mTvNationality = view.findViewById(R.id.tv_player_detail_nationality);
        mImgNationalityLogo = view.findViewById(R.id.img_player_detail_nationality_logo);
        mTvAge = view.findViewById(R.id.tv_player_detail_age);
        mTvBirthday = view.findViewById(R.id.tv_player_detail_birthday);
        mTvHeight = view.findViewById(R.id.tv_player_detail_height);
        mTvPreferredFoot = view.findViewById(R.id.tv_player_detail_preferred_foot);
        mTvPosition = view.findViewById(R.id.tv_player_detail_position);
        mTvShirtNumber = view.findViewById(R.id.tv_player_detail_shirt_number);
        RecyclerView recyclerViewTransfer = view.findViewById(R.id.list_transfer_history);
        TextView tvPlayerName = view.findViewById(R.id.player_name);
        View btnBack = view.findViewById(R.id.img_back_arrow);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        recyclerViewTransfer.setNestedScrollingEnabled(false);
        recyclerViewTransfer.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTransfer.setAdapter(mTransferAdapter);
        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(mPlayerId), imgPlayer);

        tvPlayerName.setText(this.mPlayerName);
        notifyDataChange();
        ItemClickSupport.addTo(recyclerViewTransfer).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Transfer item = mTransferAdapter.getItem(position);
                if(item == null) {
                    return;
                }
                displayTeam(item.getTo());
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail_player;
    }

    private void loadPlayerDetailData() {
        mPlayerDetailPresenter.loadPlayerDetail(mPlayerId);
    }

    private void loadTransferData() {
        mPlayerTransfersPresenter.loadPlayerTransfers(mPlayerId);
    }

    private void fillData() {
        if (mPlayer == null || getView() == null) {
            return;
        }
        Resources res = getResources();
        Team team = mPlayer.getTeam();
        if (team != null) {
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(team.getId()), mImgTeamLogo);
            mTvTeamName.setText(team.getName());
            mTvContractUtil.setText(res.getString(R.string.player_detail_contract_util,
                    Utils.formatDateMonthYear(new Date(mPlayer.getContractUntilTimestamp()))));
        }
        mTvNationality.setText(mPlayer.getNationality());
        mImgNationalityLogo.setImageDrawable(FlagHelper.getFlag(res, mPlayer.getFlag()));
        mTvAge.setText(res.getString(R.string.squad_player_year_old, mPlayer.getAge()));
        mTvBirthday.setText(Utils.formatDateMonthYear(new Date(mPlayer.getDateOfBirthTimestamp())));
        mTvHeight.setText(res.getString(R.string.player_detail_height, mPlayer.getHeight()));
        mTvPreferredFoot.setText(PositionHelper.getPreferFoot(res, mPlayer.getPreferredFoot()));
        mTvPosition.setText(PositionHelper.getPosition(res, mPlayer.getPosition()));
        mTvShirtNumber.setText(String.valueOf(mPlayer.getShirtNumber()));
    }

    private void notifyDataChange() {
        if (getView() != null) {
            fillData();
        }
    }

    @Override
    public void showPlayerDetail(Player data) {
        if(data == null) {
            return;
        }
        mPlayer = data;
        fillData();
    }

    @Override
    public void showPlayerTransfer(List<Transfer> data) {
        if (data == null) {
            return;
        }
        mTransferHistory.clear();
        mTransferHistory.addAll(data);
        mTransferAdapter.notifyDataSetChanged();
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
