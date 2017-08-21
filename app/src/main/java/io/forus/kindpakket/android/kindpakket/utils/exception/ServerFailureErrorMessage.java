package io.forus.kindpakket.android.kindpakket.utils.exception;

import android.content.Context;

import io.forus.kindpakket.android.kindpakket.R;

public class ServerFailureErrorMessage implements ErrorMessage {
    private final Throwable throwable;

    public ServerFailureErrorMessage(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String getMessage(Context context) {
        return context.getResources().getString(R.string.exception_server_response_failure);
    }

    @Override
    public String getDebugMessage() {
        if (throwable != null && throwable.getLocalizedMessage() != null) {
            return throwable.getLocalizedMessage();
        }
        return "Unknown error";
    }
}