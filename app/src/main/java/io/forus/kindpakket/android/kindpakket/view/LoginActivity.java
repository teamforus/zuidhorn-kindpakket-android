package io.forus.kindpakket.android.kindpakket.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.utils.Validator;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String URL_PASS_RESET = "http://mvp.forus.io/password/reset";
    private boolean currentLoggingIn = false;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View progressView;
    private View loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        loginView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        mEmailView = (EditText) findViewById(R.id.login_email);

        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.login_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView mForgotPassword = (TextView) findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWebSite(URL_PASS_RESET);
            }
        });


        // Load defaults
        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);
        mEmailView.setText(settings.getString(SettingParams.PREFS_USER_EMAIL, ""));
        mPasswordView.setText(settings.getString(SettingParams.PREFS_USER_PASS, ""));
    }

    private void showWebSite(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        this.startActivity(webIntent);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (currentLoggingIn) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Validator.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            currentLoggingIn = true;
            showProgress(true);

            saveUserInput(email, password);

            executeLoginRequest(email, password);
        }
    }

    private void saveUserInput(String email, String password) {
        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingParams.PREFS_USER_EMAIL, email);
        editor.putString(SettingParams.PREFS_USER_PASS, password);
        editor.apply();
    }

    private void executeLoginRequest(String email, String password) {
        final Context context = this;
        ServiceProvider.getOAuthService().loadToken(
                email,
                password,
                new ApiCallable.Success<Token>() {
                    @Override
                    public void call(Token param) {
                        currentLoggingIn = false;
                        showProgress(false);

                        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);
                        final SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(SettingParams.PREFS_USER_LOGGED_IN, true);
                        editor.apply();

                        Intent intent = new Intent(context, VoucherReadActivity.class);
                        startActivity(intent);
                    }
                },
                new ApiCallable.Failure() {
                    @Override
                    public void call(ErrorMessage errorMessage) {
                        currentLoggingIn = false;
                        showProgress(false);

                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }
        );
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            loginView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

