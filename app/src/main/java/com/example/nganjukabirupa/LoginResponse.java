package com.example.nganjukabirupa;

public class LoginResponse {
    public boolean success;
    public String message;
    public String id_customer;
    public String nama_customer;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getId_customer() {
        return id_customer;
    }

    public String getNama_customer() {
        return nama_customer;
    }
}