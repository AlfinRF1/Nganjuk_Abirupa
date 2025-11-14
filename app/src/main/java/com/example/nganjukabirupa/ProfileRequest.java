package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {
    @SerializedName("id_customer")
    private String id_customer;

    public ProfileRequest(String id_customer) {
        this.id_customer = id_customer;
    }
}
