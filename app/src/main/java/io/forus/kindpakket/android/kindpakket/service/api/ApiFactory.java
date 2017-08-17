package io.forus.kindpakket.android.kindpakket.service.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final String API_URL = "http://mvp.forus.io/";

    private static final ApiFactory instance = new ApiFactory();

    private final OAuthServiceApi oAuthServiceApi;
    private final UserServiceApi userServiceApi;

    private ApiFactory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        oAuthServiceApi = retrofit.create(OAuthServiceApi.class);
        userServiceApi = retrofit.create(UserServiceApi.class);
    }

    public static OAuthServiceApi getOAuthServiceApi() {
        return instance.oAuthServiceApi;
    }

    public static UserServiceApi getUserApi() {
        return instance.userServiceApi;
    }
}
