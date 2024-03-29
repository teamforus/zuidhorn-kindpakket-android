package io.forus.kindpakket.android.kindpakket.util;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.test.runner.AndroidJUnitRunner;

import com.facebook.testing.screenshot.ScreenshotRunner;

public class ScreenshotTestRunner extends AndroidJUnitRunner {

    private PowerManager.WakeLock mWakeLock;

    @Override
    public void callApplicationOnCreate(Application app)
    {
        // Unlock the screen
        KeyguardManager keyguard = (KeyguardManager) app.getSystemService(Context.KEYGUARD_SERVICE);
        keyguard.newKeyguardLock(getClass().getSimpleName()).disableKeyguard();

        // Start a wake lock
        PowerManager power = (PowerManager) app.getSystemService(Context.POWER_SERVICE);
        mWakeLock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, getClass().getSimpleName());
        mWakeLock.acquire();

        super.callApplicationOnCreate(app);
    }

    @Override
    public void onCreate(Bundle args) {
        ScreenshotRunner.onCreate(this, args);
        super.onCreate(args);
    }

    @Override
    public void onDestroy()
    {
        mWakeLock.release();

        super.onDestroy();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        ScreenshotRunner.onDestroy();
        super.finish(resultCode, results);
    }
}