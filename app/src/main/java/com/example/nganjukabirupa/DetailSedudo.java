package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSedudo extends AppCompatActivity {

    private ImageView imgHeader;
    private TextView tvNamaWisata, tvLokasi, tvDeskripsi, tvHargaTiket, tvFasilitas;
    private Button btnPesan;
    private ImageButton btnBack;

    private int hargaDewasa = 0;
    private int hargaAnak = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sedudo);

        // Inisialisasi view
        imgHeader = findViewById(R.id.imgHeader);
        tvNamaWisata = findViewById(R.id.tvNamaWisata);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvHargaTiket = findViewById(R.id.tvHargaTiket);
        tvFasilitas = findViewById(R.id.tvFasilitas);
        btnPesan = findViewById(R.id.btnPesan);
        btnBack = findViewById(R.id.btnBack);

        // Ambil data dari backend
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<WisataModel> call = apiService.getDetailWisata(1);

        call.enqueue(new Callback<WisataModel>() {
            @Override
            public void onResponse(Call<WisataModel> call, Response<WisataModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WisataModel data = response.body();

                    tvNamaWisata.setText(data.getNamaWisata());
                    tvLokasi.setText(data.getLokasi());

                    tvDeskripsi.setText(data.getDeskripsi() != null ? data.getDeskripsi() : "Deskripsi belum tersedia di database");
                    tvFasilitas.setText(data.getFasilitas() != null ? data.getFasilitas() : "Fasilitas belum tersedia di database");

                    tvHargaTiket.setText("Dewasa: Rp " + data.getTiketDewasa() + "\nAnak-anak: Rp " + data.getTiketAnak());

                    hargaDewasa = data.getTiketDewasa();
                    hargaAnak = data.getTiketAnak();

                    Glide.with(DetailSedudo.this)
                            .load(R.drawable.wisata_air_terjun_sedudo)
                            .error(R.drawable.wisata_air_terjun_sedudo)
                            .into(imgHeader);
                } else {
                    Toast.makeText(DetailSedudo.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WisataModel> call, Throwable t) {
                Toast.makeText(DetailSedudo.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Pesan
        btnPesan.setOnClickListener(v -> {
            Intent intent = new Intent(this, PemesananActivity.class);
            intent.putExtra("id_wisata", "sedudo");
            intent.putExtra("hargaDewasa", hargaDewasa);
            intent.putExtra("hargaAnak", hargaAnak);
            intent.putExtra("jumlahDewasa", 0);
            intent.putExtra("jumlahAnak", 0);
            startActivity(intent);
        });

        // Tombol Back
        btnBack = findViewById(R.id.btnBack);
        btnBack.bringToFront(); // ini penting biar bisa diklik
        btnBack.setOnClickListener(v -> finish());

    }
}