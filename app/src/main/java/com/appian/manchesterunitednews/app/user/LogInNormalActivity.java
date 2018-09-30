package com.appian.manchesterunitednews.app.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.app.user.presenter.LoginUserPresenter;
import com.appian.manchesterunitednews.app.user.view.LoginUserView;
import com.appian.manchesterunitednews.data.account.AccountManager;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.SignInAccount;

public class LogInNormalActivity extends BaseActivity implements LoginUserView{
    private LoginUserPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_withmail);
        final EditText edtEmail = findViewById(R.id.input_email);
        final EditText edtPassword = findViewById(R.id.input_pass);
        final Button btnSignUp = findViewById(R.id.btn_signup);

        mPresenter = new LoginUserPresenter(new UserInteractor());
        mPresenter.attachView(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    focusField(edtEmail);
                    return;
                }
                String password = edtPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    focusField(edtPassword);
                    return;
                }
                mPresenter.login(email, password);
            }
        });

    }


    @Override
    public void loginSuccess(SignInAccount data) {
        AccountManager.getInstance().saveAccount(this, data);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void loginFail() {
        Toast.makeText(this, R.string.login_user_profile_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void focusField(EditText edt) {
        edt.requestFocus();
        Toast.makeText(this, R.string.edt_empty_message, Toast.LENGTH_SHORT).show();
    }
}