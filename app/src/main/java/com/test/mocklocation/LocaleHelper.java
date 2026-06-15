package com.test.mocklocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {
    private static final String PREFS = "settings";
    private static final String KEY_LANGUAGE = "language_code";
    private static final String DEFAULT_LANGUAGE = "en";

    public static Context wrap(Context context) {
        String languageCode = getLanguageCode(context);
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    public static String getLanguageCode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }

    public static void saveLanguage(Context context, String languageCode) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, languageCode)
                .apply();
    }

    public static String getLanguageButtonText(Context context) {
        return "en".equals(getLanguageCode(context)) ? "中文" : "English";
    }
}
