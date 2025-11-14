package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {

    @SerializedName("id_customer")
    public String idCustomer;

    public ProfileRequest(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    // Optional: diperlukan oleh Gson untuk deserialisasi otomatis
    public ProfileRequest() {}
}