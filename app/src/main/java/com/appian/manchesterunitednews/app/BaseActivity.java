package com.appian.manchesterunitednews.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.appian.manchesterunitednews.data.app.Language;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.onAttach(newBase));
    }
}
