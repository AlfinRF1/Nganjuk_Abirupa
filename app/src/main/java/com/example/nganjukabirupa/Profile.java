package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("id_customer")
    private String idCustomer;

    @SerializedName("nama_customer")
    private String namaCustomer;

    @SerializedName("email_customer")
    private String emailCustomer;

    @SerializedName("no_tlp")
    private String noTelp;

    @SerializedName("foto")
    private String foto;

    // Getter
    public String getIdCustomer() {
        return idCustomer;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public String getEmailCustomer() {
        return emailCustomer;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getFoto() {
        return foto;   // ← getter foto
    }
}
