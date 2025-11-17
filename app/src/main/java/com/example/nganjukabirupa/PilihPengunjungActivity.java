package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PilihPengunjungActivity extends AppCompatActivity {

    private int jumlahDewasa = 0;
    private int jumlahAnak = 0;

    private TextView tvJumlahDewasa, tvJumlahAnak;
    private ImageButton btnPlusDewasa, btnMinusDewasa;
    private ImageButton btnPlusAnak, btnMinusAnak;
    private Button btnSimpan;
    private Spinner spinnerWisata;

    private WisataModel[] daftarWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihpengunjung);

        // Inisialisasi data wisata
        daftarWisata = new WisataModel[]{
                new WisataModel("Sri Tanjung Wisata Tirta", "Loceret, Nganjuk", 10000, 7000, 500),
                new WisataModel("Taman Rekreasi Anjuk Ladang", "Ploso, Nganjuk", 8000, 5000, 500),
                new WisataModel("Air Terjun Sedudo", "Sawahan, Nganjuk", 10000, 8000, 1000),
                new WisataModel("Goa Margo Tresno", "Ngluyu, Nganjuk", 9000, 7000, 1000)
        };

        // Inisialisasi view

        // Setup spinner
        String[] namaWisataArray = new String[daftarWisata.length];
        for (int i = 0; i < daftarWisata.length; i++) {
            namaWisataArray[i] = daftarWisata[i].getNamaWisata();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, namaWisataArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWisata.setAdapter(adapter);

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
            int selectedIndex = spinnerWisata.getSelectedItemPosition();
            WisataModel wisataDipilih = daftarWisata[selectedIndex];

            Intent intent = new Intent(PilihPengunjungActivity.this, PemesananActivity.class);
            intent.putExtra("jumlahDewasa", jumlahDewasa);
            intent.putExtra("jumlahAnak", jumlahAnak);
            intent.putExtra("namaWisata", wisataDipilih.getNamaWisata());
            intent.putExtra("lokasi", wisataDipilih.getLokasi());
            intent.putExtra("tiketDewasa", wisataDipilih.getTiketDewasa());
            intent.putExtra("tiketAnak", wisataDipilih.getTiketAnak());
            intent.putExtra("asuransi", wisataDipilih.getAsuransi());
            startActivity(intent);
        });
    }
}