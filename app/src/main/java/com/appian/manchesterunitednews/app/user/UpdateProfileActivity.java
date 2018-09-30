package com.appian.manchesterunitednews.app.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;
import com.appian.manchesterunitednews.app.user.presenter.DetailUserProfilePresenter;
import com.appian.manchesterunitednews.app.user.presenter.UpdateAvatarPresenter;
import com.appian.manchesterunitednews.app.user.presenter.UpdateUserProfilePresenter;
import com.appian.manchesterunitednews.app.user.view.DetailUserProfileView;
import com.appian.manchesterunitednews.app.user.view.UpdateAvatarView;
import com.appian.manchesterunitednews.app.user.view.UpdateUserProfileView;
import com.appian.manchesterunitednews.data.account.AccountManager;
import com.appian.manchesterunitednews.data.account.UserAccount;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.PermissionHelper;
import com.appian.manchesterunitednews.util.Utils;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.fbvn.data.AccountProfile;
import com.appnet.android.football.fbvn.data.Avatar;
import com.appnet.android.football.fbvn.data.User;
import com.appnet.android.football.fbvn.data.UserProfile;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends BaseActivity implements
        DetailUserProfileView, UpdateUserProfileView, UpdateAvatarView, View.OnClickListener {

    private EditText mEdtSurname;
    private EditText mEdtLastname;
    private EditText mEdtAddress;
    private EditText mEdtEmailUser;
    private CircleImageView mImgAvatar;

    private DetailUserProfilePresenter mDetailPresenter;
    private UpdateUserProfilePresenter mUpdatePresenter;
    private UpdateAvatarPresenter mAvatarPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        initView();

        UserInteractor userInteractor = new UserInteractor();
        mUpdatePresenter = new UpdateUserProfilePresenter(userInteractor);
        mDetailPresenter = new DetailUserProfilePresenter(userInteractor);
        mAvatarPresenter = new UpdateAvatarPresenter(userInteractor);
        mDetailPresenter.attachView(this);
        mUpdatePresenter.attachView(this);
        mAvatarPresenter.attachView(this);
        final UserAccount user = AccountManager.getInstance().getAccount(this);
        if (user != null) {
            mDetailPresenter.loadDetailUserProfile(user.getAuthorization());
        }
        mImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionHelper.checkStoragePermission(UpdateProfileActivity.this)) {
                    loadChooseImage();
                }
            }
        });
    }

    private void initView() {
        mEdtSurname = findViewById(R.id.edit_surname);
        mEdtLastname = findViewById(R.id.edit_lastname);
        mEdtAddress = findViewById(R.id.edit_address_user);
        mEdtEmailUser = findViewById(R.id.edit_email_user);
        mImgAvatar = findViewById(R.id.img_avatar_profile);
        View btnUpdate = findViewById(R.id.btn_update_infor);
        btnUpdate.setOnClickListener(this);
        View btnClearEmail = findViewById(R.id.clear_email);
        btnClearEmail.setOnClickListener(this);
        View btnClearLastName = findViewById(R.id.clear_lastname);
        btnClearLastName.setOnClickListener(this);
        View btnClearAddress = findViewById(R.id.clear_address);
        btnClearAddress.setOnClickListener(this);
        View btnClearSurname = findViewById(R.id.clear_surname);
        btnClearSurname.setOnClickListener(this);
        View btnBack = findViewById(R.id.btn_back_screen_profile);
        btnBack.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void focusField(EditText edt) {
        edt.requestFocus();
        Toast.makeText(this, R.string.edt_empty_message, Toast.LENGTH_SHORT).show();
    }

    private void loadChooseImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uploadImage(data.getData());
        }
    }

    private void uploadImage(final Uri imageUri) {
        UserAccount user = AccountManager.getInstance().getAccount(this);
        if (user == null) {
            return;
        }
        File file = new File(Utils.getPath(this, imageUri));
        mAvatarPresenter.updateAvatar(user.getAuthorization(), file);
    }

    @Override
    public void updateSuccess(AccountProfile data) {
        Toast.makeText(this, R.string.update_user_profile_success, Toast.LENGTH_SHORT).show();
        if (data.getUser() != null) {
            AccountManager.getInstance().updateAccount(this, data.getUser());
        }
        finish();
    }

    @Override
    public void updateFail() {
        Toast.makeText(this, R.string.update_user_profile_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserProfile(AccountProfile data) {
        User user = data.getUser();
        if (user != null) {
            mEdtSurname.setText(user.getFirstName());
            mEdtLastname.setText(user.getLastName());
            mEdtEmailUser.setText(user.getEmail());
            ImageLoader.displayImage(ViewHelper.getUserAvatar(user.getId()), mImgAvatar, R.drawable.user);
        }
        UserProfile userProfile = data.getUserProfile();
        if (userProfile != null) {
            mEdtAddress.setText(userProfile.getAddress());
            Avatar avatar = userProfile.getAvatar();
            if (avatar != null && !TextUtils.isEmpty(avatar.getMediumThumb())) {
                ImageLoader.displayImage(avatar.getMediumThumb(), mImgAvatar, R.drawable.profile_pic);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.detachView();
        mUpdatePresenter.detachView();
        mAvatarPresenter.detachView();
    }

    @Override
    public void updateAvatarSuccess(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ImageLoader.displayImage(path, mImgAvatar, R.drawable.user);
        Toast.makeText(this, R.string.update_user_avatar_success, Toast.LENGTH_SHORT).show();
        AccountManager.getInstance().updateAvatar(this, path);
    }

    @Override
    public void updateAvatarFail() {
        Toast.makeText(this, R.string.update_user_avatar_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionHelper.REQUEST_PERMISSIONS_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadChooseImage();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_infor:
                update();
                break;
            case R.id.clear_email:
                clearEmail();
                break;
            case R.id.clear_lastname:
                clearLastName();
                break;
            case R.id.clear_address:
                clearAddress();
                break;
            case R.id.clear_surname:
                clearSurname();
                break;
            case R.id.btn_back_screen_profile:
                finish();
                break;
        }
    }

    private void update() {
        UserAccount user = AccountManager.getInstance().getAccount(getApplicationContext());
        if (user == null || TextUtils.isEmpty(user.getAccessToken())) {
            ViewHelper.requestLogin(getApplicationContext());
            return;
        }
        String firstName = mEdtSurname.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            focusField(mEdtSurname);
            return;
        }
        String lastName = mEdtLastname.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            focusField(mEdtLastname);
            return;
        }
        String email = mEdtEmailUser.getText().toString();
        if (TextUtils.isEmpty(email)) {
            focusField(mEdtAddress);
            return;
        }
        String address = mEdtAddress.getText().toString();
        mUpdatePresenter.updateUserProfile(user.getAuthorization(), firstName, lastName, email, true, address);
    }

    private void clearEmail() {
        mEdtEmailUser.setText("");
    }

    private void clearLastName() {
        mEdtLastname.setText("");
    }

    private void clearAddress() {
        mEdtAddress.setText("");
    }

    void clearSurname() {
        mEdtSurname.setText("");
    }
}
