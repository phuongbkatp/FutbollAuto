package com.appian.manutdvietnam.app.more;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseActivity;
import com.appian.manutdvietnam.app.BaseFragment;
import com.appian.manutdvietnam.app.HomeFragment;
import com.appian.manutdvietnam.app.ToolbarViewListener;
import com.appian.manutdvietnam.app.adapter.NavigationListAdapter;
import com.appian.manutdvietnam.app.home.presenter.SeasonLeagueTeamPresenter;
import com.appian.manutdvietnam.app.home.view.SeasonLeagueTeamView;
import com.appian.manutdvietnam.app.league.LeagueFragment;
import com.appian.manutdvietnam.app.newstopic.NewsTopicFragment;
import com.appian.manutdvietnam.app.setting.SettingActivity;
import com.appian.manutdvietnam.app.team.TeamFragment;
import com.appian.manutdvietnam.app.user.LogInActivity;
import com.appian.manutdvietnam.app.user.OnBtnLogoutClickListener;
import com.appian.manutdvietnam.app.user.UserFragment;
import com.appian.manutdvietnam.data.account.AccountManager;
import com.appian.manutdvietnam.data.account.UserAccount;
import com.appian.manutdvietnam.data.app.AppConfig;
import com.appian.manutdvietnam.data.app.AppConfigManager;
import com.appian.manutdvietnam.data.app.Language;
import com.appian.manutdvietnam.network.ConnectivityEvent;
import com.appian.manutdvietnam.receiver.ReceiverHelper;
import com.appian.manutdvietnam.util.ImageLoader;
import com.appnet.android.ads.admob.InterstitialAdMob;
import com.appnet.android.football.fbvn.data.LeagueSeason;
import com.appnet.android.social.auth.OnLogoutListener;
import com.github.fernandodev.easyratingdialog.library.EasyRatingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by phuongbkatp on 9/19/2018.
 */

public class MoreFragment extends BaseFragment
        implements View.OnClickListener,
        OnLogoutListener, OnBtnLogoutClickListener,
        SeasonLeagueTeamView {
    private static final String TAG_FRAGMENT_LEAGUE = "fragment_league";

    private static final String TAG_FRAGMENT_PROFILE = "fragment_profile";

    private static final int RC_SETTING = 1;
    private static final int RC_LOGIN = 2;
    private ToolbarViewListener mToolbar;

    private NavigationListAdapter mNavigationAdapter;
    private List<LeagueSeason> mLeagueSesons;
    private TextView mTvProfileName;

    private ImageView mImgUserProfile;

    private BroadcastReceiver mUserProfileChangedReceiver;

    private SeasonLeagueTeamPresenter mSeasonLeagueTeamPresenter;

    private boolean mIShowAds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLeagueSesons = new ArrayList<>();

        //User profile
        mImgUserProfile = view.findViewById(R.id.profileImage);
        mTvProfileName = view.findViewById(R.id.tv_profile_name);

        mNavigationAdapter = new NavigationListAdapter(getContext(), mLeagueSesons);
        ListView navigationList = view.findViewById(R.id.navigation_list);
        navigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeagueSeason leagueSeason = mLeagueSesons.get(position);
                Bundle args = new Bundle();
                args.putInt("league_id", leagueSeason.getSofaLeagueId());
                args.putInt("season_id", leagueSeason.getSofaId());
                args.putString("league_name", leagueSeason.getLeagueName());
                switchFragment(TAG_FRAGMENT_LEAGUE, args);
            }
        });
        navigationList.setAdapter(mNavigationAdapter);

        View viewSetting = view.findViewById(R.id.rl_setting);
        View viewProfile = view.findViewById(R.id.profile_layout);

        viewSetting.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        mSeasonLeagueTeamPresenter = new SeasonLeagueTeamPresenter();
        mSeasonLeagueTeamPresenter.attachView(this);
        loadLeftMenu();

        handleLogin();
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(getContext());

        mUserProfileChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleLogin();
            }
        };
        View btnShare = view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareVia();
            }
        });
        ReceiverHelper.registerUserProfileChanged(getContext(), mUserProfileChangedReceiver);
        setTitle();
    }

    private void shareVia() {
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(getContext());
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appConfig.getShareContent());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_via)));
    }


    @Override
    public void onStart() {
        super.onStart();
        registerEventBus(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIShowAds = false;
    }

    private void loadLeftMenu() {
        AppConfig appConfig = AppConfigManager.getInstance().getAppConfig(getContext());
        mSeasonLeagueTeamPresenter.loadSeasonLeaguesByTeam(appConfig.getCurrentSeasonId(), appConfig.getTeamId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConnectivityEvent event) {
        if (event.isConnected()) {
            if (mLeagueSesons != null && mLeagueSesons.isEmpty()) {
                loadLeftMenu();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting:
                switchActivity(RC_SETTING);
                break;
            case R.id.profile_layout:
                UserAccount userAccount = AccountManager.getInstance().getAccount(getContext());
                if (userAccount != null) {
                    switchFragment(TAG_FRAGMENT_PROFILE);
                } else {
                    switchActivity(RC_LOGIN);
                }
                break;
            default:
                break;

        }
    }

    private void switchFragment(String tag) {
        switchFragment(tag, null);
    }

    private void switchFragment(String tag, Bundle args) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (TAG_FRAGMENT_LEAGUE.equals(tag)) {
            if (fragment == null) {
                fragment = LeagueFragment.newInstance(args);
            } else if (fragment instanceof LeagueFragment) {
                ((LeagueFragment) fragment).updateLeagueSeason(args);
            }
        } else if (TAG_FRAGMENT_PROFILE.equals(tag)) {
            if (fragment == null) {
                fragment = new UserFragment();
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
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case RC_LOGIN:
                intent = new Intent(getContext(), LogInActivity.class);
                startActivityForResult(intent, RC_LOGIN);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarViewListener) {
            mToolbar = (ToolbarViewListener) context;
        }
    }

    private void setTitle() {
        if (mToolbar != null) {
            mToolbar.changeToolbarTitle(getString(R.string.news_topic_menu));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ReceiverHelper.unregisterReceiver(getContext(), mUserProfileChangedReceiver);
        mSeasonLeagueTeamPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN && resultCode == RESULT_OK) {
            handleLogin();
        }
    }

    private void handleLogin() {
        UserAccount account = AccountManager.getInstance().getAccount(getContext());
        if (account == null) {
            fillNotLoginUser();
            return;
        }
        ImageLoader.displayImage(account.getAvatar(), mImgUserProfile, R.drawable.profile_pic);
        mTvProfileName.setText(account.getFullName());
    }

    private void handleLogout() {
        AccountManager.getInstance().clearAccount(getContext());
        fillNotLoginUser();
    }

    private void fillNotLoginUser() {
        mImgUserProfile.setImageResource(R.drawable.profile_pic);
        mTvProfileName.setText(getResources().getString(R.string.log_in_menu));
    }

    @Override
    public void onLogoutSuccess() {
        fillNotLoginUser();
    }

    @Override
    public void onBtnLogoutClick() {
        handleLogout();
    }

    @Override
    public void showSeasonLeagueTeam(List<LeagueSeason> data) {
        mLeagueSesons.clear();
        mLeagueSesons.addAll(data);
        mNavigationAdapter.notifyDataSetChanged();
    }


    @Override
    public void onStop() {
        super.onStop();
        registerEventBus(false);
    }

    private void registerEventBus(boolean isRegister) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (isRegister) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_navigation_drawer;
    }
}