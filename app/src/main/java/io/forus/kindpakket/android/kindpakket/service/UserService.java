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

    UserService() {
    }

    public void register(String email,
                         String password,
                         String kvk,
                         String iban,
                         final ApiCallable.Success<User> successCallable,
                         final ApiCallable.Failure failureCallable) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        request.put("kvk", kvk);
        request.put("iban", iban);

        ApiFactory.getUserApi().register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                onLoginSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    private void onLoginSuccess(Response<User> response,
                                final ApiCallable.Success<User> successCallable,
                                final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            User user = response.body();
            Log.i(LOG_NAME, "user registered " + user.toString());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error registration of user: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }
}