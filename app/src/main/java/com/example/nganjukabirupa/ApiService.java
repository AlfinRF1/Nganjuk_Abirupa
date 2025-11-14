package com.example.nganjukabirupa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("logincustomer.php")
    Call<LoginResponse> loginCustomer(@Body LoginRequest request);

    @Headers("Content-Type: application/json")
    @POST("register.php")
    Call<RegisterResponse> registerCustomer(@Body RegisterRequest request);

    @POST("google_login.php")
    Call<GenericResponse> sendGoogleUser(@Body GoogleUserRequest request);
}