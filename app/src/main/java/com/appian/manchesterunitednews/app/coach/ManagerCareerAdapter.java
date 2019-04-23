package com.appian.manchesterunitednews.app.coach;

import android.content.Context;
import android.content.res.Resources;
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
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.sofa.data.ManagerCareer;
import com.appnet.android.football.sofa.data.Team;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

class ManagerCareerAdapter extends BaseRecyclerViewAdapter<ManagerCareer, RecyclerView.ViewHolder> {

    ManagerCareerAdapter(Context context) {
        super(context);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manager_career_history, parent, false);
        return new CareerHolder(view);
    }

    public ManagerCareer getItem(int position) {
        if(position < 0 || mData == null || mData.isEmpty()) {
            return null;
        }
        if(position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Resources res = mContext.getResources();
        CareerHolder holder = (CareerHolder) viewHolder;
        ManagerCareer item = mData.get(position);
        if (item.getLosses() > 0) {
            holder.TvLoss.setText(String.valueOf(item.getLosses()));
        }
        if (item.getDraws() > 0) {
            holder.TvDraw.setText(String.valueOf(item.getDraws()));
        }
        if (item.getWins() > 0) {
            holder.TvWin.setText(String.valueOf(item.getWins()));
        }
        if (item.getMatches() > 0) {
            holder.TvMatch.setText(String.valueOf(item.getMatches()));
            holder.TvMatchPerPoint.setText(ViewHelper.formatFloat(item.getPointsPerMatch()));
        }
        if (item.getEndTimestamp() == 0) {
            holder.TvTime.setText(res.getString(R.string.manager_career_start_time,
                    Utils.formatMonthYear(item.getStartTimestamp())));
        } else {
            holder.TvTime.setText(res.getString(R.string.manager_career_start_end_time,
                    Utils.formatMonthYear(item.getStartTimestamp()), Utils.formatMonthYear(item.getEndTimestamp())));
        }
        Team team = item.getTeam();
        if (team != null) {
            ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(team.getId()), holder.ImgTeamLogo);
            holder.TvTeamName.setText(team.getName());
        }
    }

    private static class CareerHolder extends RecyclerView.ViewHolder {
        TextView TvMatchPerPoint;
        TextView TvTime;
        TextView TvMatch;
        TextView TvTeamName;
        ImageView ImgTeamLogo;
        TextView TvWin;
        TextView TvDraw;
        TextView TvLoss;

        CareerHolder(View itemView) {
            super(itemView);
            TvMatchPerPoint = itemView.findViewById(R.id.tv_manager_career_match_per_point);
            TvTime = itemView.findViewById(R.id.tv_manager_career_time);
            TvMatch = itemView.findViewById(R.id.tv_manager_career_matches);
            TvTeamName = itemView.findViewById(R.id.tv_manager_career_team_name);
            ImgTeamLogo = itemView.findViewById(R.id.img_manager_career_team_image);
            TvWin = itemView.findViewById(R.id.tv_manager_career_wins);
            TvDraw = itemView.findViewById(R.id.tv_manager_career_draws);
            TvLoss = itemView.findViewById(R.id.tv_manager_career_loss);
        }
    }
}
