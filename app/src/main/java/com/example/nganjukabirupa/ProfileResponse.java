package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("profile")
    private Profile profile;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Profile getProfile() {
        return profile;
    }

    public static class Profile {
        @SerializedName("id_customer")
        private String idCustomer;   // opsional, kalau backend kirim

        @SerializedName("nama_customer")
        private String namaCustomer;

        @SerializedName("email_customer")
        private String emailCustomer;

        public String getIdCustomer() {
            return idCustomer;
        }

        public String getNamaCustomer() {
            return namaCustomer;
        }

        public String getEmailCustomer() {
            return emailCustomer;
        }
    }
}