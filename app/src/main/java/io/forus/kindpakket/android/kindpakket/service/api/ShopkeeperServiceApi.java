package io.forus.kindpakket.android.kindpakket.service.api;

import java.util.Map;

import io.forus.kindpakket.android.kindpakket.model.Device;
import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ShopkeeperServiceApi {
    @POST(ApiParams.URL_SHOPKEEPER_DEVICE)
    Call<Token> deviceLogin(@Body Map<String, Object> request);

    @POST(ApiParams.URL_SHOPKEEPER_DEVICE_TOKEN)
    Call<Device> getDeviceToken(@Header(value = ApiParams.AUTHORIZATION) String token);

    @POST(ApiParams.URL_OAUTH_TOKEN)
    Call<Token> getToken(@Body Map<String, Object> request);

    @GET(ApiParams.URL_USER)
    Call<User> getUser(@Header(value = ApiParams.AUTHORIZATION) String token);

    @POST(ApiParams.URL_SHOPKEEPER_SIGNUP)
    Call<Token> register(@Body Map<String, Object> request);
}