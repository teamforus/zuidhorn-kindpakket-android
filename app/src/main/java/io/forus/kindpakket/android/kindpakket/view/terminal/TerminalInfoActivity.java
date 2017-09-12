package io.forus.kindpakket.android.kindpakket.view.terminal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Device;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;

public class TerminalInfoActivity extends AppCompatActivity {
    private static final String LOG_NAME = TerminalInfoActivity.class.getName();

    private static final int QR_CODE_SIZE = 200;
    private ImageView qrCode;
    private final MultiFormatWriter writer = new MultiFormatWriter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!PreferencesChecker.isLoggedIn(this)) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_info);

        qrCode = (ImageView) findViewById(R.id.terminal_info_qr);

        getDeviceToken();
    }

    private void getDeviceToken() {
        final Activity activity = this;
        ServiceProvider.getShopkeeperService(activity).getDeviceToken(
                ServiceProvider.getShopkeeperService(activity).getToken(),
                new ApiCallable.Success<Device>() {
                    @Override
                    public void call(Device param) {
                        updateQrCode(param.getToken());

                        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.terminal_info_progress);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new ApiCallable.Failure() {
                    @Override
                    public void call(final ErrorMessage errorMessage) {
                        new ApiCallableFailureToast(activity).call(errorMessage);
                    }
                });
    }

    private void updateQrCode(final String qrData) {
        try {
            BitMatrix bm = writer.encode(qrData,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE);
            Bitmap imageBitmap = Bitmap.createBitmap(QR_CODE_SIZE,
                    QR_CODE_SIZE,
                    Bitmap.Config.ARGB_8888);

            for (int i = 0; i < QR_CODE_SIZE; i++) {//width
                for (int j = 0; j < QR_CODE_SIZE; j++) {//height
                    imageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(imageBitmap);
        } catch (WriterException e) {
            Log.e(LOG_NAME, e.getLocalizedMessage());

            Toast.makeText(getApplicationContext(), getString(R.string.terminal_qr_generate_error), Toast.LENGTH_SHORT).show();
        }
    }
}
