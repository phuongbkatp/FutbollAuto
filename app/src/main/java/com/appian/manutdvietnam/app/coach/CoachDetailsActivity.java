package com.appian.manutdvietnam.app.coach;

import android.os.Bundle;
import android.view.MenuItem;

import com.appian.manutdvietnam.Constant;
import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseActivity;

public class CoachDetailsActivity extends BaseActivity {

    private String mCoachName;
    private int mCoachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail);
        getBundleExtras();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_coach_detail, CoachDetailFragment.newInstance(mCoachId, mCoachName)).commit();
    }

    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mCoachName = bundle.getString(Constant.EXTRA_KEY_MANAGER_NAME);
            this.mCoachId = bundle.getInt(Constant.EXTRA_KEY_SOFA_MANAGER_ID);
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
