package io.forus.kindpakket.android.kindpakket.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.view.registration.RegistrationActivity;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (PreferencesChecker.alreadyLoggedIn(this)) {
            Intent intent = new Intent(this, VoucherReadActivity.class);
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
    }
}
