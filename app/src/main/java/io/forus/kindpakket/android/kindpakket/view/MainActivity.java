package io.forus.kindpakket.android.kindpakket.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.view.registration.RegistrationActivity;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;

public class MainActivity extends AppCompatActivity {
    private static final int SCAN_TERMINAL_TOKEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (PreferencesChecker.isLoggedIn(this)) {
            Intent intent = new Intent(this, VoucherReadActivity.class);
            startActivity(intent);
        } else if (PreferencesChecker.hasToken(this)) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }

        final Context context = this;
        final Button loginButton = (Button) findViewById(R.id.main_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
        final Button registerButton = (Button) findViewById(R.id.main_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        final Button terminalButton = (Button) findViewById(R.id.main_terminal_button);
        terminalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent scannerIntent = new Intent(context, ScannerActivity.class);
                startActivityForResult(scannerIntent, SCAN_TERMINAL_TOKEN);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // show only, if the user is not logged in already
        if (PreferencesChecker.isLoggedIn(this)) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Context context = this;
        if (requestCode == SCAN_TERMINAL_TOKEN && resultCode == RESULT_OK) {
            String token = data.getStringExtra(ScannerActivity.SCANNER_RESULT);
            ServiceProvider.getShopkeeperService(context).deviceLogin(token,
                    new ApiCallable.Success<Token>() {
                        @Override
                        public void call(Token param) {
                            SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);
                            final SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean(SettingParams.PREFS_LOGGED_IN, true);
                            editor.apply();

                            Intent intent = new Intent(context, VoucherReadActivity.class);
                            startActivity(intent);
                        }
                    }, new ApiCallableFailureToast(this));
        }
    }
}
