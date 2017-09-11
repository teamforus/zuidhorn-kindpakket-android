package io.forus.kindpakket.android.kindpakket.utils;

import android.util.Patterns;

import java.util.Map;
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
import retrofit2.http.Query;

public class Validator {
    private static final String LOG_NAME = Validator.class.getName();

    interface IbanValidatorApi {
        String URL = "https://openiban.com/";

        class IbanResponse {
            boolean valid = false;
        }

        @GET("/validate/{iban}?getBIC=false&validateBankCode=true")
        Call<IbanResponse> validate(@Path("iban") String iban);
    }

    interface KvkValidatorApi {
        String URL = "https://api.kvk.nl";
        String API_KEY = "l7xx45d657b473d94db29e58337db156ca29";

        class KvkResponse {
            boolean kvkValid;
            Map<String, Object> error;
        }

        @GET("/api/v2/profile/companies?&user_key=" + API_KEY)
        Call<KvkResponse> validate(@Query("q") String kvk);
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

    public static Future<Boolean> isKvkValid(final String kvk) {
        return executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(KvkValidatorApi.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                KvkValidatorApi service = retrofit.create(KvkValidatorApi.class);
                Response<KvkValidatorApi.KvkResponse> response = service.validate(kvk).execute();

                return response.isSuccessful();
            }
        });
    }
}
