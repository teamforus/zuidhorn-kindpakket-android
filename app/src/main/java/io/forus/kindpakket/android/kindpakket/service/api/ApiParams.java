package io.forus.kindpakket.android.kindpakket.service.api;

interface ApiParams {
    String ACCEPT = "Accept";
    String ACCEPT_VALUE = "application/json, text/javascript, /; q=0.01";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";
    String AUTHORIZATION = "Authorization";

    String URL_APP = "/api";

    String URL_OAUTH = URL_APP + "/oauth";
    String URL_OAUTH_TOKEN = URL_OAUTH + "/token";

    String URL_USER = URL_APP + "/user";
    String URL_USER_REGISTER = URL_APP + "/register";

    String URL_VOUCHER = URL_APP + "/vouchers";
    String URL_VOUCHER_ID = URL_VOUCHER + "/{id}";
}
