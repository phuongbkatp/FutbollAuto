package com.appian.manchesterunitednews.app.match;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appnet.android.football.sofa.data.Incident;

import java.util.ArrayList;
import java.util.List;


class MatchDetailEventRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOME_GOAL = 0;
    private static final int TYPE_AWAY_GOAL = 1;
    private static final int TYPE_HOME_SUBSTITUTION = 2;
    private static final int TYPE_AWAY_SUBSTITUTION = 3;
    private static final int TYPE_HOME_CARD = 4;
    private static final int TYPE_AWAY_CARD = 5;
    private static final int TYPE_PERIOD = 6;
    private static final int TYPE_INJURY_TIME = 7;
    private static final int TYPE_HOME_PENATY = 8;
    private static final int TYPE_AWAY_PENATY = 9;
    private List<Incident> data = new ArrayList<>();
    private Context mContext;

    MatchDetailEventRecycleViewAdapter(Context context) {
        mContext = context;
        this.data = new ArrayList<>();
    }

    void reloadData(List<Incident> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case TYPE_HOME_PENATY:
            case TYPE_HOME_GOAL:
                view = inflater.inflate(R.layout.item_match_detail_event_home_team_gold, parent, false);
                return new EventGoalHolder(view);
            case TYPE_AWAY_PENATY:
            case TYPE_AWAY_GOAL:
                view = inflater.inflate(R.layout.item_match_detail_event_away_team_gold, parent, false);
                return new EventGoalHolder(view);
            case TYPE_HOME_SUBSTITUTION:
                view = inflater.inflate(R.layout.item_match_detail_event_home_team_substitution, parent, false);
                return new EventSubstitutionHolder(view);
            case TYPE_AWAY_SUBSTITUTION:
                view = inflater.inflate(R.layout.item_match_detail_event_away_team_substitution, parent, false);
                return new EventSubstitutionHolder(view);
            case TYPE_HOME_CARD:
                view = inflater.inflate(R.layout.item_match_detail_event_home_team_card, parent, false);
                return new EventCardHolder(view);
            case TYPE_AWAY_CARD:
                view = inflater.inflate(R.layout.item_match_detail_event_away_team_card, parent, false);
                return new EventCardHolder(view);
            case TYPE_PERIOD:
                view = inflater.inflate(R.layout.item_match_detail_event_period, parent, false);
                return new EventPeriodHolder(view);
            case TYPE_INJURY_TIME:
                view = inflater.inflate(R.layout.item_match_detail_event_injury_time, parent, false);
                return new EventInjuryTimedHolder(view);

        }
        view = inflater.inflate(R.layout.item_match_detail_event_period, parent, false);
        return new EventPeriodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Incident item = data.get(position);
        if (item != null) {
            initializeViews(item, holder, position);
        }
    }

    private void initializeViews(final Incident event, final RecyclerView.ViewHolder viewHolder, final int position) {
        Resources res = mContext.getResources();

        switch (getItemViewType(position)) {
            case TYPE_HOME_GOAL:
            case TYPE_AWAY_GOAL:
            case TYPE_HOME_PENATY:
            case TYPE_AWAY_PENATY:
                EventGoalHolder holder1 = (EventGoalHolder) viewHolder;
                if(event.getPlayer() != null) {
                    holder1.TvPlayerScore.setText(event.getPlayer().getShortName());
                }
                if(event.getAssist1() == null) {
                    holder1.TvPlayerAssist.setText("");
                    holder1.TvPlayerAssist.setVisibility(View.GONE);
                } else {
                    holder1.TvPlayerAssist.setVisibility(View.VISIBLE);
                    holder1.TvPlayerAssist.setText(event.getAssist1().getShortName());
                }
                if(event.getAddedTime() == 0 && event.getTime() > 0) {
                    holder1.TvMinute.setText(res.getString(R.string.match_detail_event_minute, event.getTime()));
                } else if(event.getAddedTime() > 0 && event.getTime() > 0){
                    holder1.TvMinute.setText(res.getString(R.string.match_detail_event_add_minute, event.getTime(), event.getAddedTime()));
                }
                holder1.TvHomeGoal.setText(String.valueOf(event.getHomeScore()));
                holder1.TvAwayGoal.setText(String.valueOf(event.getAwayScore()));
                if(Constant.INCIDENT_GOAL_FROM_PENALTY.equalsIgnoreCase(event.getFrom())) {
                    holder1.ImgGoal.setImageResource(R.drawable.ico_football_penalty);
                } else if(Constant.INCIDENT_GOAL_FROM_OWNGOAL.equalsIgnoreCase(event.getFrom())) {
                    holder1.ImgGoal.setImageResource(R.drawable.ico_football_own_goal);
                } else if(Constant.INCIDENT_TYPE_PENATY.equalsIgnoreCase(event.getIncidentType()) && event.getMissed() == 1) {
                    holder1.ImgGoal.setImageResource(R.drawable.ico_missed_penalty);
                    holder1.TvHomeGoal.setText("");
                    holder1.TvAwayGoal.setText("");
                } else {
                    holder1.ImgGoal.setImageResource(R.drawable.ic_goal);
                }
                break;

            case TYPE_HOME_SUBSTITUTION:
            case TYPE_AWAY_SUBSTITUTION:
                EventSubstitutionHolder holder2 = (EventSubstitutionHolder) viewHolder;
                if(event.getPlayerIn() != null) {
                    holder2.TvPlayerIn.setText(event.getPlayerIn().getShortName());
                }
                if(event.getPlayerOut() != null) {
                    holder2.TvPlayerOut.setText(event.getPlayerOut().getShortName());
                }
                if(event.getAddedTime() == 0) {
                    holder2.TvMinute.setText(res.getString(R.string.match_detail_event_minute, event.getTime()));
                } else {
                    holder2.TvMinute.setText(res.getString(R.string.match_detail_event_add_minute, event.getTime(), event.getAddedTime()));
                }
                break;
            case TYPE_HOME_CARD:
            case TYPE_AWAY_CARD:
                EventCardHolder holder3 = (EventCardHolder) viewHolder;
                if(event.getPlayer() != null) {
                    holder3.TvPlayer.setText(event.getPlayer().getShortName());
                }
                if(Constant.INCIDENT_CARD_YELLOW.equalsIgnoreCase(event.getType())) {
                    holder3.ImgCard.setImageDrawable(res.getDrawable(R.drawable.yellow_card));
                } else if(Constant.INCIDENT_CARD_RED.equalsIgnoreCase(event.getType())) {
                    holder3.ImgCard.setImageDrawable(res.getDrawable(R.drawable.red_card));
                } else {
                    holder3.ImgCard.setImageResource(R.drawable.yellow_red_card);
                }
                if(event.getAddedTime() == 0) {
                    holder3.TvMinute.setText(res.getString(R.string.match_detail_event_minute, event.getTime()));
                } else {
                    holder3.TvMinute.setText(res.getString(R.string.match_detail_event_add_minute, event.getTime(), event.getAddedTime()));
                }
                break;
            case TYPE_PERIOD:
                EventPeriodHolder holder4 = (EventPeriodHolder) viewHolder;
                holder4.TvText.setText(event.getText());
                break;
            case TYPE_INJURY_TIME:
                EventInjuryTimedHolder holder5 = (EventInjuryTimedHolder) viewHolder;
                if(event.getLength() > 0) {
                    holder5.TvText.setText(res.getString(R.string.match_detail_event_injury_time, event.getLength()));
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Incident item = data.get(position);
        if (item.isHome() && Constant.INCIDENT_TYPE_GOAL.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_HOME_GOAL;
        }
        if (!item.isHome() && Constant.INCIDENT_TYPE_GOAL.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_AWAY_GOAL;
        }
        if (item.isHome() && Constant.INCIDENT_TYPE_SUBSTITUTION.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_HOME_SUBSTITUTION;
        }
        if (!item.isHome() && Constant.INCIDENT_TYPE_SUBSTITUTION.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_AWAY_SUBSTITUTION;
        }
        if (item.isHome() && Constant.INCIDENT_TYPE_CARD.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_HOME_CARD;
        }
        if (!item.isHome() && Constant.INCIDENT_TYPE_CARD.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_AWAY_CARD;
        }
        if (Constant.INCIDENT_TYPE_PERIOD.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_PERIOD;
        }
        if (Constant.INCIDENT_TYPE_INJURY_TIME.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_INJURY_TIME;
        }
        if (item.getScoringTeam() == 1 && Constant.INCIDENT_TYPE_PENATY.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_HOME_PENATY;
        }
        if (item.getScoringTeam() == 2 && Constant.INCIDENT_TYPE_PENATY.equalsIgnoreCase(item.getIncidentType())) {
            return TYPE_AWAY_PENATY;
        }
        return TYPE_INJURY_TIME;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class EventGoalHolder extends RecyclerView.ViewHolder {
        TextView TvMinute;
        TextView TvPlayerScore;
        TextView TvPlayerAssist;
        TextView TvHomeGoal;
        TextView TvAwayGoal;
        ImageView ImgGoal;

        EventGoalHolder(View view) {
            super(view);
            TvMinute = view.findViewById(R.id.tv_match_detail_event_time);
            TvPlayerScore = view.findViewById(R.id.tv_match_detail_event_goal_score);
            TvPlayerAssist = view.findViewById(R.id.tv_match_detail_event_goal_assist);
            TvHomeGoal = view.findViewById(R.id.tv_match_detail_event_gold_home);
            TvAwayGoal =  view.findViewById(R.id.tv_match_detail_event_gold_away);
            ImgGoal = view.findViewById(R.id.img_match_detail_event_goal);
        }
    }

    private static class EventSubstitutionHolder extends RecyclerView.ViewHolder {
        TextView TvMinute;
        TextView TvPlayerIn;
        TextView TvPlayerOut;

        EventSubstitutionHolder(View view) {
            super(view);
            TvMinute = view.findViewById(R.id.tv_match_detail_event_time);
            TvPlayerIn = view.findViewById(R.id.tv_match_detail_event_substitution_in);
            TvPlayerOut = view.findViewById(R.id.tv_match_detail_event_substitution_out);

        }
    }

    private static class EventCardHolder extends RecyclerView.ViewHolder {
        TextView TvMinute;
        TextView TvPlayer;
        ImageView ImgCard;

        EventCardHolder(View view) {
            super(view);
            TvMinute = view.findViewById(R.id.tv_match_detail_event_time);
            TvPlayer = view.findViewById(R.id.tv_match_detail_event_card_player);
            ImgCard = view.findViewById(R.id.img_match_detail_event_card);

        }
    }

    private static class EventPeriodHolder extends RecyclerView.ViewHolder {
        TextView TvText;

        EventPeriodHolder(View view) {
            super(view);
            TvText = view.findViewById(R.id.tv_match_detail_event_text);
        }
    }

    private static class EventInjuryTimedHolder extends RecyclerView.ViewHolder {
        TextView TvText;

        EventInjuryTimedHolder(View view) {
            super(view);
            TvText = view.findViewById(R.id.tv_match_detail_event_text);
        }
    }

}