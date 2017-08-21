package io.forus.kindpakket.android.kindpakket.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import io.forus.kindpakket.android.kindpakket.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private static final String LOG_NAME = ScannerActivity.class.getName();

    public static final String SCANNER_RESULT = "result";

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setFormats(Arrays.asList(BarcodeFormat.QR_CODE));
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(LOG_NAME, rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v(LOG_NAME, rawResult.getBarcodeFormat().toString());

        String result = rawResult.getText();
        showResult(result);
    }

    private void showResult(final String scanned) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(scanned);
        builder.setTitle(R.string.scanner_result);

        // Add the buttons
        builder.setPositiveButton(R.string.scanner_result_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // set the result of this activity
                Intent result = new Intent();
                result.putExtra(SCANNER_RESULT, scanned);
                setResult(RESULT_OK, result);

                finish();
            }
        });
        final ZXingScannerView.ResultHandler context = this;
        builder.setNegativeButton(R.string.scanner_result_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // If you would like to resume scanning, call this method below:
                mScannerView.resumeCameraPreview(context);
            }
        });

        builder.show();
    }
}