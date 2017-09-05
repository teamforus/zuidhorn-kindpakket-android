package io.forus.kindpakket.android.kindpakket.view;


import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.view.LayoutInflater;
import com.facebook.testing.screenshot.Screenshot;
import com.facebook.testing.screenshot.ViewHelpers;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.R;
import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.view.MainActivity;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void render() throws Exception {
        SharedPreferences settings = InstrumentationRegistry.getTargetContext().getSharedPreferences(SettingParams.PREFS_NAME, 0);
        settings.edit().remove(SettingParams.PREFS_USER_LOGGED_IN).apply();
        assertEquals("Not correctly initialized", false, PreferencesChecker.alreadyLoggedIn(InstrumentationRegistry.getTargetContext()));

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity(), mActivityRule.getActivity().findViewById(R.id.activity_main));
    }
}
