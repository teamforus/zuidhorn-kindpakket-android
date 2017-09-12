package io.forus.kindpakket.android.kindpakket.service;

import android.util.Log;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import io.forus.kindpakket.android.kindpakket.model.Voucher;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallableExecuter;
import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerFailureErrorMessage;
import io.forus.kindpakket.android.kindpakket.utils.exception.ServerResponseErrorMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VoucherService extends ApiCallableExecuter {
    private static final String LOG_NAME = VoucherService.class.getName();

    VoucherService() {
    }

    public void getVoucher(String voucherId,
                           String token,
                           final ApiCallable.Success<Voucher> successCallable,
                           final ApiCallable.Failure failureCallable) {

        ApiFactory.getVoucherServiceApi().getVoucher(token, voucherId).enqueue(new Callback<Voucher>() {
            @Override
            public void onResponse(Call<Voucher> call, Response<Voucher> response) {
                onGetVoucherSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<Voucher> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    private void onGetVoucherSuccess(Response<Voucher> response,
                                     final ApiCallable.Success<Voucher> successCallable,
                                     final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.i(LOG_NAME, "received voucher: " + response.body().toString());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error receiving voucher: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }

    public void useVoucher(String voucherId,
                           String token,
                           float amount,
                           final ApiCallable.Success<Voucher> successCallable,
                           final ApiCallable.Failure failureCallable) {
        Map<String, Object> request = new HashMap<>();
        request.put("amount", amount);

        ApiFactory.getVoucherServiceApi().useVoucher(token, voucherId, request).enqueue(new Callback<Voucher>() {
            @Override
            public void onResponse(Call<Voucher> call, Response<Voucher> response) {
                onUseVoucherSuccess(response, successCallable, failureCallable);
            }

            @Override
            public void onFailure(Call<Voucher> call, Throwable t) {
                onFailureCallable(failureCallable, new ServerFailureErrorMessage(t));
            }
        });
    }

    private void onUseVoucherSuccess(Response<Voucher> response,
                                     final ApiCallable.Success<Voucher> successCallable,
                                     final ApiCallable.Failure failureCallable) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Log.i(LOG_NAME, "used voucher: " + response.body().toString());

            onSuccessCallable(successCallable, response.body());
        } else {
            Log.w(LOG_NAME, "error using voucher: " + response.raw().message());

            ErrorMessage errorMessage = new ServerResponseErrorMessage(response);
            onFailureCallable(failureCallable, errorMessage);
        }
    }
}