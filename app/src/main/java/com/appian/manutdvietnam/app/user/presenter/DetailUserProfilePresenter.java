package com.appian.manutdvietnam.app.user.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.user.view.DetailUserProfileView;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appian.manutdvietnam.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.AccountProfile;

public class DetailUserProfilePresenter extends BasePresenter<DetailUserProfileView> implements OnResponseListener<AccountProfile> {
    private final UserInteractor mInteractor;

    public DetailUserProfilePresenter(UserInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadDetailUserProfile(String authorization) {
        mInteractor.loadUserProfile(authorization, this);
    }

    @Override
    public void onSuccess(AccountProfile data) {
        if(getView() == null) {
            return;
        }
        getView().showUserProfile(data);
    }

    @Override
    public void onFailure(String error) {

    }
}
