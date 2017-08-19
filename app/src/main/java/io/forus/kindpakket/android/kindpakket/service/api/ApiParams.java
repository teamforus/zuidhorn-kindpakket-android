package io.forus.kindpakket.android.kindpakket.service.api;

interface ApiParams {
    String AUTHORIZATION = "Authorization";

    String URL_OAUTH = "/oauth";
    String URL_OAUTH_TOKEN = URL_OAUTH + "/token";

    String URL_APP = "/app";

    String URL_USER = URL_APP + "/user";
    String URL_USER_REGISTER = URL_USER + "/register";

    String URL_VOUCHER = URL_APP + "/voucher";
    String URL_VOUCHER_ID = URL_VOUCHER + "/{id}";
}
