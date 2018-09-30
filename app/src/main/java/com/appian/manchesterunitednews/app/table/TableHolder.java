package com.appian.manchesterunitednews.app.table;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;

class TableHolder extends RecyclerView.ViewHolder {
    TextView TvPos;
    ImageView ImgLogo;
    TextView TvTeam;
    TextView TvPlayed;
    TextView TvWin;
    TextView TvDraw;
    TextView TvLoss;
    TextView TvGoals;
    TextView TvPoint;
    LinearLayout RowTeam;

    TableHolder(View view) {
        super(view);
        this.ImgLogo = view.findViewById(R.id.img_team_logo);
        this.TvPos = view.findViewById(R.id.tv_standing_pos);
        this.TvTeam = view.findViewById(R.id.tv_standing_team);
        this.TvPlayed = view.findViewById(R.id.tv_standing_played);
        this.TvWin = view.findViewById(R.id.tv_standing_win);
        this.TvDraw = view.findViewById(R.id.tv_standing_draw);
        this.TvLoss = view.findViewById(R.id.tv_standing_loss);
        this.TvGoals = view.findViewById(R.id.tv_standing_goals);
        this.TvPoint = view.findViewById(R.id.tv_standing_points);

        this.RowTeam = view.findViewById(R.id.layout_table_row);
    }
}