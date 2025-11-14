package com.example.nganjukabirupa;

public class GoogleLoginRequest {
    public String nama_customer;
    public String email_customer;

    // ✅ Tambahkan constructor ini
    public GoogleLoginRequest(String nama_customer, String email_customer) {
        this.nama_customer = nama_customer;
        this.email_customer = email_customer;
    }

    // Optional: constructor kosong kalau kamu pakai Gson
    public GoogleLoginRequest() {}
}
