package com.appian.manutdvietnam.app.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.appian.manutdvietnam.Constant;
import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseActivity;
import com.appian.manutdvietnam.util.ViewHelper;

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
