package io.forus.kindpakket.android.kindpakket.utils.exception;

import android.content.Context;

import io.forus.kindpakket.android.kindpakket.R;

public class ServerFailureErrorMessage implements ErrorMessage {
    private final Throwable _throwable;

    public ServerFailureErrorMessage(Throwable throwable) {
        _throwable = throwable;
    }

    @Override
    public String getMessage(Context context) {
        return context.getResources().getString(R.string.exception_server_response_failure);
    }

    @Override
    public String getDebugMessage() {
        if (_throwable != null) {
            if (_throwable.getLocalizedMessage() != null) {
                return _throwable.getLocalizedMessage();
            }
        }
        return "Unknown error";
    }
}