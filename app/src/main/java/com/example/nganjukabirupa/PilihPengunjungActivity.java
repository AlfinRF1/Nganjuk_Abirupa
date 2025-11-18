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

    private int idWisata, tiketDewasa, tiketAnak, asuransi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihpengunjung);

        // Ambil data dari intent
        Intent intent = getIntent();
        idWisata = intent.getIntExtra("idWisata", -1);
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
        btnSimpan = findViewById(R.id.btnSimpan);

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

        // Tombol simpan → kirim data balik ke PemesananActivity
        btnSimpan.setOnClickListener(v -> {
            if (jumlahDewasa + jumlahAnak == 0) {
                Toast.makeText(this, "Jumlah pengunjung belum diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("jumlahDewasa", jumlahDewasa);
            resultIntent.putExtra("jumlahAnak", jumlahAnak);
            setResult(RESULT_OK, resultIntent);
            finish(); // kembali ke PemesananActivity
        });
    }
}