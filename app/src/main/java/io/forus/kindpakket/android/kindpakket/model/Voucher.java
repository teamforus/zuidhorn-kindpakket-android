package io.forus.kindpakket.android.kindpakket.model;

import com.google.gson.annotations.SerializedName;

public class Voucher extends ApiBase {
    public static final Voucher INVALID = null;

    @SerializedName("public_key")
    private String publicKey;

    @SerializedName("max_amount")
    private float maxAmount;

    public String getPublicKey() {
        return publicKey;
    }

    public float getMaxAmount() {
        return maxAmount;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "publicKey='" + publicKey + '\'' +
                ", maxAmount=" + maxAmount +
                '}';
    }
}
