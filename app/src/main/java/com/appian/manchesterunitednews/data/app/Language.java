package com.appian.manchesterunitednews.data.app;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public final class Language {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static final String EXTRA_LANGUAGE = "language";
    public static final String ENGLISH = "en";
    public static final String VIETNAMESE = "vi";
    public static final String FRENCH = "fr";
    public static final String THAILAND = "th";
    public static final String SPANISH = "es";

    public static String getDefaultLanguage() {
        Locale locale = Locale.getDefault();
        String iso3 = locale.getISO3Language();
        if("vie".equalsIgnoreCase(iso3)){
            return VIETNAMESE;
        } else if("fra".equalsIgnoreCase(iso3)) {
            return FRENCH;
        } else if("tha".equalsIgnoreCase(iso3)) {
            return THAILAND;
        } else if("spa".equalsIgnoreCase(iso3)) {
            return SPANISH;
        }
        return ENGLISH;
    }

    public static Context onAttach(Context context) {
        return setLocale(context, getPersistedData(context));
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, getDefaultLanguage());
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);
        Locale locale;
        if(Language.VIETNAMESE.equals(language)) {
            locale = new Locale("vi");
        } else if(Language.FRENCH.equals(language)) {
            locale = Locale.FRENCH;
        } else if(Language.SPANISH.equals(language)) {
            locale = new Locale("es");
        } else {
            locale = Locale.ENGLISH;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }
        return updateResourcesLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Locale locale) {
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static String getPersistedData(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, getDefaultLanguage());
    }
}
