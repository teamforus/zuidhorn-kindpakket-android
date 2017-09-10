package io.forus.kindpakket.android.kindpakket.view;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule =
            new IntentsTestRule<>(LoginActivity.class, true, false);

    @Test
    public void render() {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}