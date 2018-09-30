package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.fbvn.data.AccountProfile;
import com.appnet.android.football.fbvn.data.SignInAccount;
import com.appnet.android.football.fbvn.data.SignInAccountData;
import com.appnet.android.football.fbvn.data.UserProfileData;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInteractor {
    public void loadUserProfile(String authorization, final OnResponseListener<AccountProfile> listener) {
        if (listener == null) {
            return;
        }
        Call<UserProfileData> call = RestfulService.getInstance().loadUserProfile(authorization);
        call.enqueue(new Callback<UserProfileData>() {
            @Override
            public void onResponse(Call<UserProfileData> call, Response<UserProfileData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<UserProfileData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void updateUserProfile(String authorization, String firstName, String lastName, String email, boolean male, String address, final OnResponseListener<AccountProfile> listener) {
        if (listener == null) {
            return;
        }
        Call<UserProfileData> call = RestfulService.getInstance().updateUserProfile(authorization, firstName, lastName, email, male, address);
        call.enqueue(new Callback<UserProfileData>() {
            @Override
            public void onResponse(Call<UserProfileData> call, Response<UserProfileData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<UserProfileData> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void register(String email, String password, String confirmPassword, String fullName, final OnResponseListener<SignInAccount> listener) {
        if (listener == null) {
            return;
        }
        Call<SignInAccountData> call = RestfulService.getInstance().register(email, password, confirmPassword, fullName);
        call.enqueue(new Callback<SignInAccountData>() {
            @Override
            public void onResponse(Call<SignInAccountData> call, Response<SignInAccountData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<SignInAccountData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void login(String email, String password, final OnResponseListener<SignInAccount> listener) {
        if (listener == null) {
            return;
        }
        Call<SignInAccountData> call = RestfulService.getInstance().login(email, password);
        call.enqueue(new Callback<SignInAccountData>() {
            @Override
            public void onResponse(Call<SignInAccountData> call, Response<SignInAccountData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<SignInAccountData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void uploadAvatar(String authorization, File file, final OnResponseListener<AccountProfile> listener) {
        if (listener == null) {
            return;
        }
        Call<UserProfileData> call = RestfulService.getInstance().uploadAvatar(authorization, file);
        call.enqueue(new Callback<UserProfileData>() {
            @Override
            public void onResponse(Call<UserProfileData> call, Response<UserProfileData> response) {
                if (response.body() == null) {
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<UserProfileData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void loginSocial(String email, String id, String provider, String displayName, String photoSrc, final OnResponseListener<SignInAccount> listener) {
        if (listener == null) {
            return;
        }
        Call<SignInAccountData> call = RestfulService.getInstance().loginSocial(email, id, provider, displayName, photoSrc);
        call.enqueue(new Callback<SignInAccountData>() {
            @Override
            public void onResponse(Call<SignInAccountData> call, Response<SignInAccountData> response) {
                SignInAccountData data = response.body();
                if (data == null) {
                    listener.onSuccess(null);
                    return;
                }
                listener.onSuccess(data.getData());
            }

            @Override
            public void onFailure(Call<SignInAccountData> call, Throwable t) {

            }
        });
    }
}
