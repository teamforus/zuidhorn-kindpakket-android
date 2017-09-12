package io.forus.kindpakket.android.kindpakket.util;


import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;

import static junit.framework.Assert.assertEquals;

public class PreferencesUtil {

    public static void setLoginStatus(boolean hasToken, boolean isLoggedIn) {
        SharedPreferences settings = InstrumentationRegistry.getTargetContext().getSharedPreferences(SettingParams.PREFS_NAME, 0);
        settings.edit().putBoolean(SettingParams.PREFS_LOGGED_IN, isLoggedIn).apply();
        if (hasToken) {
            settings.edit().putString(SettingParams.PREFS_TOKEN, "test-token").apply();
        } else {
            settings.edit().remove(SettingParams.PREFS_TOKEN).apply();
        }

        assertEquals("Not correctly initialized", isLoggedIn, PreferencesChecker.isLoggedIn(InstrumentationRegistry.getTargetContext()));
        assertEquals("Not correctly initialized", hasToken, PreferencesChecker.hasToken(InstrumentationRegistry.getTargetContext()));
    }
}
