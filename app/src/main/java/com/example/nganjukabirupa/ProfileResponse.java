package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("profile")
    public Profile profile;

    public class Profile {
        @SerializedName("nama_customer")
        public String nama_customer;

        @SerializedName("email_customer")
        public String email_customer;

        @SerializedName("no_tlp")
        public String no_tlp;
    }
}
