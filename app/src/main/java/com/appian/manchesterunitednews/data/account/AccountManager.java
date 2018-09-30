package com.appian.manchesterunitednews.data.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.appian.manchesterunitednews.receiver.ReceiverHelper;
import com.appnet.android.football.fbvn.data.AccessToken;
import com.appnet.android.football.fbvn.data.SignInAccount;
import com.appnet.android.football.fbvn.data.User;
import com.appnet.android.football.fbvn.data.UserProfile;

public class AccountManager {
    private static final String PREF_ACCOUNT = "account";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_FIRST_NAME = "user_first_name";
    private static final String KEY_USER_LAST_NAME = "user_last_name";
    private static final String KEY_USER_FULL_NAME = "user_full_name";
    private static final String KEY_USER_ACCESS_TOKEN = "user_access_token";
    private static final String KEY_USER_AVATAR = "user_avatar";

    private static AccountManager sInstance;

    public static AccountManager getInstance() {
        if (sInstance == null) {
            sInstance = new AccountManager();
        }
        return sInstance;
    }

    public void saveAccount(Context context, UserAccount account) {
        if(account == null) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_USER_ID, account.getId());
        editor.putString(KEY_USER_EMAIL, account.getEmail());
        editor.putString(KEY_USER_FIRST_NAME, account.getFirstName());
        editor.putString(KEY_USER_LAST_NAME, account.getLastName());
        editor.putString(KEY_USER_FULL_NAME, account.getFullName());
        editor.putString(KEY_USER_ACCESS_TOKEN, account.getAccessToken());
        editor.putString(KEY_USER_AVATAR, account.getAvatar());
        editor.apply();
        notifyUserProfileChanged(context);
    }

    public void saveAccount(Context context, SignInAccount account) {
        if(account == null) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        User user = account.getUser();
        AccessToken accessToken = account.getToken();
        if(user == null || accessToken == null || TextUtils.isEmpty(accessToken.getAccessToken())) {
            return;
        }
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_USER_LAST_NAME, user.getLastName());
        editor.putString(KEY_USER_FULL_NAME, user.getFullName());
        editor.putString(KEY_USER_ACCESS_TOKEN, accessToken.getAccessToken());
        UserProfile profile = account.getUserProfile();
        if(profile != null && profile.getAvatar() != null) {
            editor.putString(KEY_USER_AVATAR, profile.getAvatar().getMediumThumb());
        }
        editor.apply();
        notifyUserProfileChanged(context);
    }

    public void updateAccount(Context context, User user) {
        if(user == null) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_USER_LAST_NAME, user.getLastName());
        editor.putString(KEY_USER_FULL_NAME, user.getFullName());
        editor.apply();
        notifyUserProfileChanged(context);
    }

    public void updateAvatar(Context context, String path) {
        if(TextUtils.isEmpty(path)) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_AVATAR, path);
        editor.apply();
        notifyUserProfileChanged(context);
    }

    public UserAccount getAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        int id = prefs.getInt(KEY_USER_ID, 0);
        if (id == 0) {
            return null;
        }
        String accessToken = prefs.getString(KEY_USER_ACCESS_TOKEN, null);
        if (TextUtils.isEmpty(accessToken)) {
            return null;
        }
        String email = prefs.getString(KEY_USER_EMAIL, "");
        String firstName = prefs.getString(KEY_USER_FIRST_NAME, "");
        String lastName = prefs.getString(KEY_USER_LAST_NAME, "");
        String fullName = prefs.getString(KEY_USER_FULL_NAME, "");
        String avatar = prefs.getString(KEY_USER_AVATAR, "");
        UserAccount.Builder builder = new UserAccount.Builder();
        builder.id(id);
        builder.accessToken(accessToken);
        builder.email(email);
        builder.firstName(firstName);
        builder.lastName(lastName);
        builder.avatar(avatar);
        builder.fullName(fullName);
        return builder.build();
    }

    public void clearAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        if (prefs != null) {
            prefs.edit().clear().apply();
        }
    }

    private static void notifyUserProfileChanged(Context context) {
        ReceiverHelper.notifyUserProfileChanged(context);
    }
}
