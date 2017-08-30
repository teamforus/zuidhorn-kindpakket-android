package io.forus.kindpakket.android.kindpakket.util;


import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.DisplayMetrics;
import android.view.View;

import com.facebook.testing.screenshot.Screenshot;
import com.facebook.testing.screenshot.ViewHelpers;

import io.forus.kindpakket.android.kindpakket.R;

public class ScreenshotUtil {
    int dpHeight;
    int dpWidth;

    public ScreenshotUtil(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = (int)(displayMetrics.heightPixels / displayMetrics.density);
        dpWidth = (int)(displayMetrics.widthPixels / displayMetrics.density);
    }
    public void snap(final Activity activity, final View view) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewHelpers.setupView(view).setExactWidthDp(dpWidth).setExactHeightDp(dpHeight).layout();
                Screenshot.snap(view).record();
            }
        });
    }
    public void snap(final Activity activity) {
        Screenshot.snapActivity(activity).record();
    }
}
