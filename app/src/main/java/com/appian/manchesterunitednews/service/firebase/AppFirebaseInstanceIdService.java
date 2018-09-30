package com.appian.manchesterunitednews.service.firebase;

import com.appian.manchesterunitednews.service.app.AppHelper;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class AppFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        AppHelper.refreshDeviceToken(getApplicationContext());
    }
}
