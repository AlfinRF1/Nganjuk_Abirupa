package com.example.nganjukabirupa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvNama, tvEmail;
    private ImageView imgPhoto;
    private Button btnLogout;

    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID_CUSTOMER = "id_customer";
    private static final String KEY_EMAIL_CUSTOMER = "email_customer";
    private static final String KEY_NAMA_CUSTOMER = "nama_customer";
    private static final String KEY_PHOTO_URL = "photo_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvNama = findViewById(R.id.tv_user_name);
        tvEmail = findViewById(R.id.tv_user_email);
        imgPhoto = findViewById(R.id.iv_profile_photo);
        btnLogout = findViewById(R.id.btn_logout);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String id_customer = prefs.getString(KEY_ID_CUSTOMER, null);
        String email_customer = prefs.getString(KEY_EMAIL_CUSTOMER, null);
        String nama_customer = prefs.getString(KEY_NAMA_CUSTOMER, null);
        String photo_url = prefs.getString(KEY_PHOTO_URL, null);

        // Tampilkan nama dan email langsung dari SharedPreferences
        if (nama_customer != null) {
            tvNama.setText(nama_customer);
        }
        if (email_customer != null) {
            tvEmail.setText(email_customer);
        }

        // Tampilkan foto profil dari URL
        if (photo_url != null) {
            Glide.with(this)
                    .load(photo_url)
                    .placeholder(R.drawable.default_profile_placeholder)
                    .error(R.drawable.default_profile_placeholder)
                    .into(imgPhoto);
        }

        // Ambil data dari backend (opsional, untuk update data terbaru)
        if (id_customer != null) {
            ambilDataProfilById(id_customer);
        } else if (email_customer != null) {
            ambilDataProfilByEmail(email_customer);
        } else {
            Toast.makeText(this, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void ambilDataProfilById(String id_customer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.117/NganjukAbirupa/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        ProfileRequest request = new ProfileRequest(id_customer);
        Call<ProfileResponse> call = apiService.getProfile(request);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    tvNama.setText(response.body().profile.namaCustomer);
                    tvEmail.setText(response.body().profile.emailCustomer);
                } else {
                    Toast.makeText(ProfileActivity.this, "Gagal ambil profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ambilDataProfilByEmail(String email_customer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.117/NganjukAbirupa/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        EmailRequest request = new EmailRequest(email_customer);
        Call<ProfileResponse> call = apiService.getProfileByEmail(request);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    tvNama.setText(response.body().profile.namaCustomer);
                    tvEmail.setText(response.body().profile.emailCustomer);
                } else {
                    Toast.makeText(ProfileActivity.this, "Gagal ambil profil via email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔽 Handler untuk Bottom Navigation

    public void onHomeClicked(View view) {
        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRiwayatClicked(View view) {
        Intent intent = new Intent(ProfileActivity.this, RiwayatActivity.class);
        startActivity(intent);
        finish();
    }

    public void onProfileClicked(View view) {
        Toast.makeText(this, "Kamu sudah di halaman Profil", Toast.LENGTH_SHORT).show();
    }
}