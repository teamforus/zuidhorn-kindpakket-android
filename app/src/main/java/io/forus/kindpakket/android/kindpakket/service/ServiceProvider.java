package io.forus.kindpakket.android.kindpakket.service;

import android.content.Context;

public class ServiceProvider {
    private static ShopkeeperService shopkeeperService;
    private static VoucherService voucherService;

    private ServiceProvider() {
    }

    public static synchronized ShopkeeperService getShopkeeperService(Context c) {
        if (shopkeeperService == null) {
            shopkeeperService = new ShopkeeperService(c);
        }
        return shopkeeperService;
    }

    public static synchronized VoucherService getVoucherService() {
        if (voucherService == null) {
            voucherService = new VoucherService();
        }
        return voucherService;
    }
}
