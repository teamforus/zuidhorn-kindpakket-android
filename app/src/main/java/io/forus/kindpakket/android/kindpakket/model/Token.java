package io.forus.kindpakket.android.kindpakket.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("token_type")
    String tokenType;

    @SerializedName("expires_in")
    String expiresIn;

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("refresh_token")
    String refreshToken;


}
