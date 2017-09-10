package io.forus.kindpakket.android.kindpakket.model;


public class Device extends ApiBase {
    String token;

    Device(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
