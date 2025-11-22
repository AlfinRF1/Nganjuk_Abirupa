package com.example.nganjukabirupa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RiwayatAdapter adapter;
    private static final String TAG = "RiwayatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        recyclerView = findViewById(R.id.recyclerViewRiwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ambil id_customer dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String idCustomerStr = prefs.getString("id_customer", null);

        Log.d(TAG, "ID customer dari session: " + idCustomerStr);

        if (idCustomerStr == null) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            return;
        }

        int idCustomer;
        try {
            idCustomer = Integer.parseInt(idCustomerStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID user tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil data riwayat dari backend
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<RiwayatModel>> call = apiService.getRiwayat(idCustomer);

        call.enqueue(new Callback<List<RiwayatModel>>() {
            @Override
            public void onResponse(Call<List<RiwayatModel>> call, Response<List<RiwayatModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ✅ Log raw JSON hasil parsing Retrofit
                    Log.d("RiwayatActivity", "Raw response: " + new Gson().toJson(response.body()));

                    List<RiwayatModel> riwayatList = response.body();
                    Log.d(TAG, "Jumlah data riwayat: " + riwayatList.size());

                    if (riwayatList.isEmpty()) {
                        Toast.makeText(RiwayatActivity.this, "Belum ada riwayat transaksi", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter = new RiwayatAdapter(riwayatList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Log.e(TAG, "Response gagal: " + response.code());
                    Toast.makeText(RiwayatActivity.this, "Riwayat kosong atau gagal dimuat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RiwayatModel>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(RiwayatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onHomeClicked(View view) {
        startActivity(new Intent(RiwayatActivity.this, DashboardActivity.class));
        finish();
    }

    public void onRiwayatClicked(View view) {
        Toast.makeText(this, "Kamu sudah di halaman Riwayat", Toast.LENGTH_SHORT).show();
    }

    public void onProfileClicked(View view) {
        startActivity(new Intent(RiwayatActivity.this, ProfileActivity.class));
        finish();
    }
}