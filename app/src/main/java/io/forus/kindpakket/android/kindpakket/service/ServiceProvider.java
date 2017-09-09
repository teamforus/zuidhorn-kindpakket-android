package io.forus.kindpakket.android.kindpakket.service;

public class ServiceProvider {
    private static OAuthService oAuthService;
    private static ShopkeeperService shopkeeperService;
    private static UserService userService;
    private static VoucherService voucherService;

    private ServiceProvider() {
    }

    public static synchronized OAuthService getOAuthService() {
        if (oAuthService == null) {
            oAuthService = new OAuthService();
        }
        return oAuthService;
    }

    public static synchronized ShopkeeperService getShopkeeperService() {
        if (shopkeeperService == null) {
            shopkeeperService = new ShopkeeperService();
        }
        return shopkeeperService;
    }

    public static synchronized UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public static synchronized VoucherService getVoucherService() {
        if (voucherService == null) {
            voucherService = new VoucherService();
        }
        return voucherService;
    }
}
