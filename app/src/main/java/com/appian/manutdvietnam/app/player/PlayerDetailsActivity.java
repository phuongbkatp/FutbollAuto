package com.appian.manutdvietnam.app.player;

import android.os.Bundle;
import android.view.MenuItem;

import com.appian.manutdvietnam.Constant;
import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseActivity;

public class PlayerDetailsActivity extends BaseActivity {

    private String mPlayerName;
    private int mPlayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);
        getBundleExtras();
        getSupportFragmentManager().beginTransaction().replace(R.id.squad_fragment_player_detail, PlayerDetailFragment.newInstance(mPlayerId, mPlayerName)).commit();
    }


    private void getBundleExtras() {
        /* Receive player information passed from the SquadFragment */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mPlayerName = bundle.getString(Constant.EXTRA_KEY_PLAYER_NAME);
            this.mPlayerId = bundle.getInt(Constant.EXTRA_KEY_SOFA_PLAYER_ID);
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
