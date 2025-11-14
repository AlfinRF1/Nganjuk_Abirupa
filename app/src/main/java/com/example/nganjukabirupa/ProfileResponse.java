package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("profile")
    public Profile profile;

    public static class Profile {
        @SerializedName("namaCustomer")
        public String namaCustomer;

        @SerializedName("emailCustomer")
        public String emailCustomer;
    }
}