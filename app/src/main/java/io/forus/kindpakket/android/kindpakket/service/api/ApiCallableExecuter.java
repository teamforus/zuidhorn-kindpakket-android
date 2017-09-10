package io.forus.kindpakket.android.kindpakket.service.api;

import android.util.Log;

import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;

public class ApiCallableExecuter {
    private static final String LOG_NAME = ApiCallableExecuter.class.getName();

    /**
     * Call passed callable and handle exception
     */
    protected <T> void onSuccessCallable(ApiCallable.Success<T> onSuccessCallable,
                                         T response) {
        if (onSuccessCallable != null) {
            try {
                onSuccessCallable.call(response);
            } catch (Exception e) {
                Log.i(LOG_NAME, "onSuccessCallable Callable exception " + e.getLocalizedMessage());
            }
        } else {
            Log.i(LOG_NAME, "onSuccessCallable==null");
        }
    }

    /**
     * Call passed callable and handle exception
     */
    protected void onFailureCallable(ApiCallable.Failure onFailureCallable,
                                     ErrorMessage errorMessage) {
        // Log the throwable, just in case we will not handle the error
        Log.w(LOG_NAME, errorMessage.getDebugMessage());

        if (onFailureCallable != null) {
            try {
                onFailureCallable.call(errorMessage);
            } catch (Exception e) {
                Log.w(LOG_NAME, "onFailureCallable Callable exception " + e.getLocalizedMessage());
            }
        } else {
            Log.w(LOG_NAME, "onFailureCallable==null");
        }
    }
}