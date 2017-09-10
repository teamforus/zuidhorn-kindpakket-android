package io.forus.kindpakket.android.kindpakket.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesChecker {
    private PreferencesChecker() {
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SettingParams.PREFS_NAME, 0);
        return settings.getBoolean(SettingParams.PREFS_LOGGED_IN, false);
    }

    public static boolean hasToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SettingParams.PREFS_NAME, 0);
        return settings.contains(SettingParams.PREFS_TOKEN);
    }
}
