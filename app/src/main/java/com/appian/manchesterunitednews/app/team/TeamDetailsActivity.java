package com.appian.manchesterunitednews.app.team;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appnet.android.football.sofa.helper.SofaImageHelper;

public class TeamDetailsActivity extends BaseActivity {

    private String mTeamName;
    private int mTeamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        getBundleExtras();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_team_detail, TeamFragment.newInstance(mTeamId)).commit();
        //
        View btnBack = findViewById(R.id.img_back_arrow);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        ImageView imgTeamLogo = findViewById(R.id.img_team_logo);
        TextView tvTeamName = findViewById(R.id.tv_team_name);
        tvTeamName.setText(this.mTeamName);
        ImageLoader.displayImage(SofaImageHelper.getSofaImgTeam(mTeamId), imgTeamLogo);
    }

    private void getBundleExtras() {
        /* Receive player information passed from the SquadFragment */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mTeamName = bundle.getString(Constant.EXTRA_KEY_TEAM_NAME);
            this.mTeamId = bundle.getInt(Constant.EXTRA_KEY_TEAM_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
