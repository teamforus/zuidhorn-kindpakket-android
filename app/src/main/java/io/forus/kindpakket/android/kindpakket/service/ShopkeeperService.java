package io.forus.kindpakket.android.kindpakket.service;

import android.util.Log;

import java.net.HttpURLConnection;

import io.forus.kindpakket.android.kindpakket.model.Device;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallableExecuter;
import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerFailureErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerResponseErrorMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopkeeperService extends ApiCallableExecuter {
    private static final String LOG_NAME = ShopkeeperService.class.getName();

    ShopkeeperService() {
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

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }
}
