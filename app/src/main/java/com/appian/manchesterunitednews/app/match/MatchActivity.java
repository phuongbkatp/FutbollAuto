package com.appian.manchesterunitednews.app.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.util.EventHelper;
import com.appian.manchesterunitednews.util.ViewHelper;

public class MatchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        //
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        int sofaMatchId = 0;
        if(intent != null) {
            sofaMatchId = intent.getIntExtra(Constant.KEY_SOFA_MATCH_ID, 0);
        }
        fragmentManager.beginTransaction().replace(R.id.fragment_container, MatchFragment.newInstance(sofaMatchId)).commit();
        EventHelper.log(getApplicationContext(), EventHelper.EVENT_SCREEN_MATCH_DETAIL, null);
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            finish();
            ViewHelper.launchMainScreen(getApplicationContext());
        } else {
            super.onBackPressed();
        }
    }
}
