package com.appian.manchesterunitednews.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.data.app.AppConfigManager;
import com.appian.manchesterunitednews.data.app.Language;

public class ChooseLanguageActivity extends BaseActivity {

    private RelativeLayout mRlEnglish;
    private RelativeLayout mRlVietnamese;
    private View view_English;
    private View view_Vietnamese;
    String language;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language_activity);

        mRlEnglish = findViewById(R.id.settings_themeX_item_checkView);
        view_English = findViewById(R.id.view_settings_themeX_item_checkView);
        language = AppConfigManager.getInstance().getLanguage(this);
        if (language.equals(Language.ENGLISH)) {
            view_English.setBackground(getResources().getDrawable(R.drawable.theme_enable_circle));
        } else {
            view_English.setBackground(getResources().getDrawable(R.drawable.theme_disable_circle));
        }
        mRlEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Language.setLocale(ChooseLanguageActivity.this, Language.ENGLISH);
                view_English.setBackground(getResources().getDrawable(R.drawable.theme_enable_circle));
                view_Vietnamese.setBackground(getResources().getDrawable(R.drawable.theme_disable_circle));
                Intent intent = new Intent(ChooseLanguageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mRlVietnamese = findViewById(R.id.settings_theme8_item_checkView);
        view_Vietnamese = findViewById(R.id.view_settings_theme8_item_checkView);
        if (language.equals(Language.VIETNAMESE)) {
            view_Vietnamese.setBackground(getResources().getDrawable(R.drawable.theme_enable_circle));
        } else {
            view_Vietnamese.setBackground(getResources().getDrawable(R.drawable.theme_disable_circle));
        }
        mRlVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Language.setLocale(ChooseLanguageActivity.this, Language.VIETNAMESE);
                view_Vietnamese.setBackground(getResources().getDrawable(R.drawable.theme_enable_circle));
                view_English.setBackground(getResources().getDrawable(R.drawable.theme_disable_circle));
                Intent intent = new Intent(ChooseLanguageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
