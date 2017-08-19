package io.forus.kindpakket.android.kindpakket.service.api;

import io.forus.kindpakket.android.kindpakket.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserServiceApi {
    @GET(ApiParams.URL_USER)
    Call<User> getUser(@Header(value = ApiParams.AUTHORIZATION) String token);

    @POST(ApiParams.URL_USER_REGISTER)
    Call<User> register(@Body Object object);
}
