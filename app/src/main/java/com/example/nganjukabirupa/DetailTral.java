package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTral extends AppCompatActivity {

    private ImageView imgHeader;
    private TextView tvNamaWisata, tvLokasi, tvDeskripsi, tvHargaTiket, tvFasilitas;
    private Button btnPesan;
    private ImageButton btnBack;

    private int hargaDewasa = 0;
    private int hargaAnak = 0;
    private int idWisata = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tral); // layout khusus Tral

        // Inisialisasi view
        imgHeader = findViewById(R.id.imgHeader);
        tvNamaWisata = findViewById(R.id.tvNamaWisata);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvHargaTiket = findViewById(R.id.tvHargaTiket);
        tvFasilitas = findViewById(R.id.tvFasilitas);
        btnPesan = findViewById(R.id.btnPesan);
        btnBack = findViewById(R.id.btnBack);

        // Ambil id_wisata dari Intent
        idWisata = getIntent().getIntExtra("id_wisata", -1);

        if (idWisata == -1) {
            Toast.makeText(this, "ID wisata tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ambil data dari backend sesuai id_wisata
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<WisataModel> call = apiService.getDetailWisata(idWisata);

        call.enqueue(new Callback<WisataModel>() {
            @Override
            public void onResponse(Call<WisataModel> call, Response<WisataModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WisataModel data = response.body();

                    tvNamaWisata.setText(data.getNamaWisata());
                    tvLokasi.setText(data.getLokasi());
                    tvDeskripsi.setText(data.getDeskripsi() != null ? data.getDeskripsi() : "Deskripsi belum tersedia");
                    tvFasilitas.setText(data.getFasilitas() != null ? data.getFasilitas() : "Fasilitas belum tersedia");
                    tvHargaTiket.setText("Dewasa: Rp " + data.getTiketDewasa() + "\nAnak-anak: Rp " + data.getTiketAnak());

                    hargaDewasa = data.getTiketDewasa();
                    hargaAnak = data.getTiketAnak();

                    // ✅ langsung set resource tanpa Glide
                    int imageResId = getDrawableForWisata(idWisata);
                    imgHeader.setImageResource(imageResId);

                } else {
                    Toast.makeText(DetailTral.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WisataModel> call, Throwable t) {
                Toast.makeText(DetailTral.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Pesan
        btnPesan.setOnClickListener(v -> {
            Intent intent = new Intent(this, PemesananActivity.class);
            intent.putExtra("id_wisata", idWisata);
            intent.putExtra("hargaDewasa", hargaDewasa);
            intent.putExtra("hargaAnak", hargaAnak);
            intent.putExtra("jumlahDewasa", 0);
            intent.putExtra("jumlahAnak", 0);
            startActivity(intent);
        });

        // Tombol Back
        btnBack.setOnClickListener(v -> finish());
    }

    // ✅ Mapping gambar berdasarkan ID wisata
    private int getDrawableForWisata(int idWisata) {
        switch (idWisata) {
            case 1: return R.drawable.wisata_air_terjun_sedudo;
            case 2: return R.drawable.wisata_roro_kuning;
            case 3: return R.drawable.wisata_goa_margotresno;
            case 4: return R.drawable.wisata_sritanjung;
            case 5: return R.drawable.wisata_tral;
            default: return R.drawable.default_wisata;
        }
    }
}