package io.forus.kindpakket.android.kindpakket.service.api;


import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    // Not set to final, to allow testing
    private static String apiUrl = "http://mvp.forus.io/";

    private static final String LOG_NAME = ApiFactory.class.getName();
    private static ApiFactory instance = new ApiFactory(apiUrl);

    private final ShopkeeperServiceApi shopkeeperServiceApi;
    private final VoucherServiceApi voucherServiceApi;

    private ApiFactory(String url) {
        apiUrl = url;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(ApiParams.ACCEPT, ApiParams.ACCEPT_VALUE)
                                .addHeader(ApiParams.CONTENT_TYPE, ApiParams.CONTENT_TYPE_VALUE)
                                .addHeader(ApiParams.DEVICE_ID, ApiParams.DEVICE_ID_VALUE)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        shopkeeperServiceApi = retrofit.create(ShopkeeperServiceApi.class);
        voucherServiceApi = retrofit.create(VoucherServiceApi.class);
    }

    public static synchronized void build(String url) {
        instance = new ApiFactory(url);
        Log.d(LOG_NAME, "using api url: " + apiUrl);
    }

    public static ShopkeeperServiceApi getShopkeeperServiceApi() {
        return instance.shopkeeperServiceApi;
    }

    public static VoucherServiceApi getVoucherServiceApi() {
        return instance.voucherServiceApi;
    }
}
