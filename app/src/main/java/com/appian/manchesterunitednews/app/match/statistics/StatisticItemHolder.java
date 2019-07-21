package com.appian.manchesterunitednews.app.match.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;

public class StatisticItemHolder extends RecyclerView.ViewHolder {
    TextView TvHome;
    TextView TvName;
    TextView TvAway;

    StatisticItemHolder(View view) {
        super(view);
        this.TvHome = view.findViewById(R.id.tv_statistic_home);
        this.TvName = view.findViewById(R.id.tv_statistic_name);
        this.TvAway = view.findViewById(R.id.tv_statistic_away);
    }

}
