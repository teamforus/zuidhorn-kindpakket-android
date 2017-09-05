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

import io.forus.kindpakket.android.kindpakket.service.api.ApiFactory;
import io.forus.kindpakket.android.kindpakket.util.RestServiceTestHelper;
import io.forus.kindpakket.android.kindpakket.util.ScreenshotUtil;
import io.forus.kindpakket.android.kindpakket.utils.PreferencesChecker;
import io.forus.kindpakket.android.kindpakket.utils.SettingParams;
import io.forus.kindpakket.android.kindpakket.view.voucher.VoucherProcessActivity;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VoucherProcessActivityTest {
    @Rule
    public IntentsTestRule<VoucherProcessActivity> mActivityRule =
            new IntentsTestRule<>(VoucherProcessActivity.class, true, false);
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        ApiFactory.build(server.url("/").toString());
    }

    @Test
    public void render() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(InstrumentationRegistry.getContext(), "oauth_token_200_ok.json")));
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(InstrumentationRegistry.getContext(), "voucher_get_200.json")));

        Intent intent = new Intent();
        intent.putExtra(VoucherProcessActivity.INTENT_CODE, "VIES-2F9M-J8RR-TC5W");
        mActivityRule.launchActivity(intent);

        new ScreenshotUtil(mActivityRule.getActivity()).snap(mActivityRule.getActivity());
    }
}
