package com.example.nganjukabirupa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananActivity extends AppCompatActivity {

    private static final int REQUEST_JUMLAH = 100;

    TextView tvTotalHarga, tvLabelDewasa, tvLabelAnak, tvLabelAsuransi;
    TextView tvHargaDewasa, tvHargaAnak, tvAsuransi;
    EditText etTanggal, etNama, etTelepon;
    Button btnJumlah, btnCalendar, btnBayar;

    int jumlahDewasa = 0;
    int jumlahAnak = 0;
    int hargaDewasa = 0;
    int hargaAnak = 0;
    int tarifAsuransi = 0;

    int idWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // Inisialisasi view
        tvTotalHarga = findViewById(R.id.tvTotal);
        etTanggal = findViewById(R.id.etTanggal);
        etNama = findViewById(R.id.etNama);
        etTelepon = findViewById(R.id.etTelepon);
        btnJumlah = findViewById(R.id.btnJumlah);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnBayar = findViewById(R.id.btnBayar);

        tvLabelDewasa = findViewById(R.id.tvLabelDewasa);
        tvLabelAnak = findViewById(R.id.tvLabelAnak);
        tvLabelAsuransi = findViewById(R.id.tvLabelAsuransi);
        tvHargaDewasa = findViewById(R.id.tvHargaDewasa);
        tvHargaAnak = findViewById(R.id.tvHargaAnak);
        tvAsuransi = findViewById(R.id.tvAsuransi);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // Ambil ID wisata dari intent
        idWisata = getIntent().getIntExtra("id_wisata", -1);
        if (idWisata == -1) {
            Toast.makeText(this, "ID Wisata tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tombol pilih jumlah pengunjung
        btnJumlah.setOnClickListener(v -> {
            Intent intent = new Intent(PemesananActivity.this, PilihPengunjungActivity.class);
            intent.putExtra("idWisata", idWisata);
            intent.putExtra("tiketDewasa", hargaDewasa);
            intent.putExtra("tiketAnak", hargaAnak);
            intent.putExtra("asuransi", tarifAsuransi);
            startActivityForResult(intent, REQUEST_JUMLAH);
        });

        // Tombol pilih tanggal
        btnCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PemesananActivity.this,
                    (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                        String tanggal = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etTanggal.setText(tanggal);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        // Tombol bayar
        btnBayar.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String telepon = etTelepon.getText().toString().trim();
            String tanggalDipilih = etTanggal.getText().toString().trim();

            if (nama.isEmpty()) {
                Toast.makeText(this, "Silakan isi nama lengkap", Toast.LENGTH_SHORT).show();
                return;
            }

            if (telepon.isEmpty()) {
                Toast.makeText(this, "Silakan isi nomor telepon", Toast.LENGTH_SHORT).show();
                return;
            }

            if (telepon.length() < 11 || telepon.length() > 13) {
                Toast.makeText(this, "Nomor telepon harus 11–13 digit", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tanggalDipilih.isEmpty()) {
                Toast.makeText(this, "Silakan pilih tanggal kunjungan", Toast.LENGTH_SHORT).show();
                return;
            }

            if (jumlahDewasa + jumlahAnak == 0) {
                Toast.makeText(this, "Silakan pilih jumlah pengunjung", Toast.LENGTH_SHORT).show();
                return;
            }

            int totalTiket = (jumlahDewasa * hargaDewasa) + (jumlahAnak * hargaAnak);
            int totalAsuransi = (jumlahDewasa + jumlahAnak) * tarifAsuransi;
            int totalHarga = totalTiket + totalAsuransi;
            int jumlahPengunjung = jumlahDewasa + jumlahAnak;

            Log.d("Bayar", "Nama: " + nama);
            Log.d("Bayar", "Telepon: " + telepon);
            Log.d("Bayar", "Tanggal: " + tanggalDipilih);
            Log.d("Bayar", "Jumlah: " + jumlahPengunjung);
            Log.d("Bayar", "Total: " + totalHarga);
            Log.d("Bayar", "ID Wisata: " + idWisata);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.insertPemesanan(
                    nama,
                    telepon,
                    tanggalDipilih,
                    jumlahPengunjung,
                    totalHarga,
                    idWisata
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PemesananActivity.this, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PemesananActivity.this, KodeQRActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("telepon", telepon);
                        intent.putExtra("tanggal", tanggalDipilih);
                        intent.putExtra("jumlah", jumlahPengunjung);
                        intent.putExtra("total", totalHarga);
                        intent.putExtra("idWisata", idWisata);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PemesananActivity.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PemesananActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Ambil data harga dari backend
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getDetailWisataRaw(idWisata);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawStr = response.body().string();
                        int jsonStart = rawStr.indexOf("{");
                        if (jsonStart >= 0) {
                            rawStr = rawStr.substring(jsonStart);
                        }

                        JSONObject jsonObject = new JSONObject(rawStr);

                        hargaDewasa = jsonObject.optInt("tiketDewasa", 0);
                        hargaAnak = jsonObject.optInt("tiketAnak", 0);

                        int asuransiServer = jsonObject.optInt("asuransi", -1);
                        tarifAsuransi = (asuransiServer >= 0)
                                ? asuransiServer
                                : ((idWisata == 4 || idWisata == 5) ? 500 : 1000);

                        hitungTotalHarga();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PemesananActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PemesananActivity.this, "Gagal ambil data tiket", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PemesananActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_JUMLAH && resultCode == RESULT_OK && data != null) {
            jumlahDewasa = data.getIntExtra("jumlahDewasa", 0);
            jumlahAnak = data.getIntExtra("jumlahAnak", 0);

            btnJumlah.setText(String.format("%02d Dewasa, %02d Anak", jumlahDewasa, jumlahAnak));
            hitungTotalHarga();
        }
    }
    void hitungTotalHarga() {
        int totalTiket = (jumlahDewasa * hargaDewasa) + (jumlahAnak * hargaAnak);
        int totalAsuransi = (jumlahDewasa + jumlahAnak) * tarifAsuransi;
        int totalHarga = totalTiket + totalAsuransi;

        tvLabelDewasa.setText(jumlahDewasa + " x Rp " + hargaDewasa);
        tvLabelAnak.setText(jumlahAnak + " x Rp " + hargaAnak);
        tvLabelAsuransi.setText((jumlahDewasa + jumlahAnak) + " x Rp " + tarifAsuransi);

        tvHargaDewasa.setText("Rp " + String.format("%,d", jumlahDewasa * hargaDewasa));
        tvHargaAnak.setText("Rp " + String.format("%,d", jumlahAnak * hargaAnak));
        tvAsuransi.setText("Rp " + String.format("%,d", totalAsuransi));
        tvTotalHarga.setText("Rp " + String.format("%,d", totalHarga));

        Log.d("Pemesanan", "Dewasa: " + jumlahDewasa + ", Anak: " + jumlahAnak);
        Log.d("Pemesanan", "Harga Tiket: " + totalTiket);
        Log.d("Pemesanan", "Asuransi: " + totalAsuransi);
        Log.d("Pemesanan", "Total Harga: " + totalHarga);
    }
}