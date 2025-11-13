package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailWisataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        // Ambil data dari intent
        String nama = getIntent().getStringExtra("nama_wisata");
        String lokasi = getIntent().getStringExtra("lokasi");
        int gambar = getIntent().getIntExtra("gambar", 0);
        String deskripsi = getIntent().getStringExtra("deskripsi") != null ?
                getIntent().getStringExtra("deskripsi") :
                "Belum ada deskripsi.";
        String tiket = getIntent().getStringExtra("tiket") != null ?
                getIntent().getStringExtra("tiket") :
                "Rp 10.000 - Rp 18.000";
        String fasilitas = getIntent().getStringExtra("fasilitas") != null ?
                getIntent().getStringExtra("fasilitas") :
                "Warung Makan, Parkir, Toilet, Kamar Shalat, Kolam Ibuatan, Jembatan, Pendopo, Foto souvenir, dan Mushola";

        // Set data ke layout
        ((TextView) findViewById(R.id.tvJudulWisata)).setText(nama);
        ((TextView) findViewById(R.id.tvNamaWisata)).setText(nama);
        ((TextView) findViewById(R.id.tvLokasiWisata)).setText(lokasi);
        ((ImageView) findViewById(R.id.ivGambarWisata)).setImageResource(gambar);
        ((TextView) findViewById(R.id.tvDeskripsi)).setText(deskripsi);
        ((TextView) findViewById(R.id.tvTiket)).setText(tiket);
        ((TextView) findViewById(R.id.tvFasilitas)).setText(fasilitas);

        // Tombol Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Tombol Pesan Sekarang
        findViewById(R.id.btnPesanSekarang).setOnClickListener(v ->
                startActivity(new Intent(this, PemesananActivity.class)
                        .putExtra("nama_wisata", nama)
                        .putExtra("lokasi", lokasi)
                        .putExtra("gambar", gambar))
        );
    }
}