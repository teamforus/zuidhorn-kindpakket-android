package io.forus.kindpakket.android.kindpakket;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;
import io.forus.kindpakket.android.kindpakket.view.registration.RegistrationActivity;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherProcessActivity;

@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest extends InstrumentationTestCase {
    @Rule
    public IntentsTestRule<RegistrationActivity> mActivityRule =
            new IntentsTestRule<>(RegistrationActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void render() {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}
