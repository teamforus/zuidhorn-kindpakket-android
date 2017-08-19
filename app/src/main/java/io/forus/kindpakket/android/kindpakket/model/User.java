package io.forus.kindpakket.android.kindpakket.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    public static final User INVALID = null;

    @SerializedName("user_id")
    private int id;
    private String name;
    private String iban;
    @SerializedName("kvk_number")
    private String kvkNumber;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String state;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public String getKvkNumber() {
        return kvkNumber;
    }
}
