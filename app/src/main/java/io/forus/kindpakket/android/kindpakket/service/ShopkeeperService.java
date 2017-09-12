package io.forus.kindpakket.android.kindpakket.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import io.forus.kindpakket.android.kindpakket.model.Device;
import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.model.User;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallableExecuter;
import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.utils.Utils;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerFailureErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerResponseErrorMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopkeeperService extends ApiCallableExecuter {
    private static final String LOG_NAME = ShopkeeperService.class.getName();

    private static final Token INVALID = null;

    private final Context context;
    private Token token = INVALID;

    ShopkeeperService(Context c) {
        context = c;

        SharedPreferences settings = context.getSharedPreferences(SettingParams.PREFS_NAME, 0);
        String tokenObj = settings.getString(SettingParams.PREFS_TOKEN, "");
        token = Utils.deserialize(tokenObj, Token.class);
    }

    public String getToken() {
        //TODO: Is this the right place for this function?
        if (token != null) {
            return token.getTokenType() + " " + token.getAccessToken();
        }
        return "";
    }

    private boolean hasValidToken() {
        // TODO: check if token is valid according to expires_in
        return token != INVALID;
    }

    public void validateToken(String token, final ApiCallable.Success<Void> successCallable,
                              final ApiCallable.Failure failureCallable) {
        getUser(token, new ApiCallable.Success<User>() {
            @Override
            public void call(User param) {
                SharedPreferences settings = context.getSharedPreferences(SettingParams.PREFS_NAME, 0);
                final SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(SettingParams.PREFS_LOGGED_IN, true);
                editor.apply();

                successCallable.call(null);
            }
        }, failureCallable);
    }

    public void getUser(String token,
                        final ApiCallable.Success<User> successCallable,
                        final ApiCallable.Failure failureCallable) {
        ApiFactory.getShopkeeperServiceApi().getUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                onGetUserSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    private void onGetUserSuccess(Response<User> response,
                                  final ApiCallable.Success<User> successCallable,
                                  final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.d(LOG_NAME, response.body().toString());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.e(LOG_NAME, "error receiving user: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response.body(), response.raw().message());
            onFailureCallable(failureCallable, errorMessage);
        }
    }

    public void loadToken(
            String username,
            String password,
            final ApiCallable.Success<Token> successCallable,
            final ApiCallable.Failure failureCallable) {
        if (!hasValidToken()) {
            Map<String, Object> request = new HashMap<>();
            request.put("client_id", ServiceParams.API_CLIENT_ID);
            request.put("client_secret", ServiceParams.API_CLIENT_SECRET);
            request.put("username", username);
            request.put("password", password);
            request.put("grant_type", ServiceParams.API_GRANT_TYPE);
            request.put("scope", ServiceParams.API_SCOPE);

            ApiFactory.getShopkeeperServiceApi().getToken(request).enqueue(new Callback<Token>() {
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
            Log.i(LOG_NAME, "A valid token was already loaded.");

            successCallable.call(token);
        }
    }


    public void register(String email,
                         String kvk,
                         String iban,
                         final ApiCallable.Success<Token> successCallable,
                         final ApiCallable.Failure failureCallable) {
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("iban", iban);
        request.put("kvk_number", kvk);

        ApiFactory.getShopkeeperServiceApi().register(request).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                onLoginSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    private void onLoginSuccess(Response<Token> response,
                                final ApiCallable.Success<Token> successCallable,
                                final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.d(LOG_NAME, response.body().toString());

            setToken(response.body());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.e(LOG_NAME, "error receiving a token: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response.body(), response.raw().message());
            onFailureCallable(failureCallable, errorMessage);
        }
    }

    public void deviceLogin(String deviceToken,
                            final ApiCallable.Success<Token> successCallable,
                            final ApiCallable.Failure failureCallable) {
        Map<String, Object> request = new HashMap<>();
        request.put("token", deviceToken);

        ApiFactory.getShopkeeperServiceApi().deviceLogin(request).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                onLoginSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    public void getDeviceToken(String token,
                               final ApiCallable.Success<Device> successCallable,
                               final ApiCallable.Failure failureCallable) {

        ApiFactory.getShopkeeperServiceApi().getDeviceToken(token).enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                getDeviceTokenSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    private void getDeviceTokenSuccess(Response<Device> response,
                                       final ApiCallable.Success<Device> successCallable,
                                       final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.i(LOG_NAME, "getDeviceToken: " + response.body().toString());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error getDeviceToken: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response.body(), response.raw().message());
            onFailureCallable(failureCallable, errorMessage);
        }
    }

    private void setToken(Token token) {
        this.token = token;

        if (token != null) {
            SharedPreferences settings = context.getSharedPreferences(SettingParams.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(SettingParams.PREFS_TOKEN, Utils.serialize(token));
            editor.apply();
        }
    }
}
