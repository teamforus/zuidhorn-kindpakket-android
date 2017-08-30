package io.forus.kindpakket.android.kindpakket.util;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

import com.facebook.testing.screenshot.ScreenshotRunner;

public class ScreenshotTestRunner extends AndroidJUnitRunner {
    @Override
    public void onCreate(Bundle args) {
        MultiDex.install(getTargetContext());
        ScreenshotRunner.onCreate(this, args);
        super.onCreate(args);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        ScreenshotRunner.onDestroy();
        super.finish(resultCode, results);
    }
}