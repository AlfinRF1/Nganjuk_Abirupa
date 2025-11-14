package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email_customer")
    private String email;

    @SerializedName("password_customer")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}