package io.forus.kindpakket.android.kindpakket.view.voucher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VoucherReadActivityTest {
    @Rule
    public IntentsTestRule<VoucherReadActivity> mActivityRule =
            new IntentsTestRule<>(VoucherReadActivity.class, true, false);

    @Test
    public void render() throws Exception {
        SharedPreferences settings = InstrumentationRegistry.getTargetContext().getSharedPreferences(SettingParams.PREFS_NAME, 0);
        settings.edit().putBoolean(SettingParams.PREFS_TOKEN, true).apply();
        assertEquals("Not correctly initialized", true, PreferencesChecker.hasToken(InstrumentationRegistry.getTargetContext()));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}
