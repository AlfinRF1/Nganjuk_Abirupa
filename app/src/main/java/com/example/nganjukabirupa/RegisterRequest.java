package com.example.nganjukabirupa;

public class RegisterRequest {
    public String nama_customer;
    public String email;
    public String no_tlp;
    public String password;

    // Constructor (opsional, tapi bagus buat kejelasan)
    public RegisterRequest(String nama_customer, String email, String no_tlp, String password) {
        this.nama_customer = nama_customer;
        this.email = email;
        this.no_tlp = no_tlp;
        this.password = password;
    }

    // Constructor kosong (WAJIB untuk Gson/Retrofit)
    public RegisterRequest() {}
}