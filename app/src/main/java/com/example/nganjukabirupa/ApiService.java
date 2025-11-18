package com.example.nganjukabirupa;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

    // ⚠️ Ambil detail wisata (Gson-safe)
    @GET("get_detail_wisata.php")
    Call<WisataModel> getDetailWisata(@Query("id") int id);

    // ⚠️ Ambil detail wisata raw (ResponseBody) → aman untuk string atau object
    @GET("get_detail_wisata.php")
    Call<ResponseBody> getDetailWisataRaw(@Query("id") int id);
}
