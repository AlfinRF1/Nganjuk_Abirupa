package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {

    @SerializedName("id_customer")
    private String idCustomer;

    @SerializedName("nama_customer")
    private String namaCustomer;

    @SerializedName("email_customer")
    private String emailCustomer;

    @SerializedName("photo_url")
    private String photoUrl;

    // Constructor 1 parameter (ambil profil by ID)
    public ProfileRequest(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    // Constructor 4 parameter (update profil lengkap)
    public ProfileRequest(String idCustomer, String namaCustomer, String emailCustomer, String photoUrl) {
        this.idCustomer = idCustomer;
        this.namaCustomer = namaCustomer;
        this.emailCustomer = emailCustomer;
        this.photoUrl = photoUrl;
    }

    // Constructor kosong
    public ProfileRequest() {}

    public String getIdCustomer() { return idCustomer; }
    public void setIdCustomer(String idCustomer) { this.idCustomer = idCustomer; }

    public String getNamaCustomer() { return namaCustomer; }
    public void setNamaCustomer(String namaCustomer) { this.namaCustomer = namaCustomer; }

    public String getEmailCustomer() { return emailCustomer; }
    public void setEmailCustomer(String emailCustomer) { this.emailCustomer = emailCustomer; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}