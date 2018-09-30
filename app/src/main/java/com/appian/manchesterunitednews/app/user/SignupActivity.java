package com.appian.manchesterunitednews.app.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.app.user.presenter.RegisterUserPresenter;
import com.appian.manchesterunitednews.app.user.view.RegisterUserView;
import com.appian.manchesterunitednews.data.account.AccountManager;
import com.appian.manchesterunitednews.data.account.UserAccount;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.AccessToken;
import com.appnet.android.football.fbvn.data.SignInAccount;
import com.appnet.android.football.fbvn.data.User;
import com.appnet.android.football.fbvn.data.UserProfile;

public class SignupActivity extends BaseActivity implements RegisterUserView{
    private static final String TAG = "SignupActivity";

    private EditText mEdtFullName;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private EditText mEdtRePass;
    private Button mBtnSignUp;

    private ProgressDialog mProgressDialog;

    private RegisterUserPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_withmail);

        mEdtFullName = findViewById(R.id.input_full_name);
        mEdtEmail = findViewById(R.id.input_email);
        mEdtPassword = findViewById(R.id.input_password);
        mEdtRePass = findViewById(R.id.intput_password_retype);
        mBtnSignUp = findViewById(R.id.btn_signup);

        mProgressDialog =  new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Creating Account...");
        mPresenter = new RegisterUserPresenter(new UserInteractor());
        mPresenter.attachView(this);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            registerFail();
            return;
        }

        mBtnSignUp.setEnabled(false);

        String name = mEdtFullName.getText().toString();
        String email = mEdtEmail.getText().toString();
        String password = mEdtPassword.getText().toString();
        String reEnterPassword = mEdtRePass.getText().toString();
        mProgressDialog.show();
        mPresenter.register(email, password, reEnterPassword, name);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mEdtFullName.getText().toString();
        String email = mEdtEmail.getText().toString();
        String password = mEdtPassword.getText().toString();
        String reEnterPassword = mEdtRePass.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mEdtFullName.setError("at least 3 characters");
            valid = false;
        } else {
            mEdtFullName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEdtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mEdtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mEdtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mEdtPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 ||
                !(reEnterPassword.equals(password))) {
            mEdtRePass.setError("Password Do not match");
            valid = false;
        } else {
            mEdtRePass.setError(null);
        }

        return valid;
    }

    @Override
    public void registerSuccess(SignInAccount signInAccount) {
        mBtnSignUp.setEnabled(true);
        if(signInAccount == null) {
            return;
        }
        User user = signInAccount.getUser();
        if(user == null || user.getId() == 0) {
            return;
        }
        AccessToken accessToken = signInAccount.getToken();
        if(accessToken == null || TextUtils.isEmpty(accessToken.getAccessToken())) {
            return;
        }
        String avatarUrl = "";
        UserProfile profile = signInAccount.getUserProfile();
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
        mProgressDialog.dismiss();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void registerFail() {
        mBtnSignUp.setEnabled(true);
        mProgressDialog.dismiss();
        Toast.makeText(this, R.string.register_user_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}