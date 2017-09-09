package io.forus.kindpakket.android.kindpakket.view.terminal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.model.Device;
import io.forus.kindpakket.android.kindpakket.model.Token;
import io.forus.kindpakket.android.kindpakket.service.OAuthService;
import io.forus.kindpakket.android.kindpakket.service.ServiceProvider;
import io.forus.kindpakket.android.kindpakket.service.api.ApiCallable;
import io.forus.kindpakket.android.kindpakket.utils.OAuthServiceAdapter;
import io.forus.kindpakket.android.kindpakket.utils.exception.ErrorMessage;
import io.forus.kindpakket.android.kindpakket.view.toast.ApiCallableFailureToast;

public class TerminalInfoActivity extends AppCompatActivity {

    private static final int QR_CODE_SIZE = 200;
    private ImageView qrCode;
    private final MultiFormatWriter writer = new MultiFormatWriter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_info);

        qrCode = (ImageView) findViewById(R.id.terminal_info_qr);

        getDeviceToken();
    }


    private void getDeviceToken() {
        final Activity activity = this;
        final OAuthServiceAdapter adapter = new OAuthServiceAdapter(activity);
        adapter.execute(new ApiCallable.Success<Token>() {
            @Override
            public void call(Token token) {
                ServiceProvider.getShopkeeperService().getDeviceToken(
                        OAuthService.buildAuthorizationToken(token),
                        new ApiCallable.Success<Device>() {
                            @Override
                            public void call(Device param) {
                                updateQrCode(param.getToken());
                            }
                        },
                        new ApiCallable.Failure() {
                            @Override
                            public void call(final ErrorMessage errorMessage) {
                                new ApiCallableFailureToast(activity).call(errorMessage);
                            }
                        });
            }
        });
    }

    private void updateQrCode(final String qrData) {
        try {
            BitMatrix bm = writer.encode(qrData,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE);
            Bitmap ImageBitmap = Bitmap.createBitmap(QR_CODE_SIZE,
                    QR_CODE_SIZE,
                    Bitmap.Config.ARGB_8888);

            for (int i = 0; i < QR_CODE_SIZE; i++) {//width
                for (int j = 0; j < QR_CODE_SIZE; j++) {//height
                    ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(ImageBitmap);
        } catch (WriterException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.terminal_qr_generate_error), Toast.LENGTH_SHORT).show();
        }
    }
}
