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
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this;

        if (alreadyRegistered()) {
            Intent intent = new Intent(context, VoucherReadActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button loginButton = (Button) findViewById(R.id.main_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                // TODO
                Intent intent = new Intent(context, VoucherReadActivity.class);
                startActivity(intent);
            }
        });
        final Button registerButton = (Button) findViewById(R.id.main_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO
            }
        });
    }

    private boolean alreadyRegistered() {
        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);
        return settings.getBoolean(SettingParams.PREFS_USER_LOGGED_IN, false);
    }
}
