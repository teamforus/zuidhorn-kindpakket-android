package io.forus.kindpakket.android.kindpakket.service;


import android.util.Log;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallableExecuter;
import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerFailureErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerResponseErrorMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OAuthService extends ApiCallableExecuter {
    private static final String LOG_NAME = OAuthService.class.getName();

    private static final String INVALID = null;
    private static String TOKEN = null;

    OAuthService() {
    }

    public String getToken() {
        return TOKEN;
    }

    private void setToken(String token) {
        TOKEN = token;
    }

    public boolean hasValidToken() {
        return TOKEN != INVALID;
    }

    public void loadToken(
            String username,
            String password,
            final ApiCallable.Success<Object> successCallable,
            final ApiCallable.Failure failureCallable) {
        if (!hasValidToken()) {
            Map<String, String> request = new HashMap<>();
            request.put("client_id", ServiceParams.API_CLIENT_ID);
            request.put("client_secret", ServiceParams.API_CLIENT_SECRET);
            request.put("username", username);
            request.put("password", password);
            request.put("grant_type", ServiceParams.API_GRANT_TYPE);
            request.put("scope", ServiceParams.API_SCOPE);

            ApiFactory.getOAuthServiceApi().getToken(request).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    onLoginSuccess(response, successCallable, failureCallable);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
                }
            });
        } else {
            Log.e(LOG_NAME, "A valid token was already loaded.");
        }
    }

    private void onLoginSuccess(Response<Object> response,
                                final ApiCallable.Success<Object> successCallable,
                                final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.i(LOG_NAME, response.body().toString());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error receiving a token: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }
}
