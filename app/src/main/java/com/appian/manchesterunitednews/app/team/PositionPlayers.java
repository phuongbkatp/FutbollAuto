package com.appian.manchesterunitednews.app.team;

import com.appian.manchesterunitednews.app.widget.Section;
import com.appnet.android.football.sofa.data.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionPlayers implements Section<Player> {
    private String position;
    private List<Player> players;

    @Override
    public List<Player> getChildItem() {
        return players;
    }

    public String getPosition() {
        return position;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static List<PositionPlayers> valueOf(List<Player> players) {
        HashMap<String, List<Player>> hashMap = new HashMap<>();
        for(Player player : players) {
            String position = player.getPosition();
            if(!hashMap.containsKey(position)) {
                hashMap.put(position, new ArrayList<Player>());
            }
            hashMap.get(position).add(player);
        }
        List<PositionPlayers> data = new ArrayList<>();
        for(Map.Entry<String, List<Player>> entrySet : hashMap.entrySet()) {
            PositionPlayers positionPlayers = new PositionPlayers();
            positionPlayers.players = new ArrayList<>();
            positionPlayers.players.addAll(entrySet.getValue());
            positionPlayers.position = entrySet.getKey();
            data.add(positionPlayers);
        }
        return data;
    }
}
