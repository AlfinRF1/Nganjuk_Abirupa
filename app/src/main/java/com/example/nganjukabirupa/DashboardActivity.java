package com.example.nganjukabirupa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private LinearLayout searchBar;
    private EditText searchInput;
    private View[] cards;
    private int[] idWisata;          // pakai int, bukan String
    private String[] namaWisata;
    private String[] lokasiWisata;

    private TextView tvWelcome; // untuk menampilkan nama user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Ambil nama user dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String namaUser = prefs.getString("nama_customer", "Nama Kamu");

        // Set ke TextView di dashboard
        tvWelcome = findViewById(R.id.tvWelcome);
        if (tvWelcome != null) {
            tvWelcome.setText("Selamat Datang, " + namaUser + "!");
        }

        // Search bar
        searchBar = findViewById(R.id.searchBar);
        searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCards(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // Cards
        cards = new View[] {
                findViewById(R.id.cardWisata1),
                findViewById(R.id.cardWisata2),
                findViewById(R.id.cardWisata3),
                findViewById(R.id.cardWisata4),
                findViewById(R.id.cardWisata5)
        };

        // ID wisata sesuai backend
        idWisata = new int[] {1, 2, 3, 4, 5};

        // Nama & lokasi
        namaWisata = new String[] {
                "Air Terjun Sedudo",
                "Roro Kuning & Situs Jendral Sudirman",
                "Goa Margotresno",
                "Sri Tanjung",
                "Tral"
        };

        lokasiWisata = new String[] {
                "Desa Ngliman, Kec Sawahan, Kab Nganjuk",
                "Desa Bajulan, Kec Loceret, Kab Nganjuk",
                "Desa Bajulan, Kec Loceret, Kab Nganjuk",
                "Kota Nganjuk",
                "Kab Nganjuk"
        };

        // Set listener untuk tiap card & tombol
        for (int i = 0; i < cards.length; i++) {
            final int index = i;

            // Klik area CardView
            cards[i].setOnClickListener(v -> openDetail(index));

            // Klik Button “Selengkapnya”
            Button btnDetail = cards[i].findViewById(getResources().getIdentifier(
                    "btnDetail" + (i + 1), "id", getPackageName()));
            if (btnDetail != null) {
                btnDetail.setFocusable(false);
                btnDetail.setFocusableInTouchMode(false);
                btnDetail.setOnClickListener(v -> openDetail(index));
            }
        }

        // Footer navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {});
        findViewById(R.id.navRiwayat).setOnClickListener(v ->
                startActivity(new Intent(this, RiwayatActivity.class)));
        findViewById(R.id.navProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }

    // Fungsi buka detail wisata
    private void openDetail(int index) {
            Log.d("Dashboard", "Klik card index: " + index + ", id_wisata: " + idWisata[index]);
        Intent intent = null;
        switch (idWisata[index]) {
            case 1: intent = new Intent(this, DetailSedudo.class); break;
            case 2: intent = new Intent(this, DetailRoro.class); break;
            case 3: intent = new Intent(this, DetailGoa.class); break;
            case 4: intent = new Intent(this, DetailSri.class); break;
            case 5: intent = new Intent(this, DetailTral.class); break;
        }
        if (intent != null) {
            // ✅ kirim ID sebagai int
            intent.putExtra("id_wisata", idWisata[index]);
            intent.putExtra("nama_wisata", namaWisata[index]);
            intent.putExtra("lokasi", lokasiWisata[index]);
            startActivity(intent);
        }
    }

    // Filter search
    private void filterCards(String query) {
        String q = query.toLowerCase().trim();
        for (int i = 0; i < cards.length; i++) {
            boolean match = q.isEmpty() ||
                    namaWisata[i].toLowerCase().contains(q) ||
                    lokasiWisata[i].toLowerCase().contains(q);
            cards[i].setVisibility(match ? View.VISIBLE : View.GONE);
        }
    }
}