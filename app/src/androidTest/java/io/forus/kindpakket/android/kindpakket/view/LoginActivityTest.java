package io.forus.kindpakket.android.kindpakket.view;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;
import okhttp3.mockwebserver.MockWebServer;


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

    @Test
    public void render() {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}