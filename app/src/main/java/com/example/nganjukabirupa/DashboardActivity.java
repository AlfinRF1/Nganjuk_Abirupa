package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private SearchView searchView;
    private View[] cards;
    private String[] namaWisata;
    private String[] lokasiWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi search bar
        searchView = findViewById(R.id.searchBar);
        searchView.setQueryHint("Mau Liburan Kemana?");
        searchView.setIconifiedByDefault(false); // biar selalu terbuka

        // Inisialisasi card & data
        cards = new View[] {
                findViewById(R.id.card1),
                findViewById(R.id.card2),
                findViewById(R.id.card3),
                findViewById(R.id.card4),
                findViewById(R.id.card5)
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

        // Listener pencarian
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCards(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCards(newText);
                return true;
            }
        });

        // Handle klik card
        setupCardClick(R.id.btnDetail1, 0);
        setupCardClick(R.id.btnDetail2, 1);
        setupCardClick(R.id.btnDetail3, 2);
        setupCardClick(R.id.btnDetail4, 3);
        setupCardClick(R.id.btnDetail5, 4);

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
                R.drawable.wisata_taman_rekreasi,
                R.drawable.wisata_museum,
                R.drawable.wisata_pantai
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