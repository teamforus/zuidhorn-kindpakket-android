package io.forus.kindpakket.android.kindpakket.view.registration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.utils.Validator;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;

public class RegistrationActivity extends AppCompatActivity {
    private final String LOG_NAME = RegistrationActivity.class.getName();
    private boolean currentlyRegistering = false;

    private View progressView;
    private View registrationView;

    private EditText emailView;
    private EditText kvkView;
    private EditText ibanView;
    private Button submitButton;
    private Button checkStatusButton;
    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferencesChecker.isLoggedIn(this)) {
            setResult(RESULT_CANCELED);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationView = findViewById(R.id.registration_form);
        progressView = findViewById(R.id.registration_progress);

        emailView = (EditText) findViewById(R.id.registration_email);
        kvkView = (EditText) findViewById(R.id.registration_kvk);
        ibanView = (EditText) findViewById(R.id.registration_iban);
        statusView = (TextView) findViewById(R.id.registration_await_status);

        submitButton = (Button) findViewById(R.id.registration_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInput();
            }
        });

        checkStatusButton = (Button) findViewById(R.id.registration_await_button);
        final Context c = this;
        checkStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.setText(getString(R.string.registration_await_status_checking));
                showProgress(true);

                String token = ServiceProvider.getShopkeeperService(c).getToken();
                ServiceProvider.getShopkeeperService(c).validateToken(token, new ApiCallable.Success<Void>() {
                    @Override
                    public void call(Void param) {
                        statusView.setText(getString(R.string.registration_await_status_approved));
                        showProgress(false);

                        Intent intent = new Intent(RegistrationActivity.this, VoucherReadActivity.class);
                        startActivity(intent);
                    }
                }, new ApiCallable.Failure() {
                    @Override
                    public void call(ErrorMessage errorMessage) {
                        statusView.setText(getString(R.string.registration_await_status_notvalided));
                        showProgress(false);
                    }
                });
            }
        });

        // Load defaults
        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);
        emailView.setText(settings.getString(SettingParams.PREFS_USER_EMAIL, ""));
        kvkView.setText(settings.getString(SettingParams.PREFS_USER_KVK, ""));
        ibanView.setText(settings.getString(SettingParams.PREFS_USER_IBAN, ""));

        if (PreferencesChecker.hasToken(this)) {
            tokenReceived();
        }
    }

    private void processInput() {
        if (currentlyRegistering) {
            return;
        }

        boolean cancel = false;
        View focusView = null;

        showProgress(true);

        emailView.setError(null);
        kvkView.setError(null);
        ibanView.setError(null);

        Future<Boolean> validIban = Validator.isIbanValid(ibanView.getText().toString());
        Future<Boolean> validKvk = Validator.isKvkValid(kvkView.getText().toString());
        try {
            if (TextUtils.isEmpty(emailView.getText())) {
                emailView.setError(getString(R.string.error_field_required));
                focusView = emailView;
                cancel = true;
            } else if (!Validator.isEmailValid(emailView.getText().toString())) {
                emailView.setError(getString(R.string.error_invalid_email));
                focusView = emailView;
                cancel = true;
            } else if (TextUtils.isEmpty(kvkView.getText())) {
                kvkView.setError(getString(R.string.error_field_required));
                focusView = kvkView;
                cancel = true;
            } else if (!validKvk.get()) {
                kvkView.setError(getString(R.string.error_invalid_kvk));
                focusView = kvkView;
                cancel = true;
            } else if (TextUtils.isEmpty(ibanView.getText())) {
                ibanView.setError(getString(R.string.error_field_required));
                focusView = ibanView;
                cancel = true;
            } else if (!validIban.get()) {
                ibanView.setError(getString(R.string.error_invalid_iban));
                focusView = ibanView;
                cancel = true;
            }
        } catch (NullPointerException | InterruptedException | ExecutionException e) {
            Log.e(LOG_NAME, "error during validation", e);
            emailView.setError(getString(R.string.registration_failed));
        }

        if (cancel) {
            focusView.requestFocus();
            showProgress(false);
        } else {
            saveUserInput(emailView.getText().toString(),
                    kvkView.getText().toString(),
                    ibanView.getText().toString());

            executeRegisterRequest(emailView.getText().toString(),
                    kvkView.getText().toString(),
                    ibanView.getText().toString());
        }
    }

    private void saveUserInput(String email, String kvk, String iban) {
        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);

        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingParams.PREFS_USER_EMAIL, email);
        editor.remove(SettingParams.PREFS_USER_PASS);
        editor.putString(SettingParams.PREFS_USER_KVK, kvk);
        editor.putString(SettingParams.PREFS_USER_IBAN, iban);
        editor.apply();
    }

    private void executeRegisterRequest(String email, String kvk, String iban) {
        final Activity activity = this;
        ServiceProvider.getShopkeeperService(activity).register(
                email, kvk, iban,
                new ApiCallable.Success<Token>() {
                    @Override
                    public void call(Token param) {
                        tokenReceived();
                    }
                },
                new ApiCallable.Failure() {
                    @Override
                    public void call(ErrorMessage errorMessage) {
                        showProgress(false);

                        emailView.setError(getString(R.string.error_email_used));
                        emailView.requestFocus();

                        new ApiCallableFailureToast(activity).call(errorMessage);
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registrationView.setVisibility(show ? View.GONE : View.VISIBLE);
            registrationView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registrationView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            registrationView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void tokenReceived() {
        emailView.setEnabled(false);
        kvkView.setEnabled(false);
        ibanView.setEnabled(false);
        submitButton.setEnabled(false);

        showProgress(false);

        View view = findViewById(R.id.registration_await);
        view.setVisibility(View.VISIBLE);

        checkStatusButton.performClick();
    }
}
