package io.forus.kindpakket.android.kindpakket.view.registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.User;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.LoginActivity;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText kvkView;
    private EditText ibanView;
    private EditText companynameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferencesChecker.alreadyRegistered(this)) {
            setResult(RESULT_CANCELED);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailView = (EditText) findViewById(R.id.registration_email);
        kvkView = (EditText) findViewById(R.id.registration_kvk);
        ibanView = (EditText) findViewById(R.id.registration_iban);
        companynameView = (EditText) findViewById(R.id.registration_companyname);

        Button submitButton = (Button) findViewById(R.id.registration_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInput();
            }
        });
    }

    private void processInput() {
        SharedPreferences settings = getSharedPreferences(SettingParams.PREFS_NAME, 0);

        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingParams.PREFS_USER_EMAIL, emailView.getText().toString());
        editor.remove(SettingParams.PREFS_USER_PASS);
        editor.putString(SettingParams.PREFS_USER_KVK, kvkView.getText().toString());
        editor.putString(SettingParams.PREFS_USER_IBAN, ibanView.getText().toString());
        editor.putString(SettingParams.PREFS_USER_COMPANYNAME, companynameView.getText().toString());
        editor.apply();

        final Activity activity = this;
        ServiceProvider.getUserService().register(
                emailView.getText().toString(),
                kvkView.getText().toString(),
                ibanView.getText().toString(),
                companynameView.getText().toString(),
                new ApiCallable.Success<User>() {
                    @Override
                    public void call(User param) {
                        editor.putBoolean(SettingParams.PREFS_USER_REGISTERED, true);
                        editor.apply();

                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivity(intent);
                    }
                },
                new ApiCallable.Failure() {
                    @Override
                    public void call(ErrorMessage errorMessage) {
                        new ApiCallableFailureToast(activity).call(errorMessage);
                    }
                });
    }

}
