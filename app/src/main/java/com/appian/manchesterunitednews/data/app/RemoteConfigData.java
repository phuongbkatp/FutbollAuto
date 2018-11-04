package com.appian.manchesterunitednews.data.app;

import java.util.List;

public class RemoteConfigData {
    private List<League> leagues;

    public List<League> getLeagues() {
        return leagues;
    }

    public static class League {
        private int id;
        private int season;
        private String name;

        public int getId() {
            return id;
        }

        public int getSeason() {
            return season;
        }

        public String getName() {
            return name;
        }
    }
}
