package io.forus.kindpakket.android.kindpakket.utils.exception;

import android.content.Context;
import android.support.annotation.Nullable;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.ApiBase;

public class ServerResponseErrorMessage implements ErrorMessage {
    private final ApiBase response;
    private final String rawMessage;

    public ServerResponseErrorMessage(@Nullable ApiBase response, String rawMessage) {
        this.response = response;
        this.rawMessage = rawMessage;
    }

    @Override
    public String getMessage(Context context) {
        if (response != null && !response.getError().isEmpty()) {
            String message = response.getError();
            return context.getResources().getString(R.string.exception_server_response_notok) + message;
        }
        return context.getResources().getString(R.string.exception_server_response_failed) +
                "\n(" + getDebugMessage() + ")";
    }

    @Override
    public String getDebugMessage() {
        return rawMessage;
    }
}