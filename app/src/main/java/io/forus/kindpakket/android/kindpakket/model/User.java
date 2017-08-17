package io.forus.kindpakket.android.kindpakket.model;

public class User {
    public static final User INVALID = null;

    /**
     * _token gets initalized by Retrofit
     */
    private String _token;


    public String getToken() {
        return _token;
    }
}
