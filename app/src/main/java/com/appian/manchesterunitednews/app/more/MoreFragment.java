package com.appian.manchesterunitednews.app.more;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.ToolbarViewListener;
import com.appian.manchesterunitednews.app.adapter.NavigationListAdapter;
import com.appian.manchesterunitednews.app.league.LeagueFragment;
import com.appian.manchesterunitednews.app.setting.SettingActivity;
import com.appian.manchesterunitednews.app.user.LogInActivity;
import com.appian.manchesterunitednews.app.user.UserFragment;
import com.appian.manchesterunitednews.data.app.AppConfig;
import com.appian.manchesterunitednews.data.app.RemoteConfigData;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by phuongbkatp on 9/19/2018.
 */

public class MoreFragment extends BaseFragment
        implements View.OnClickListener {
    private static final String TAG_FRAGMENT_LEAGUE = "fragment_league";

    private static final String TAG_FRAGMENT_PROFILE = "fragment_profile";

    private static final int RC_SETTING = 1;
    private static final int RC_LOGIN = 2;
    private ToolbarViewListener mToolbar;

    private NavigationListAdapter mNavigationAdapter;


    private boolean mIShowAds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<RemoteConfigData.League> currentLeagues = AppConfig.getInstance().getLeagues();
        mNavigationAdapter = new NavigationListAdapter(getContext(), AppConfig.getInstance().getLeagues());
        ListView navigationList = view.findViewById(R.id.navigation_list);
        navigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RemoteConfigData.League leagueSeason = currentLeagues.get(position);
                Bundle args = new Bundle();
                args.putInt("league_id", leagueSeason.getId());
                args.putInt("season_id", leagueSeason.getSeason());
                args.putString("league_name", leagueSeason.getName());
                switchFragment(TAG_FRAGMENT_LEAGUE, args);
            }
        });
        navigationList.setAdapter(mNavigationAdapter);

        View viewSetting = view.findViewById(R.id.rl_setting);

        viewSetting.setOnClickListener(this);

        View btnShare = view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareVia();
            }
        });

        View btnRate = view.findViewById(R.id.btn_review);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                }
                goToMarket.addFlags(flags);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });
        setTitle();
        TextView tvSplashClaim = view.findViewById(R.id.tv_splash_claim);
        tvSplashClaim.setText(getResources().getString(R.string.splash_claim, getResources().getString(R.string.team_fc)));
    }

    private void shareVia() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content, getString(R.string.team_fc)) + getContext().getPackageName());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_via)));
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIShowAds = false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting:
                switchActivity(RC_SETTING);
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
            mToolbar.changeToolbarTitle(getString(R.string.more_menu));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN && resultCode == RESULT_OK) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_navigation_drawer;
    }
}