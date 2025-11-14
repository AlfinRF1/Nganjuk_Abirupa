package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("id_customer")
    public String idCustomer;
}