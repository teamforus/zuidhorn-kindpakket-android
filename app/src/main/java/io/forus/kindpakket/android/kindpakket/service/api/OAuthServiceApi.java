package io.forus.kindpakket.android.kindpakket.service.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OAuthServiceApi {
    @POST(ApiParams.URL_OAUTH_TOKEN)
    Call<Object> getToken(@Body Object object);
}