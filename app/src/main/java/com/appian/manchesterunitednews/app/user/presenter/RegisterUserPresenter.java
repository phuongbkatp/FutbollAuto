package com.appian.manchesterunitednews.app.user.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.user.view.RegisterUserView;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.SignInAccount;

public class RegisterUserPresenter extends BasePresenter<RegisterUserView> implements OnResponseListener<SignInAccount> {
    private final UserInteractor mInteractor;

    public RegisterUserPresenter(UserInteractor interactor) {
        mInteractor = interactor;
    }

    public void register(String email, String password, String confirmPassword, String fullName) {
        mInteractor.register(email, password, confirmPassword, fullName, this);
    }

    @Override
    public void onSuccess(SignInAccount data) {
        if(getView() == null) {
            return;
        }
        getView().registerSuccess(data);
    }

    @Override
    public void onFailure(String error) {
        if(getView() == null) {
            return;
        }
        getView().registerFail();
    }
}
