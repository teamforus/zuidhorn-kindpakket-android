package io.forus.kindpakket.android.kindpakket.utils;

import android.util.Patterns;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class Validator {
    interface IbanValidatorApi {
        String URL = "https://openiban.com/";

        class IbanResponse {
            String iban;
            boolean valid = false;
        }

        @GET("/validate/{iban}?getBIC=false&validateBankCode=true")
        Call<IbanResponse> validate(@Path("iban") String iban);
    }

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Future<Boolean> isIbanValid(final String iban) {
        return executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(IbanValidatorApi.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                IbanValidatorApi service = retrofit.create(IbanValidatorApi.class);
                Response<IbanValidatorApi.IbanResponse> response = service.validate(iban).execute();
                return response.body().valid;
            }
        });
    }
}
