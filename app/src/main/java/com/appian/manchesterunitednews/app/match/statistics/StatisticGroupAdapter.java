package com.appian.manchesterunitednews.app.match.statistics;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.widget.SectionRecyclerViewAdapter;
import com.appnet.android.football.sofa.data.Statistic;

import java.util.List;

public class StatisticGroupAdapter extends SectionRecyclerViewAdapter<StatisticsGroupSection,
        Statistic.Item,
        StatisticGroupHeaderHolder,
        StatisticItemHolder> {
    private Context mContext;

    StatisticGroupAdapter(Context context, List<StatisticsGroupSection> data) {
        super(data);
        mContext = context;
    }


    @Override
    public void onBindSectionViewHolder(StatisticGroupHeaderHolder sectionViewHolder, int sectionPosition, StatisticsGroupSection section) {
        Statistic.Group data = section.getData();
        if(data != null) {
            sectionViewHolder.TvGroup.setText(data.getGroupName());
        }
    }

    @Override
    public void onBindChildViewHolder(StatisticItemHolder childViewHolder, int sectionPosition, int childPosition, Statistic.Item child) {
        childViewHolder.TvHome.setText(child.getHome());
        childViewHolder.TvName.setText(child.getName());
        childViewHolder.TvAway.setText(child.getAway());
        TextView tvCompare = child.getCompareCode() == 2 ? childViewHolder.TvAway : childViewHolder.TvHome;
        tvCompare.setTypeface(tvCompare.getTypeface(), Typeface.BOLD);
        tvCompare.setTextColor(mContext.getResources().getColor(R.color.win_color));
    }

    @Override
    public StatisticGroupHeaderHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_statistics_group, sectionViewGroup, false);
        return new StatisticGroupHeaderHolder(header);
    }

    @Override
    public StatisticItemHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_statistics, childViewGroup, false);
        return new StatisticItemHolder(row);
    }

}
