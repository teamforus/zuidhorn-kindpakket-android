package io.forus.kindpakket.android.kindpakket.service;


import android.util.Log;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import io.forus.kindpakket.android.kindpakket.model.Token;
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

    private static final Token INVALID = null;
    private Token token = null;

    OAuthService() {
    }

    public Token getToken() {
        return token;
    }

    private void setToken(Token token) {
        this.token = token;
    }

    public boolean hasValidToken() {
        // TODO: check if token is valid according to expires_in
        return token != INVALID;
    }

    public void loadToken(
            String username,
            String password,
            final ApiCallable.Success<Token> successCallable,
            final ApiCallable.Failure failureCallable) {
        if (!hasValidToken()) {
            Map<String, String> request = new HashMap<>();
            request.put("client_id", ServiceParams.API_CLIENT_ID);
            request.put("client_secret", ServiceParams.API_CLIENT_SECRET);
            request.put("username", username);
            request.put("password", password);
            request.put("grant_type", ServiceParams.API_GRANT_TYPE);
            request.put("scope", ServiceParams.API_SCOPE);

            ApiFactory.getOAuthServiceApi().getToken(request).enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    onLoginSuccess(response, successCallable, failureCallable);
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
                }
            });
        } else {
            Log.e(LOG_NAME, "A valid token was already loaded.");
        }
    }

    private void onLoginSuccess(Response<Token> response,
                                final ApiCallable.Success<Token> successCallable,
                                final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.i(LOG_NAME, response.body().toString());

            setToken(response.body());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error receiving a token: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }
}
