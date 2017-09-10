package io.forus.kindpakket.android.kindpakket.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token extends ApiBase implements Serializable {
    @SerializedName("access_token")
    String accessToken;

    @SerializedName("expires_in")
    String expiresIn;

    @SerializedName("refresh_token")
    String refreshToken;

    @SerializedName("token_type")
    String tokenType;

    public Token(String accessToken, String expiresIn, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType='" + tokenType + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
