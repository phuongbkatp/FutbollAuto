package com.appian.manchesterunitednews.app.match.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;

public class StatisticGroupHeaderHolder extends RecyclerView.ViewHolder {
    TextView TvGroup;

    StatisticGroupHeaderHolder(View view) {
        super(view);
        this.TvGroup = view.findViewById(R.id.tv_group_name);
    }

}
