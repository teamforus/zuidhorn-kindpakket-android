package io.forus.kindpakket.android.kindpakket.model;

import com.google.gson.annotations.SerializedName;

public class Voucher {
    public static final Voucher INVALID = null;

    private String code;

    @SerializedName("max_amount")
    private float maxAmount;

    public String getCode() {
        return code;
    }

    public float getMaxAmount() {
        return maxAmount;
    }
}
