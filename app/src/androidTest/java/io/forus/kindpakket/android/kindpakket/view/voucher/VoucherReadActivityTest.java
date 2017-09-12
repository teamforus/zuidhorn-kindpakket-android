package io.forus.kindpakket.android.kindpakket.view.voucher;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.util.PreferencesUtil;
import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;

@RunWith(AndroidJUnit4.class)
public class VoucherReadActivityTest {
    @Rule
    public IntentsTestRule<VoucherReadActivity> mActivityRule =
            new IntentsTestRule<>(VoucherReadActivity.class, true, false);

    @Test
    public void render() throws Exception {
        PreferencesUtil.setLoginStatus(true, true);

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}
