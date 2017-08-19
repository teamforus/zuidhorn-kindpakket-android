package io.forus.kindpakket.android.kindpakket.view.toast;

import android.app.Activity;
import android.widget.Toast;

import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;

public class ApiCallableFailureToast implements ApiCallable.Failure {
    private final Activity _activity;

    public ApiCallableFailureToast(final Activity activity) {
        _activity = activity;
    }

    @Override
    public void call(final ErrorMessage errorMessage) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_activity, errorMessage.getMessage(_activity), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}