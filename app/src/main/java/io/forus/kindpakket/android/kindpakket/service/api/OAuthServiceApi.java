package io.forus.kindpakket.android.kindpakket.service.api;

import io.forus.kindpakket.android.kindpakket.model.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OAuthServiceApi {
    @POST(ApiParams.URL_OAUTH_TOKEN)
    Call<Token> getToken(@Body Object object);
}