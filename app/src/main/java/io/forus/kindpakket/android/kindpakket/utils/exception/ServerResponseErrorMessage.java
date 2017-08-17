package io.forus.kindpakket.android.kindpakket.utils.exception;

import android.content.Context;

import java.io.IOException;

import io.forus.kindpakket.android.kindpakket.R;
import retrofit2.Response;

public class ServerResponseErrorMessage implements ErrorMessage {
    private final Response<?> _response;

    public ServerResponseErrorMessage(Response<?> response) {
        _response = response;
    }

    @Override
    public String getMessage(Context context) {
        return context.getResources().getString(R.string.exception_server_response_notok);
    }

    @Override
    public String getDebugMessage() {
        if (_response != null) {
            try {
                return _response.errorBody().string();
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        }
        return "Unknown error";
    }
}