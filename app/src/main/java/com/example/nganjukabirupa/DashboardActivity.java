package com.example.nganjukabirupa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerWisata;
    private WisataAdapter adapter;
    private List<WisataModel> wisataList = new ArrayList<>();
    private EditText searchInput;
    private TextView tvWelcome;
    private ShimmerFrameLayout shimmerLayout; // ✅ shimmer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Ambil nama user dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String namaUser = prefs.getString("nama_customer", "Nama Kamu");

        tvWelcome = findViewById(R.id.tvWelcome);
        if (tvWelcome != null) {
            tvWelcome.setText("Selamat Datang, " + namaUser + "!");
        }

        // Setup Shimmer + RecyclerView
        shimmerLayout = findViewById(R.id.shimmerLayout);
        shimmerLayout.startShimmer(); // mulai shimmer

        recyclerWisata = findViewById(R.id.recyclerWisata);
        recyclerWisata.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WisataAdapter(DashboardActivity.this, wisataList);
        recyclerWisata.setAdapter(adapter);

        // Panggil API
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getAllWisata().enqueue(new Callback<WisataResponse>() {
            @Override
            public void onResponse(Call<WisataResponse> call, Response<WisataResponse> response) {
                shimmerLayout.stopShimmer(); // ✅ stop shimmer
                shimmerLayout.setVisibility(View.GONE); // sembunyikan shimmer
                recyclerWisata.setVisibility(View.VISIBLE); // tampilkan data

                if (response.isSuccessful() && response.body() != null) {
                    wisataList.clear();
                    wisataList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    Log.d("Dashboard", "Jumlah data: " + wisataList.size());
                } else {
                    Log.e("Dashboard", "Response gagal / kosong");
                }
            }

            @Override
            public void onFailure(Call<WisataResponse> call, Throwable t) {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerWisata.setVisibility(View.VISIBLE);
                Log.e("Dashboard", "Error: " + t.getMessage());
            }
        });

        // Search filter
        searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) adapter.getFilter().filter(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // Footer navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {});
        findViewById(R.id.navRiwayat).setOnClickListener(v ->
                startActivity(new Intent(this, RiwayatActivity.class)));
        findViewById(R.id.navProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }
}