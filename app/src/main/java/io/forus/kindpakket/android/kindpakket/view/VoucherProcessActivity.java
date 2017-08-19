package io.forus.kindpakket.android.kindpakket.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.forus.kindpakket.android.kindpakket.R;

public class VoucherProcessActivity extends AppCompatActivity {
    public static final String INTENT_CODE = "intent_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_process);

        Intent intent = getIntent();
        Toast.makeText(this, intent.getStringExtra(INTENT_CODE), Toast.LENGTH_SHORT);
    }
}
