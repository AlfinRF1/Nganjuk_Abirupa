package com.example.nganjukabirupa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    // 🔐 Registrasi akun baru
    @Headers("Content-Type: application/json")
    @POST("register.php")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // 🔐 Login manual
    @Headers("Content-Type: application/json")
    @POST("login.php")
    Call<LoginResponse> login(@Body LoginRequest request);

    // 🔐 Login Google
    @Headers("Content-Type: application/json")
    @POST("google_login.php")
    Call<LoginResponse> googleLogin(@Body GoogleLoginRequest request);

    // 📄 Ambil profil user
    @Headers("Content-Type: application/json")
    @POST("get_profile.php")
    Call<ProfileResponse> getProfile(@Body ProfileRequest request);
    @Headers("Content-Type: application/json")
    @POST("get_profile_by_email.php")
    Call<ProfileResponse> getProfileByEmail(@Body EmailRequest request);
}