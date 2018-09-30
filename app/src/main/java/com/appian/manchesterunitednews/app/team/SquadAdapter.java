package com.appian.manchesterunitednews.app.team;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.sofa.helper.PositionHelper;
import com.appnet.android.football.sofa.helper.SofaImageHelper;
import com.appian.manchesterunitednews.app.widget.SectionRecyclerViewAdapter;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appnet.android.football.sofa.data.Player;

import java.util.List;


class SquadAdapter extends SectionRecyclerViewAdapter<PositionPlayers, Player, SquadAdapter.PositionHolder, SquadAdapter.PlayerHolder> {
    private Context mContext;


    SquadAdapter(Context context, List<PositionPlayers> playerList) {
        super(playerList);
        this.mContext = context;
    }

    @Override
    public void onBindSectionViewHolder(PositionHolder sectionViewHolder, int sectionPosition, PositionPlayers section) {
        sectionViewHolder.tvName.setText(PositionHelper.getPosition(mContext.getResources(), section.getPosition()));
    }

    @Override
    public void onBindChildViewHolder(PlayerHolder childViewHolder, int sectionPosition, int childPosition, Player child) {
        childViewHolder.tvName.setText(child.getName());
        childViewHolder.tvNumber.setText(String.valueOf(child.getShirtNumber()));
        childViewHolder.tvNational.setText(child.getNationality());
        String currency = (TextUtils.isEmpty(child.getMarketValueCurrency())) ? "" : child.getMarketValueCurrency();
        childViewHolder.tvPlayerValue.setText(Utils.formatTransferValue(child.getMarketValue(), currency));
        childViewHolder.tvPlayerAge.setText(mContext.getString(R.string.squad_player_year_old, child.getAge()));
        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(child.getId()), childViewHolder.imgPlayer);
    }

    @Override
    public PositionHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_squad_position_header, sectionViewGroup, false);
        return new PositionHolder(header);
    }

    @Override
    public PlayerHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_squad_player, childViewGroup, false);
        return new PlayerHolder(header);
    }


    static class PlayerHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvNumber;
        TextView tvNational;
        TextView tvPlayerValue;
        TextView tvPlayerAge;
        ImageView imgPlayer;

        PlayerHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_player_name);
            tvNumber = itemView.findViewById(R.id.tv_player_shirt_number);
            tvNational = itemView.findViewById(R.id.tv_player_national);
            tvPlayerValue = itemView.findViewById(R.id.tv_player_value);
            tvPlayerAge = itemView.findViewById(R.id.tv_player_age);
            imgPlayer = itemView.findViewById(R.id.img_player_image);
        }
    }

    static class PositionHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        PositionHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_position_name);
        }
    }
}
