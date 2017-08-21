package io.forus.kindpakket.android.kindpakket.utils;

import android.content.Context;
import android.os.Build;
import android.util.Patterns;

import java.util.Locale;

public class Utils {
    private Utils() {
    }

    public static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
