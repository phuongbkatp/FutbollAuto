package com.appian.manchesterunitednews.app.user.presenter;

import com.appian.manchesterunitednews.app.BasePresenter;
import com.appian.manchesterunitednews.app.user.view.UpdateAvatarView;
import com.appian.manchesterunitednews.data.interactor.OnResponseListener;
import com.appian.manchesterunitednews.data.interactor.UserInteractor;
import com.appnet.android.football.fbvn.data.AccountProfile;
import com.appnet.android.football.fbvn.data.Avatar;
import com.appnet.android.football.fbvn.data.UserProfile;

import java.io.File;

public class UpdateAvatarPresenter extends BasePresenter<UpdateAvatarView> implements OnResponseListener<AccountProfile> {
    private final UserInteractor mInteractor;

    public UpdateAvatarPresenter(UserInteractor interactor) {
        mInteractor = interactor;
    }

    public void updateAvatar(String authorization, File file) {
        mInteractor.uploadAvatar(authorization, file, this);
    }

    @Override
    public void onSuccess(AccountProfile data) {
        if(getView() == null) {
            return;
        }
        UserProfile profile = data.getUserProfile();
        if(profile != null) {
            Avatar avatar = profile.getAvatar();
            if(avatar != null) {
                getView().updateAvatarSuccess(avatar.getMediumThumb());
            }
        }
    }

    @Override
    public void onFailure(String error) {
        if(getView() == null) {
            return;
        }
        getView().updateAvatarFail();
    }
}
