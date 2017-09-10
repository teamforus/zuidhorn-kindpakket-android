package io.forus.kindpakket.android.kindpakket.utils;


public interface SettingParams {
    String PREFS_NAME = "PrefsFile";

    // Completed the registration process
    String PREFS_REGISTERED = "registered";
    // User received a token
    String PREFS_TOKEN = "token";
    // User is logged in / was approved (has a valid token)
    String PREFS_LOGGED_IN = "logged_in";

    String PREFS_USER_EMAIL = "user_email";
    String PREFS_USER_PASS = "user_password";
    String PREFS_USER_KVK = "user_kvk";
    String PREFS_USER_IBAN = "user_iban";
}
