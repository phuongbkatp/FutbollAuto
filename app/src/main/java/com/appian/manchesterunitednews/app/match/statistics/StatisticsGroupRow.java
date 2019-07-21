package com.appian.manchesterunitednews.app.match.statistics;

import com.appnet.android.football.sofa.data.Statistic;

import java.util.List;

public class StatisticsGroupRow {
    private String name;
    private List<Statistic.Item> items;

    public String getName() {
        return name;
    }

    public List<Statistic.Item> getItems() {
        return items;
    }

}
