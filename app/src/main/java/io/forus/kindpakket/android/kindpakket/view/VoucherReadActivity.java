package io.forus.kindpakket.android.kindpakket.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Voucher;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;

public class VoucherReadActivity extends AppCompatActivity {
    private static final int SCAN_CODE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_read);

        final Button codeEnteredButton = (Button) findViewById(R.id.voucher_read_voucher_button);
        codeEnteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEnteredButton.getText().toString();
                processCode(code);
            }
        });

        final Button scannerButton = (Button) findViewById(R.id.voucher_read_scanner_button);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scannerIntent = new Intent(VoucherReadActivity.this, ScannerActivity.class);
                startActivityForResult(scannerIntent, SCAN_CODE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_CODE_REQUEST && resultCode == RESULT_OK) {
            String code = data.getStringExtra(ScannerActivity.SCANNER_RESULT);
            processCode(code);
        }
    }

    private void processCode(final String code) {
        final Activity activity = this;
        ServiceProvider.getVoucherService().getVoucher(code,
                new ApiCallable.Success<Voucher>() {
                    @Override
                    public void call(Voucher param) {
                        Intent intent = new Intent(activity, VoucherProcessActivity.class);
                        intent.putExtra(VoucherProcessActivity.INTENT_CODE, code);
                        startActivity(intent);
                    }
                },
                new ApiCallable.Failure() {
                    @Override
                    public void call(final ErrorMessage errorMessage) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, getResources().getString(R.string.voucher_read_invalid_code) + "\n" + errorMessage, Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
    }
}
