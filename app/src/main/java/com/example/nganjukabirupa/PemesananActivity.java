package com.example.nganjukabirupa;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananActivity extends AppCompatActivity {

    private TextView tvTotalHarga;
    private int jumlahDewasa = 0;
    private int jumlahAnak = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        tvTotalHarga = findViewById(R.id.tvTotal);

        // Ambil data dari Intent
        String idWisataStr = getIntent().getStringExtra("id_wisata");
        jumlahDewasa = getIntent().getIntExtra("jumlahDewasa", 0);
        jumlahAnak = getIntent().getIntExtra("jumlahAnak", 0);

        if (idWisataStr == null || idWisataStr.isEmpty()) {
            Toast.makeText(this, "ID Wisata tidak dikirim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final int idWisata;
        try {
            idWisata = Integer.parseInt(idWisataStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "ID Wisata tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Panggil API (gunakan ResponseBody untuk aman dari warning PHP)
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getDetailWisataRaw(idWisata);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawStr = response.body().string();

                        // Hilangkan semua yang bukan JSON (contoh: warning PHP)
                        int jsonStart = rawStr.indexOf("{");
                        if (jsonStart >= 0) {
                            rawStr = rawStr.substring(jsonStart);
                        }

                        JSONObject jsonObject = new JSONObject(rawStr);

                        // Ambil harga tiket
                        int hargaDewasa = jsonObject.optInt("tiketDewasa", 0);
                        int hargaAnak = jsonObject.optInt("tiketAnak", 0);

                        // Hitung total tiket
                        int totalTiket = (jumlahDewasa * hargaDewasa) + (jumlahAnak * hargaAnak);

                        // Hitung total asuransi (server optional, pakai fallback)
                        int asuransiServer = jsonObject.optInt("asuransi", -1);
                        int tarifAsuransi = (asuransiServer >= 0) ? asuransiServer : ((idWisata == 4 || idWisata == 5) ? 500 : 1000);
                        int totalAsuransi = (jumlahDewasa + jumlahAnak) * tarifAsuransi;

                        // Total keseluruhan
                        int totalHarga = totalTiket + totalAsuransi;

                        // Tampilkan
                        tvTotalHarga.setText("Rp " + String.format("%,d", totalHarga));

                        // Log debug
                        Log.d("Pemesanan", "ID Wisata: " + idWisata);
                        Log.d("Pemesanan", "Dewasa: " + jumlahDewasa + ", Anak: " + jumlahAnak);
                        Log.d("Pemesanan", "Harga Tiket: " + totalTiket);
                        Log.d("Pemesanan", "Asuransi: " + totalAsuransi);
                        Log.d("Pemesanan", "Total: " + totalHarga);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PemesananActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Gagal ambil data tiket";
                        Toast.makeText(PemesananActivity.this, errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PemesananActivity.this, "Gagal ambil data tiket", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PemesananActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
