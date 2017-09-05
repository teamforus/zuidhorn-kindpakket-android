package io.forus.kindpakket.android.kindpakket.view.registration;

import android.content.Context;
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

@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {
    @Rule
    public IntentsTestRule<RegistrationActivity> mActivityRule =
            new IntentsTestRule<>(RegistrationActivity.class, true, false);

    @Test
    public void render() throws Exception {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}
