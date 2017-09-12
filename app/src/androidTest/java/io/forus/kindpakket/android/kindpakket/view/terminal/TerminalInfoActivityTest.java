package io.forus.kindpakket.android.kindpakket.view.terminal;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.util.PreferencesUtil;
import io.forus.kindpakket.android.kindpakket.util.RestServiceTestHelper;
import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;


@RunWith(AndroidJUnit4.class)
public class TerminalInfoActivityTest {
    @Rule
    public IntentsTestRule<TerminalInfoActivity> mActivityRule =
            new IntentsTestRule<>(TerminalInfoActivity.class, true, false);

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        ApiFactory.build(server.url("/").toString());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void render() throws Exception {
        PreferencesUtil.setLoginStatus(true, true);

        String fileName = "terminalinfo_add_qr_200_ok.json";

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(InstrumentationRegistry.getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}