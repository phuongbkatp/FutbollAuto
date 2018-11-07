package com.appian.manchesterunitednews.app.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.app.user.presenter.LoginUserPresenter;
import com.appian.manchesterunitednews.app.user.view.LoginUserView;
import com.appian.manchesterunitednews.data.account.AccountManager;
import com.appian.manchesterunitednews.data.account.UserAccount;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.AccessToken;
import com.appnet.android.football.fbvn.data.SignInAccount;
import com.appnet.android.football.fbvn.data.User;
import com.appnet.android.football.fbvn.data.UserProfile;
import com.appnet.android.social.auth.AuthManager;
import com.appnet.android.social.auth.OnLoginListener;
import com.appnet.android.social.auth.SocialAccount;

public class LogInActivity extends BaseActivity implements View.OnClickListener, OnLoginListener, LoginUserView {
    private AuthManager mAuthManager;
    private static final int RC_REGISTER_ACCOUNT = 1;
    private ProgressDialog mConnectionProgressDialog;

    private LoginUserPresenter mLoginUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuthManager = new AuthManager();
        mLoginUserPresenter = new LoginUserPresenter(new UserInteractor());
        mLoginUserPresenter.attachView(this);
        initView();
    }

    private void initView() {
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage(getText(R.string.in_progress_sign_in));
        View btnLoginGoogle = findViewById(R.id.btn_google_log_in);
        View btnLoginFacebook = findViewById(R.id.btn_facebook_log_in);
        View btnLoginNormal = findViewById(R.id.btn_log_in_normal);
        View btnBackArrow = findViewById(R.id.img_back_arrow);
        View btnRegister = findViewById(R.id.tv_log_in_sign_up);
        btnLoginGoogle.setOnClickListener(this);
        btnLoginFacebook.setOnClickListener(this);
        btnLoginNormal.setOnClickListener(this);
        btnBackArrow.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_google_log_in:
//                showLoading(true);
//                mAuthManager.signInGoogle(this, getResources().getString(R.string.google_sign_in_server_client_id), this);
//                break;
//            case R.id.btn_facebook_log_in:
//                showLoading(true);
//                mAuthManager.signInFacebook(this,this);
//                break;
//            case R.id.btn_log_in_normal:
//                Intent intent = new Intent(this, LogInNormalActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_log_in_sign_up:
//                Intent intent2 = new Intent(this, SignupActivity.class);
//                startActivityForResult(intent2, RC_REGISTER_ACCOUNT);
//                break;
//            case R.id.img_back_arrow:
//                finish();
//                break;
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            showLoading(false);
            return;
        }
        if(requestCode == RC_REGISTER_ACCOUNT) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            mAuthManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLoginSuccess(@NonNull SocialAccount account) {
        String email = (TextUtils.isEmpty(account.getEmail())) ? account.getId()+"@footballlivenews.com" : account.getEmail();
        mLoginUserPresenter.loginSocial(email, account.getId(), account.getProvider(), account.getDisplayName(), account.getPhoto());
    }

    @Override
    public void onLoginError(int errorCode, String errorMessage) {
        Toast.makeText(this, getResources().getString(R.string.error_login_message, errorMessage), Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess(SignInAccount data) {
        showLoading(false);
        if(data == null) {
            return;
        }
        User user = data.getUser();
        if(user == null || user.getId() == 0) {
            return;
        }
        AccessToken accessToken = data.getToken();
        if(accessToken == null || TextUtils.isEmpty(accessToken.getAccessToken())) {
            return;
        }
        String avatarUrl = "";
        UserProfile profile = data.getUserProfile();
        if(profile != null && profile.getAvatar() != null) {
            avatarUrl = profile.getAvatar().getMediumThumb();
        }
        UserAccount.Builder builder = new UserAccount.Builder();
        builder.id(user.getId());
        builder.email(user.getEmail());
        builder.firstName(user.getFirstName());
        builder.lastName(user.getLastName());
        builder.fullName(user.getFullName());
        builder.accessToken(accessToken.getAccessToken());
        builder.avatar(avatarUrl);
        UserAccount userAccount = builder.build();
        AccountManager.getInstance().saveAccount(getApplicationContext(), userAccount);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void loginFail() {
        showLoading(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginUserPresenter.detachView();
    }

    private void showLoading(boolean visble) {
        if(visble && !mConnectionProgressDialog.isShowing()) {
            mConnectionProgressDialog.show();
        }
        if(!visble && mConnectionProgressDialog.isShowing()) {
            mConnectionProgressDialog.dismiss();
        }
    }
}
