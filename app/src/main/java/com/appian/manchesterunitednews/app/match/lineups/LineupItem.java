package com.appian.manchesterunitednews.app.match.lineups;

import com.appnet.android.football.sofa.data.LineupsData;
import com.appnet.android.football.sofa.data.LineupsPlayer;

import java.util.ArrayList;
import java.util.List;

class LineupItem {
    private PlayerItem homePlayer;
    private PlayerItem awayPlayer;

    PlayerItem getHomePlayer() {
        return homePlayer;
    }

    PlayerItem getAwayPlayer() {
        return awayPlayer;
    }

    private static PlayerItem valueOf(LineupsPlayer player) {
        PlayerItem item = new PlayerItem();
        item.setName(player.getPlayer().getShortName());
        item.setNumber(player.getShirtNumber());
        item.setId(player.getPlayer().getId());
        return item;
    }

    static List<LineupItem> getLineup(LineupsData data) {
        List<LineupsPlayer> homeTeam = data.getHome();
        List<LineupsPlayer> awayTeam = data.getAway();
        int size = homeTeam.size() <= awayTeam.size() ? homeTeam.size() : awayTeam.size();
        List<LineupItem> list = new ArrayList<>();
        if(size > 11) {
            size = 11;
        }
        for(int i = 0; i < size; i++) {
            LineupItem item = new LineupItem();
            item.homePlayer = valueOf(homeTeam.get(i));
            item.awayPlayer = valueOf(awayTeam.get(i));
            list.add(item);
        }
        return list;
    }

    static List<LineupItem> getSubstitution(LineupsData data) {
        List<LineupsPlayer> homeTeam = data.getHome();
        List<LineupsPlayer> awayTeam = data.getAway();
        int size = homeTeam.size() <= awayTeam.size() ? homeTeam.size() : awayTeam.size();
        List<LineupItem> list = new ArrayList<>();
        if(size < 11) {
            return list;
        }
        for(int i = 11; i < size; i++) {
            LineupItem item = new LineupItem();
            item.homePlayer = valueOf(homeTeam.get(i));
            item.awayPlayer = valueOf(awayTeam.get(i));
            list.add(item);
        }
        return list;
    }
}
