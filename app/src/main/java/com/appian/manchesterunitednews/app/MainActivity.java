package com.appian.manchesterunitednews.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.league.LeagueFragment;
import com.appian.manchesterunitednews.app.more.MoreFragment;
import com.appian.manchesterunitednews.app.setting.SettingActivity;
import com.appian.manchesterunitednews.app.team.TeamFragment;
import com.appian.manchesterunitednews.app.user.LogInActivity;
import com.appian.manchesterunitednews.app.user.UserFragment;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.Language;
import com.appian.manchesterunitednews.data.app.RemoteConfigData;
import com.appian.manchesterunitednews.data.interactor.AppConfigInteractor;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.service.app.AppHelper;
import com.appian.manchesterunitednews.util.BottomNavigationViewHelper;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.admob.InterstitialAdMob;
import com.github.fernandodev.easyratingdialog.library.EasyRatingDialog;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ToolbarViewListener {
    private static final String TAG_FRAGMENT_LEAGUE = "fragment_league";
    private static final String TAG_FRAGMENT_HOME = "fragment_home";
    private static final String TAG_FRAGMENT_TOPIC = "fragment_topic";
    private static final String TAG_FRAGMENT_SQUAD = "fragment_squad";
    private static final String TAG_FRAGMENT_PROFILE = "fragment_profile";
    private static final String TAG_FRAGMENT_SETTING = "fragment_setting";

    private boolean doubleBackToExitPressedOnce = false;

    private static final int RC_SETTING = 1;
    private static final int RC_LOGIN = 2;

    private TextView txtTitle;

    private InterstitialAdMob mInterstitialAdMob;

    private EasyRatingDialog easyRatingDialog;

    BottomNavigationView bottomNavigation;
    private boolean mIShowAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTitle = findViewById(R.id.txtTitle);

        easyRatingDialog = new EasyRatingDialog(this);

        bottomNavigation = findViewById(R.id.bottom_navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onBottomNavigationSelect(item);
                return true;
            }
        });

        switchFragment(TAG_FRAGMENT_HOME, null);

        AppConfig appConfig = AppConfig.getInstance();
        mInterstitialAdMob = new InterstitialAdMob(this, appConfig.getAdmobInterstitial(getApplicationContext()));
        Utils.addAdmobTestDevice(mInterstitialAdMob);
    }

    @Override
    protected void onStart() {
        super.onStart();
        easyRatingDialog.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        easyRatingDialog.showIfNeeded();
        mInterstitialAdMob.loadAd();
        mIShowAds = false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        //show ad after pressing back
        FragmentManager fm = getSupportFragmentManager();
        Fragment homeFragment = fm.findFragmentByTag(TAG_FRAGMENT_HOME);
        if (homeFragment != null && homeFragment.isVisible()) {
            if(!mIShowAds) {
                mInterstitialAdMob.show();
                mIShowAds = true;
            }
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.confirm_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            switchFragment(TAG_FRAGMENT_HOME);
        }
    }

    private void onBottomNavigationSelect (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.llNewsFeedMenu:
                switchFragment(TAG_FRAGMENT_HOME);
                break;
            case R.id.llSquadMenu:
                switchFragment(TAG_FRAGMENT_SQUAD);
                break;
            case R.id.llLeagueMenu:
                AppConfig appConfig = AppConfig.getInstance();
                List<RemoteConfigData.League> leagues = appConfig.getLeagues();
                int leagueId = 0;
                int seasonId = 0;
                String name = "";
                if(!leagues.isEmpty()) {
                    RemoteConfigData.League league = leagues.get(0);
                    leagueId = league.getId();
                    seasonId = league.getSeason();
                    name = league.getName();
                }
                Bundle args = new Bundle();
                args.putInt("league_id", leagueId);
                args.putInt("season_id", seasonId);
                args.putString("league_name", name);
                switchFragment(TAG_FRAGMENT_LEAGUE, args);
                break;
            case R.id.rlSetting:
                switchFragment(TAG_FRAGMENT_SETTING);
                break;
            default:
                switchFragment(TAG_FRAGMENT_HOME);
                break;

        }
    }

    private void switchFragment(String tag) {
        switchFragment(tag, null);
    }

    private void switchFragment(String tag, Bundle args) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (TAG_FRAGMENT_HOME.equals(tag)) {
            if (fragment == null) {
                fragment = new HomeFragment();
            }
        } else if (TAG_FRAGMENT_LEAGUE.equals(tag)) {
            if (fragment == null) {
                fragment = LeagueFragment.newInstance(args);
            } else if (fragment instanceof LeagueFragment) {
                ((LeagueFragment) fragment).updateLeagueSeason(args);
            }
        } else if (TAG_FRAGMENT_SQUAD.equals(tag)) {
            if (fragment == null) {
                fragment = TeamFragment.newInstance(getResources().getInteger(R.integer.team_id));
            }
        } else if (TAG_FRAGMENT_SETTING.equals(tag)) {
            if (fragment == null) {
                fragment = new MoreFragment();
            }
        }
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.main_layout_container, fragment, tag).commit();
        }
    }

    private void switchActivity(int requestCode) {
        Intent intent;
        switch (requestCode) {
            case RC_SETTING:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case RC_LOGIN:
                intent = new Intent(this, LogInActivity.class);
                startActivityForResult(intent, RC_LOGIN);
                break;
        }
    }

    @Override
    public void changeToolbarTitle(String title) {
        txtTitle.setText(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.onAttach(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}