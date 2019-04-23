package com.appian.manchesterunitednews.app.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseRecyclerViewAdapter;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.data.Transfer;
import com.appnet.android.football.sofa.helper.SofaImageHelper;
import com.appnet.android.football.sofa.helper.TransferHelper;

import java.util.Date;
import java.util.List;

class PlayerTransferHistoryAdapter extends BaseRecyclerViewAdapter<Transfer, PlayerTransferHistoryAdapter.TransferHolder> {

    PlayerTransferHistoryAdapter(Context context, List<Transfer> data) {
        super(context, data);
    }

    @Override
    @NonNull
    public TransferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_player_transfer_history, parent, false);
        return new TransferHolder(view);
    }

    public Transfer getItem(int position) {
        if(position < 0 || mData == null || mData.isEmpty()) {
            return null;
        }
        if(position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }


    @Override
    public void onBindViewHolder(@NonNull TransferHolder holder, int position) {
        Transfer item = mData.get(position);
        Team toTeam = item.getTo();
        if(toTeam != null) {
            holder.tvTeamName.setText(toTeam.getName());
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(toTeam.getId()), holder.imgTeamLogo);
        }
        holder.tvTime.setText(Utils.formatDateMonthYear(new Date(item.getTransferDateTimestamp())));
        holder.tvValue.setText(item.getTransferFeeDescription());
        holder.tvType.setText(TransferHelper.getTransferType(mContext.getResources(), item.getType()));
    }

    static class TransferHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvTime;
        TextView tvValue;
        TextView tvTeamName;
        ImageView imgTeamLogo;

        TransferHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_player_transfer_type);
            tvTime = itemView.findViewById(R.id.tv_player_transfer_time);
            tvValue = itemView.findViewById(R.id.tv_player_transfer_value);
            tvTeamName = itemView.findViewById(R.id.tv_player_transfer_team_name);
            imgTeamLogo = itemView.findViewById(R.id.img_player_transfer_team_image);
        }
    }
}
