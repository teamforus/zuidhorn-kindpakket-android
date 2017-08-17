package io.forus.kindpakket.android.kindpakket.service;

import android.util.Log;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import io.forus.kindpakket.android.kindpakket.model.User;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallableExecuter;
import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerFailureErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerResponseErrorMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserService extends ApiCallableExecuter {
    private static final String LOG_NAME = UserService.class.getName();

    private User _user = User.INVALID;

    UserService() {
    }

    public User getCurrentUser() {
        return _user;
    }

    public boolean hasValidUser() {
        return _user != User.INVALID;
    }

    private void setUser(User user) {
        _user = user;
    }

    public void login(String email,
                      String password,
                      final ApiCallable.Success<User> successCallable,
                      final ApiCallable.Failure failureCallable) {
        if (!hasValidUser()) {
            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("password", password);

            ApiFactory.getUserApi().login(request).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    onLoginSuccess(response, successCallable, failureCallable);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
                }
            });
        } else {
            Log.e(LOG_NAME, "This user is already registered at the server.");
        }
    }

    private void onLoginSuccess(Response<User> response,
                                final ApiCallable.Success<User> successCallable,
                                final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            User user = response.body();
            Log.i(LOG_NAME, "user logged in " + user.toString());

            setUser(user);

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error creation of user: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }
}