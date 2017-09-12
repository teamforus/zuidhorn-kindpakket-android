package io.forus.kindpakket.android.kindpakket.utils;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;

import java.util.Locale;

public class Utils {
    private static final Gson gson = new Gson();

    private Utils() {
    }

    public static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public static String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
