package io.forus.kindpakket.android.kindpakket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.util.RestServiceTestHelper;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.view.LoginActivity;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherReadActivity;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule =
            new IntentsTestRule<>(LoginActivity.class, true, false);
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        ApiFactory.build(server.url("/").toString());
    }

    private void executeUiLogin() {
        onView(ViewMatchers.withId(R.id.login_email))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("test@forus.io"));
        onView(ViewMatchers.withId(R.id.login_password))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("testforusio"));
        onView(ViewMatchers.withId(R.id.login_sign_in_button))
                .perform(ViewActions.click());
    }

    @Test
    public void testLoginSuccessful() throws Exception {
        String fileName = "oauth_token_200_ok.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(InstrumentationRegistry.getInstrumentation().getContext(), fileName)));

        SharedPreferences prefs = getTargetContext().getSharedPreferences(SettingParams.PREFS_NAME, 0);
        prefs.edit().remove(SettingParams.PREFS_USER_LOGGED_IN).apply();

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        executeUiLogin();

        boolean isLoggedIn = prefs.getBoolean(SettingParams.PREFS_USER_LOGGED_IN, false);
        assertEquals("User should now be logged in", true, isLoggedIn);

        intended(hasComponent(VoucherReadActivity.class.getName()));
    }

    @Test
    public void testLoginFailed() throws Exception {
        String fileName = "oauth_token_401_unauthorized.json";

        server.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody(RestServiceTestHelper.getStringFromFile(InstrumentationRegistry.getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        executeUiLogin();

        String errorMessage = getTargetContext().getString(R.string.error_incorrect_password);
        onView(withId(R.id.login_password)).check(matches(hasErrorText(errorMessage)));
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}