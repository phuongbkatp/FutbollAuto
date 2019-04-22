package com.appian.manchesterunitednews.app.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.match.view.MatchVideoView;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;

public class MatchVideoFragment extends Fragment implements MatchVideoView {

    private static final String ARG_HOME = "home";
    private static final String ARG_AWAY = "away";
    private String mHomeTeam;
    private String mAwayTeam;



    public MatchVideoFragment() {
        // Required empty public constructor
    }

    public static MatchVideoFragment newInstance(String homeTeam, String awayTeam) {
        MatchVideoFragment fragment = new MatchVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOME, homeTeam);
        args.putString(ARG_AWAY, awayTeam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHomeTeam = getArguments().getString(ARG_HOME);
            mAwayTeam = getArguments().getString(ARG_AWAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_match_video, container, false);
    }

    @Override
    public void showMatchVideo(DetailNewsAuto data) {

    }
}
