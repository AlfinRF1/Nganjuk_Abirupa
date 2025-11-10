package com.example.nganjukabirupa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("logincustomer.php")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("register.php")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}

class LoginRequest {
    public String email;
    public String password;
}