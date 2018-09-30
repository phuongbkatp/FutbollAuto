package com.appian.manchesterunitednews.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionHelper {
    public static final int REQUEST_PERMISSIONS_STORAGE = 1;

    public static boolean checkStoragePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission_group.STORAGE)) {
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS_STORAGE);
            }
            return false;
        }
        return true;
    }
}
