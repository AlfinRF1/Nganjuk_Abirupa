package com.example.nganjukabirupa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register.php")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("logincustomer.php")
    Call<LoginResponse> login(@Body LoginRequest request);
}

class RegisterRequest {
    public String nama_customer;
    public String email;
    public String no_tlp;
    public String password;
}

class LoginRequest {
    public String email;
    public String password;
}

class LoginResponse {
    private boolean success;
    private String message;
    private Customer customer;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Customer getCustomer() { return customer; }
}