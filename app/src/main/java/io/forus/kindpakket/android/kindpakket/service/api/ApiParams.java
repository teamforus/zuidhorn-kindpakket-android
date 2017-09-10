package io.forus.kindpakket.android.kindpakket.service.api;

import android.provider.Settings;

interface ApiParams {
    String ACCEPT = "Accept";
    String ACCEPT_VALUE = "application/json, text/javascript, /; q=0.01";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";
    String AUTHORIZATION = "Authorization";
    String DEVICE_ID = "Device-Id";
    String DEVICE_ID_VALUE = Settings.Secure.ANDROID_ID;

    String URL_APP = "/api";

    String URL_OAUTH = URL_APP + "/oauth";
    String URL_OAUTH_TOKEN = URL_OAUTH + "/token";

    String URL_SHOPKEEPER = URL_APP + "/shop-keeper";
    String URL_SHOPKEEPER_DEVICE = URL_SHOPKEEPER + "/device";
    String URL_SHOPKEEPER_DEVICE_TOKEN = URL_SHOPKEEPER_DEVICE + "/token";
    String URL_SHOPKEEPER_SIGNUP = URL_SHOPKEEPER + "/sign-up";

    String URL_VOUCHER = URL_APP + "/voucher";
    String URL_VOUCHER_ID = URL_VOUCHER + "/{id}";
}
