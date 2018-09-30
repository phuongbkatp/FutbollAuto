package com.appian.manchesterunitednews.app.fixture;

import com.appian.manchesterunitednews.app.widget.Section;
import com.appnet.android.football.sofa.data.Event;
import com.appnet.android.football.sofa.data.GameTournament;
import com.appnet.android.football.sofa.data.Round;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoundEvents implements Section<Event> {
    private Round round;
    private List<Event> events;

    public static List<RoundEvents> valueOf(List<GameTournament> gameTournaments) {
        LinkedHashMap<Round, List<Event>> mHashMapRoundEvents = new LinkedHashMap<>();
        for(GameTournament tournament : gameTournaments) {
            for(Event event : tournament.getEvents()) {
                Round round = event.getRoundInfo();
                if(!mHashMapRoundEvents.containsKey(round)) {
                    mHashMapRoundEvents.put(round, new ArrayList<Event>());
                }
                mHashMapRoundEvents.get(round).add(event);
            }
        }
        List<RoundEvents> data = new ArrayList<>();
        for(Map.Entry<Round, List<Event>> entrySet : mHashMapRoundEvents.entrySet()) {
            RoundEvents fixtures = new RoundEvents();
            fixtures.round = entrySet.getKey();
            fixtures.events = new ArrayList<>();
            fixtures.events.addAll(entrySet.getValue());
            data.add(fixtures);
        }
        return data;
    }

    Round getRound() {
        return round;
    }

    @Override
    public List<Event> getChildItem() {
        return events;
    }
}
