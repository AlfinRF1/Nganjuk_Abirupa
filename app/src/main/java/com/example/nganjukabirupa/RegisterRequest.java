package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("nama_customer")
    private String nama_customer;

    @SerializedName("email")
    private String email;

    @SerializedName("no_tlp")
    private String no_tlp;

    @SerializedName("password")
    private String password;

    // Constructor, getters, setters
}