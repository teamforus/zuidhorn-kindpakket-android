package io.forus.kindpakket.android.kindpakket.model;


abstract class ApiBase {
    String description;

    String error;

    boolean success;

    public String getDescription() {
        return description;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
