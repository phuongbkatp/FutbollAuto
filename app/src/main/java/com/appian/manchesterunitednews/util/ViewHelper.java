package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.MainActivity;
import com.appian.manchesterunitednews.app.SplashActivity;
import com.appian.manchesterunitednews.app.player.PlayerDetailsActivity;
import com.appian.manchesterunitednews.app.user.LogInActivity;

import java.util.Locale;

public final class ViewHelper {
    private static final String USER_AGENT_LOLLIPOP = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
    private static final String USER_AGENT_KITKAT = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";


    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static String getUserAvatar(int uid) {
        return Constant.BASE_URL + "picture/user/" + uid + "?type=small";
    }

    public static String formatFloat(float value) {
        return String.format(Locale.US, "%.02f", value);
    }

    public static void requestLogin(Context context) {
        Toast.makeText(context, R.string.please_login_to_continue, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, LogInActivity.class);
        context.startActivity(intent);
    }

    public static void launchMainScreen(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void restartApp(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void displayPlayerDetail(Context context, int id, String name) {
        /* Start a new Intent that passes player information to the SquadDetailsActivity */
        Intent intent = new Intent(context, PlayerDetailsActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_SOFA_PLAYER_ID, id);
        intent.putExtra(Constant.EXTRA_KEY_PLAYER_NAME, name);
        context.startActivity(intent);
    }

    public static void improveWebSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setUserAgentString(USER_AGENT_KITKAT);
        } else {
            webSettings.setUserAgentString(USER_AGENT_LOLLIPOP);
        }
    }

}
