package com.appian.manutdvietnam.app.user.presenter;

import com.appian.manutdvietnam.app.BasePresenter;
import com.appian.manutdvietnam.app.user.view.LoginUserView;
import com.appian.manutdvietnam.data.interactor.OnResponseListener;
import com.appian.manutdvietnam.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.SignInAccount;

public class LoginUserPresenter extends BasePresenter<LoginUserView> implements OnResponseListener<SignInAccount> {
    private final UserInteractor mInteractor;

    public LoginUserPresenter(UserInteractor interactor) {
        mInteractor = interactor;
    }

    public void login(String email, String password) {
        mInteractor.login(email, password, this);
    }

    public void loginSocial(String email, String id, String provider, String displayName, String photoSrc) {
        mInteractor.loginSocial(email, id, provider, displayName, photoSrc,this);
    }

    @Override
    public void onSuccess(SignInAccount data) {
        if(getView() == null) {
            return;
        }
        getView().loginSuccess(data);
    }

    @Override
    public void onFailure(String error) {
        if(getView() == null) {
            return;
        }
        getView().loginFail();
    }
}
