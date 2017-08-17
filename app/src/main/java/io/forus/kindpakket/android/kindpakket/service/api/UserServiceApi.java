package io.forus.kindpakket.android.kindpakket.service.api;

import io.forus.kindpakket.android.kindpakket.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserServiceApi {
    @POST(ApiParams.URL_USER_LOGIN)
    Call<User> login(@Body Object object);

    @GET(ApiParams.URL_USER)
    Call<User> getUser(@Header(value = ApiParams.AUTHORIZATION) String token);
}
