package io.forus.kindpakket.android.kindpakket.service.api;

import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;

public interface ApiCallable {
    interface Success<Response> {
        void call(Response param);
    }

    interface Failure {
        void call(final ErrorMessage errorMessage);
    }
}