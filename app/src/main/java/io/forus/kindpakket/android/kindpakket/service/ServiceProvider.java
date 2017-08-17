package io.forus.kindpakket.android.kindpakket.service;

public class ServiceProvider {
    private static OAuthService _oAuthService;
    private static UserService _userService;

    private ServiceProvider() {
    }

    public static synchronized OAuthService getOAuthService() {
        if (_oAuthService == null) {
            _oAuthService = new OAuthService();
        }
        return _oAuthService;
    }

    public static synchronized UserService getUserService() {
        if (_userService == null) {
            _userService = new UserService();
        }
        return _userService;
    }
}
