package com.appian.manchesterunitednews.app.user;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.ToolbarViewListener;
import com.appian.manchesterunitednews.app.user.presenter.DetailUserProfilePresenter;
import com.appian.manchesterunitednews.app.user.view.DetailUserProfileView;
import com.appian.manchesterunitednews.data.account.AccountManager;
import com.appian.manchesterunitednews.data.account.UserAccount;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appian.manchesterunitednews.receiver.ReceiverHelper;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appnet.android.football.fbvn.data.AccountProfile;
import com.appnet.android.football.fbvn.data.Avatar;
import com.appnet.android.football.fbvn.data.User;
import com.appnet.android.football.fbvn.data.UserProfile;

public class UserFragment extends BaseStateFragment implements DetailUserProfileView {
    private ToolbarViewListener mToolBar;
    private OnBtnLogoutClickListener mBtnLogoutClickListener;
    private TextView mTvEmail;
    private TextView mTvAddress;
    private TextView mTvFullName;
    private ImageView mImgAvatar;

    private DetailUserProfilePresenter mPresenter;
    private BroadcastReceiver mUserProfileChangedReceiver;

    @Override
    protected int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailUserProfilePresenter(new UserInteractor());
        mPresenter.attachView(this);
        mUserProfileChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UserAccount user = AccountManager.getInstance().getAccount(getContext());
                if(user == null) {
                    return;
                }
                fillUserAccount(user);
            }
        };
        ReceiverHelper.registerUserProfileChanged(getContext(), mUserProfileChangedReceiver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvEmail = view.findViewById(R.id.tv_email);
        mTvAddress = view.findViewById(R.id.tv_address);
        mImgAvatar = view.findViewById(R.id.user_profile_picture);
        mTvFullName = view.findViewById(R.id.tv_full_name);
        updateTitle();
        UserAccount user = AccountManager.getInstance().getAccount(getContext());
        if (user != null) {
            View btnLogout = view.findViewById(R.id.btn_user_profile_logout);
            ImageLoader.displayImage(user.getAvatar(), mImgAvatar, R.drawable.profile_pic);
            mTvFullName.setText(user.getFullName());
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBtnLogoutClickListener != null) {
                        mBtnLogoutClickListener.onBtnLogoutClick();
                    }
                }
            });
            mPresenter.loadDetailUserProfile(user.getAuthorization());
        }
        View btnUpdate = view.findViewById(R.id.btn_update_info);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fillUserAccount(UserAccount user) {
        if(getView() == null) {
            return;
        }
        ImageLoader.displayImage(user.getAvatar(), mImgAvatar, R.drawable.profile_pic);
        mTvFullName.setText(user.getFullName());
        mTvEmail.setText(user.getEmail());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity instanceof ToolbarViewListener) {
            mToolBar = (ToolbarViewListener) activity;
        }
        if (activity instanceof OnBtnLogoutClickListener) {
            mBtnLogoutClickListener = (OnBtnLogoutClickListener) activity;
        }
    }

    private void updateTitle() {
        if (mToolBar != null) {
            mToolBar.changeToolbarTitle(getResources().getString(R.string.welcome_title));
        }
    }

    @Override
    public void showUserProfile(AccountProfile data) {
        if(getView() == null) {
            return;
        }
        User user = data.getUser();
        if(user != null) {
            mTvEmail.setText(user.getEmail());
            mTvFullName.setText(user.getFullName());
        }
        UserProfile userProfile = data.getUserProfile();
        if(userProfile != null) {
            if(!TextUtils.isEmpty(userProfile.getAddress())) {
                mTvAddress.setText(userProfile.getAddress());
            }
            Avatar avatar = userProfile.getAvatar();
            if(avatar != null && !TextUtils.isEmpty(avatar.getMediumThumb())) {
                ImageLoader.displayImage(avatar.getMediumThumb(), mImgAvatar, R.drawable.profile_pic);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        ReceiverHelper.unregisterReceiver(getContext(), mUserProfileChangedReceiver);
    }
}
