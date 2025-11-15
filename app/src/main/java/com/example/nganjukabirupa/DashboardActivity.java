package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private LinearLayout searchBar;
    private TextView searchHintText;
    private View[] cards;
    private String[] namaWisata;
    private String[] lokasiWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi search bar (custom layout)
        searchBar = findViewById(R.id.searchBar);
        searchHintText = findViewById(R.id.searchHintText);

        // Tambahkan listener klik kalau mau aktifkan pencarian manual
        searchBar.setOnClickListener(v -> {
            // Contoh: buka activity pencarian atau tampilkan dialog
            // startActivity(new Intent(this, SearchActivity.class));
        });

        // Inisialisasi card & data
        cards = new View[] {
                // Tambahkan ID card view di sini, misal:
                // findViewById(R.id.cardSedudo),
                // findViewById(R.id.cardRoroKuning),
                // ...
        };

        namaWisata = new String[] {
                "Air Terjun Sedudo",
                "Roro Kuning & Situs Jendral Sudirman",
                "Taman Rekreasi",
                "Museum",
                "Pantai"
        };

        lokasiWisata = new String[] {
                "Desa Ngliman, Kec Sawahan, Kab Nganjuk",
                "Desa Bajulan, Kec Loceret, Kab Nganjuk",
                "Kota Nganjuk",
                "Kota Nganjuk",
                "Kab Nganjuk"
        };

        // Footer navigation
        findViewById(R.id.navHome).setOnClickListener(v -> { /* udah di home */ });
        findViewById(R.id.navRiwayat).setOnClickListener(v ->
                startActivity(new Intent(this, RiwayatActivity.class))
        );
        findViewById(R.id.navProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );
    }

    private void setupCardClick(int buttonId, int index) {
        findViewById(buttonId).setOnClickListener(v ->
                startActivity(new Intent(this, DetailWisataActivity.class)
                        .putExtra("nama_wisata", namaWisata[index])
                        .putExtra("lokasi", lokasiWisata[index])
                        .putExtra("gambar", getDrawableResource(index)))
        );
    }

    private int getDrawableResource(int index) {
        int[] drawables = {
                R.drawable.wisata_air_terjun_sedudo,
                R.drawable.wisata_roro_kuning,
                R.drawable.wisata_goa_margotresno,
                R.drawable.wisata_sritanjung,
                R.drawable.wisata_tral
        };
        return drawables[index];
    }

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