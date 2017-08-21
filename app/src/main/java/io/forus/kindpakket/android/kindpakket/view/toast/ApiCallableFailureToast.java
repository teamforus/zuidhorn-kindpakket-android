package io.forus.kindpakket.android.kindpakket.view.toast;

import android.app.Activity;
import android.widget.Toast;

import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;

public class ApiCallableFailureToast implements ApiCallable.Failure {
    private final Activity activity;

    public ApiCallableFailureToast(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void call(final ErrorMessage errorMessage) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, errorMessage.getMessage(activity), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}