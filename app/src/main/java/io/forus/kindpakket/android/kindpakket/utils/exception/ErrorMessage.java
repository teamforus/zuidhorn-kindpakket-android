package io.forus.kindpakket.android.kindpakket.utils.exception;

import android.content.Context;

public interface ErrorMessage {
    String getMessage(Context context);

    String getDebugMessage();
}