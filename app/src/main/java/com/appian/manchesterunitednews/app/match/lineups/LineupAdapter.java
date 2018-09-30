package com.appian.manchesterunitednews.app.match.lineups;

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
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.sofa.helper.SofaImageHelper;
import com.appian.manchesterunitednews.util.ImageLoader;

import java.util.List;

class LineupAdapter extends BaseRecyclerViewAdapter<LineupItem, LineupAdapter.ViewHolder> {

    LineupAdapter(Context context, List<LineupItem> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_detail_event_lineup_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LineupItem item = mData.get(position);
        holder.TvHomeTeamPlayerName.setText(item.getHomePlayer().getName());
        holder.TvHomeTeamPlayerNumber.setText(String.valueOf(item.getHomePlayer().getNumber()));
        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(item.getHomePlayer().getId()), holder.ImgHomeTeamPlayerImage);
        holder.TvAwayTeamPlayerName.setText(item.getAwayPlayer().getName());
        holder.TvAwayTeamPlayerNumber.setText(String.valueOf(item.getAwayPlayer().getNumber()));
        ImageLoader.displayImage(SofaImageHelper.getSofaImgPlayer(item.getAwayPlayer().getId()), holder.ImgAwayTeamPlayerImage);
        showPlayerDetail(holder.ViewHomeTeam, item.getHomePlayer());
        showPlayerDetail(holder.ViewAwayTeam, item.getAwayPlayer());
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
                ViewHelper.displayPlayerDetail(mContext, item.getId(), item.getName());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TvHomeTeamPlayerNumber;
        TextView TvHomeTeamPlayerName;
        ImageView ImgHomeTeamPlayerImage;
        TextView TvAwayTeamPlayerNumber;
        TextView TvAwayTeamPlayerName;
        ImageView ImgAwayTeamPlayerImage;
        View ViewHomeTeam;
        View ViewAwayTeam;

        ViewHolder(View view) {
            super(view);
            ViewHomeTeam = view.findViewById(R.id.view_home_team);
            ViewAwayTeam = view.findViewById(R.id.view_away_team);
            TvHomeTeamPlayerNumber = view.findViewById(R.id.tv_match_detail_lineups_home_team_player_number);
            TvHomeTeamPlayerName = view.findViewById(R.id.tv_match_detail_lineups_home_team_player_name);
            ImgHomeTeamPlayerImage = view.findViewById(R.id.img_match_detail_lineups_home_player);
            TvAwayTeamPlayerNumber = view.findViewById(R.id.tv_match_detail_lineups_away_team_player_number);
            TvAwayTeamPlayerName = view.findViewById(R.id.tv_match_detail_lineups_away_team_player_name);
            ImgAwayTeamPlayerImage = view.findViewById(R.id.img_match_detail_lineups_away_player);
        }
    }
}
