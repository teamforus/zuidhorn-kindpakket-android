package io.forus.kindpakket.android.kindpakket.view.voucher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Voucher;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.Utils;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;

public class VoucherProcessActivity extends AppCompatActivity {
    public static final String INTENT_CODE = "intent_code";

    private static final String LOG_NAME = VoucherProcessActivity.class.getName();

    private String voucherCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!PreferencesChecker.isLoggedIn(this)) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_process);

        // Read voucher code
        Intent intent = getIntent();
        voucherCode = intent.getStringExtra(INTENT_CODE);

        final Activity activity = this;

        final TextView amountView = (TextView) findViewById(R.id.voucher_process_amount);
        final Button processButton = (Button) findViewById(R.id.voucher_process_button);
        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float amount = Float.parseFloat(amountView.getText().toString());
                ServiceProvider.getVoucherService().useVoucher(voucherCode,
                        ServiceProvider.getShopkeeperService(activity).getToken(),
                        amount,
                        new ApiCallable.Success<Voucher>() {
                            @Override
                            public void call(Voucher voucher) {
                                Log.i(LOG_NAME, "voucher was used: " + voucher);

                                finish();
                            }
                        },
                        new ApiCallable.Failure() {
                            @Override
                            public void call(ErrorMessage errorMessage) {
                                new ApiCallableFailureToast(activity).call(errorMessage);
                            }
                        });
            }
        });

        final TextView budgetView = (TextView) findViewById(R.id.voucher_process_budget);
        ServiceProvider.getVoucherService().getVoucher(voucherCode,
                ServiceProvider.getShopkeeperService(activity).getToken(),
                new ApiCallable.Success<Voucher>() {
                    @Override
                    public void call(Voucher voucher) {
                        Log.i(LOG_NAME, voucher.toString());
                        String amount = String.format(
                                Utils.getCurrentLocale(activity),
                                "%.2f", voucher.getMaxAmount());
                        budgetView.setText(amount);
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
