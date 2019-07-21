package com.appian.manchesterunitednews.app.match.statistics;

import com.appian.manchesterunitednews.app.widget.Section;
import com.appnet.android.football.sofa.data.Statistic;

import java.util.ArrayList;
import java.util.List;

public class StatisticsGroupSection implements Section<Statistic.Item> {
    private final Statistic.Group data;

    public StatisticsGroupSection(Statistic.Group data) {
        this.data = data;
    }

    @Override
    public List<Statistic.Item> getChildItem() {
        if(data == null) {
            return null;
        }
        return data.getStatisticsItems();
    }

    public Statistic.Group getData() {
        return data;
    }

    public static List<StatisticsGroupSection> valueOf(List<Statistic.Group> groups) {
        List<StatisticsGroupSection> data = new ArrayList<>();
        if (groups == null) {
            return data;
        }
        for (Statistic.Group item : groups) {
            data.add(new StatisticsGroupSection(item));
        }
        return data;
    }

}
