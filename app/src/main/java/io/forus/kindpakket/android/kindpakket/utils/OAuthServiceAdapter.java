package io.forus.kindpakket.android.kindpakket.utils;


import android.app.Activity;
import android.content.SharedPreferences;

import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;

public class OAuthServiceAdapter {
    private final Activity activity;

    public OAuthServiceAdapter(Activity activity) {
        this.activity = activity;
    }

    public void execute(ApiCallable.Success<Token> tokenSuccessCallable) {
        SharedPreferences settings = activity.getSharedPreferences(SettingParams.PREFS_NAME, 0);
        String email = settings.getString(SettingParams.PREFS_USER_EMAIL, "");
        String password = settings.getString(SettingParams.PREFS_USER_PASS, "");

        ServiceProvider.getOAuthService().loadToken(email, password,
                tokenSuccessCallable,
                new ApiCallable.Failure() {
                    @Override
                    public void call(ErrorMessage errorMessage) {
                        new ApiCallableFailureToast(activity).call(errorMessage);
                    }
                });
    }
}
