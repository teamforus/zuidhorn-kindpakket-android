package io.forus.kindpakket.android.kindpakket.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private static final String LOG_NAME = ScannerActivity.class.getName();

    public static final String SCANNER_RESULT = "result";

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(Arrays.asList(BarcodeFormat.QR_CODE));
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v(LOG_NAME, rawResult.getText());
        Log.v(LOG_NAME, rawResult.getBarcodeFormat().toString());

        // set the result of this activity
        Intent result = new Intent();
        result.putExtra(SCANNER_RESULT, rawResult.getText());
        setResult(RESULT_OK, result);

        finish();
    }
}