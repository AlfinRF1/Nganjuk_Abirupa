package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PilihPengunjungActivity extends AppCompatActivity {

    private int jumlahDewasa = 0;
    private int jumlahAnak = 0;

    private TextView tvJumlahDewasa, tvJumlahAnak;
    private ImageButton btnPlusDewasa, btnMinusDewasa;
    private ImageButton btnPlusAnak, btnMinusAnak;
    private Button btnSimpan;

    private String idWisata, namaWisata, lokasi;
    private int tiketDewasa, tiketAnak, asuransi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihpengunjung);

        // Ambil data wisata dari intent
        Intent intent = getIntent();
        idWisata = intent.getStringExtra("idWisata");
        namaWisata = intent.getStringExtra("namaWisata");
        lokasi = intent.getStringExtra("lokasi");
        tiketDewasa = intent.getIntExtra("tiketDewasa", 0);
        tiketAnak = intent.getIntExtra("tiketAnak", 0);
        asuransi = intent.getIntExtra("asuransi", 0);

        // Inisialisasi view
        tvJumlahDewasa = findViewById(R.id.tvCountDewasa);
        tvJumlahAnak = findViewById(R.id.tvCountAnak);
        btnPlusDewasa = findViewById(R.id.btnPlusDewasa);
        btnMinusDewasa = findViewById(R.id.btnMinDewasa);
        btnPlusAnak = findViewById(R.id.btnPlusAnak);
        btnMinusAnak = findViewById(R.id.btnMinAnak);
        btnSimpan = findViewById(R.id.btnSimpan); // pastikan ini juga ada di XML

        // Tombol tambah/kurang dewasa
        btnPlusDewasa.setOnClickListener(v -> {
            jumlahDewasa++;
            tvJumlahDewasa.setText(String.valueOf(jumlahDewasa));
        });

        btnMinusDewasa.setOnClickListener(v -> {
            if (jumlahDewasa > 0) {
                jumlahDewasa--;
                tvJumlahDewasa.setText(String.valueOf(jumlahDewasa));
            }
        });

        // Tombol tambah/kurang anak
        btnPlusAnak.setOnClickListener(v -> {
            jumlahAnak++;
            tvJumlahAnak.setText(String.valueOf(jumlahAnak));
        });

        btnMinusAnak.setOnClickListener(v -> {
            if (jumlahAnak > 0) {
                jumlahAnak--;
                tvJumlahAnak.setText(String.valueOf(jumlahAnak));
            }
        });

        // Tombol simpan → kirim data ke PemesananActivity
        btnSimpan.setOnClickListener(v -> {
            if (jumlahDewasa + jumlahAnak == 0) {
                Toast.makeText(this, "Jumlah pengunjung belum diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent nextIntent = new Intent(PilihPengunjungActivity.this, PemesananActivity.class);
            nextIntent.putExtra("idWisata", idWisata);
            nextIntent.putExtra("namaWisata", namaWisata);
            nextIntent.putExtra("lokasi", lokasi);
            nextIntent.putExtra("tiketDewasa", tiketDewasa);
            nextIntent.putExtra("tiketAnak", tiketAnak);
            nextIntent.putExtra("asuransi", asuransi);
            nextIntent.putExtra("jumlahDewasa", jumlahDewasa);
            nextIntent.putExtra("jumlahAnak", jumlahAnak);
            startActivity(nextIntent);
        });
    }
}