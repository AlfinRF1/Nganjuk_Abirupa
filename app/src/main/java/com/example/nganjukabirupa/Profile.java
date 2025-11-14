package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("nama_customer")
    public String namaCustomer;

    @SerializedName("email_customer")
    public String emailCustomer;

    @SerializedName("no_tlp")
    public String noTelp;
}