package io.forus.kindpakket.android.kindpakket.view.voucher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Voucher;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;

public class VoucherProcessActivity extends AppCompatActivity {
    public static final String INTENT_CODE = "intent_code";

    private static final String LOG_NAME = VoucherProcessActivity.class.getName();

    private String voucher_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_process);

        final TextView budget = (TextView) findViewById(R.id.voucher_process_budget);

        Intent intent = getIntent();
        voucher_code = intent.getStringExtra(INTENT_CODE);

        final Activity activity = this;
        ServiceProvider.getVoucherService().getVoucher(voucher_code,
                new ApiCallable.Success<Voucher>() {
                    @Override
                    public void call(Voucher voucher) {
                        Log.w(LOG_NAME, voucher.toString());
                        String amount = String.format("%.2d", voucher.getMaxAmount());
                        budget.setText(amount);
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
