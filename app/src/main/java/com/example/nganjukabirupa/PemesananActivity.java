package com.example.nganjukabirupa;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananActivity extends AppCompatActivity {

    private static final int REQUEST_JUMLAH = 100;

    private TextView tvTotalHarga, tvLabelDewasa, tvLabelAnak, tvLabelAsuransi;
    private TextView tvHargaDewasa, tvHargaAnak, tvAsuransi;
    private EditText etTanggal, etNama, etTelepon;
    private Button btnJumlah, btnCalendar, btnBayar;

    private int jumlahDewasa = 0;
    private int jumlahAnak = 0;
    private int hargaDewasa = 0;
    private int hargaAnak = 0;
    private int tarifAsuransi = 0;

    private int idWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // INIT VIEW
        tvTotalHarga = findViewById(R.id.tvTotal);
        tvLabelDewasa = findViewById(R.id.tvLabelDewasa);
        tvLabelAnak = findViewById(R.id.tvLabelAnak);
        tvLabelAsuransi = findViewById(R.id.tvLabelAsuransi);
        tvHargaDewasa = findViewById(R.id.tvHargaDewasa);
        tvHargaAnak = findViewById(R.id.tvHargaAnak);
        tvAsuransi = findViewById(R.id.tvAsuransi);

        etTanggal = findViewById(R.id.etTanggal);
        etNama = findViewById(R.id.etNama);
        etTelepon = findViewById(R.id.etTelepon);

        btnJumlah = findViewById(R.id.btnJumlah);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnBayar = findViewById(R.id.btnBayar);

        // BACK BUTTON
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // GET DATA WISATA
        idWisata = getIntent().getIntExtra("id_wisata", -1);
        if (idWisata == -1) {
            Toast.makeText(this, "ID Wisata tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupCalendarPicker();
        setupJumlahButton();
        setupBayarButton();
        loadHargaWisata();
    }

    private void setupCalendarPicker() {
        btnCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PemesananActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String tanggal = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etTanggal.setText(tanggal);
                    }, year, month, day
            );
            datePickerDialog.show();
        });
    }

    private void setupJumlahButton() {
        btnJumlah.setOnClickListener(v -> {
            Intent intent = new Intent(PemesananActivity.this, PilihPengunjungActivity.class);
            intent.putExtra("idWisata", idWisata);
            intent.putExtra("tiketDewasa", hargaDewasa);
            intent.putExtra("tiketAnak", hargaAnak);
            intent.putExtra("asuransi", tarifAsuransi);
            startActivityForResult(intent, REQUEST_JUMLAH);
        });
    }

    private void setupBayarButton() {
        btnBayar.setOnClickListener(v -> {

            String nama = etNama.getText().toString().trim();
            String telepon = etTelepon.getText().toString().trim();
            String tanggalDipilih = etTanggal.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            int idCustomer = Integer.parseInt(prefs.getString("id_customer", "-1"));

            if (nama.isEmpty() || telepon.isEmpty() || tanggalDipilih.isEmpty() || jumlahDewasa + jumlahAnak == 0) {
                Toast.makeText(this, "Lengkapi semua data sebelum bayar", Toast.LENGTH_SHORT).show();
                return;
            }

            int jumlahPengunjung = jumlahDewasa + jumlahAnak;
            int totalTiket = (jumlahDewasa * hargaDewasa) + (jumlahAnak * hargaAnak);
            int totalAsuransi = jumlahPengunjung * tarifAsuransi;
            int totalHarga = totalTiket + totalAsuransi;

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Proses transaksi...");
            progress.setCancelable(false);
            progress.show();

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.insertPemesanan(
                    nama,
                    telepon,
                    tanggalDipilih,
                    String.valueOf(jumlahPengunjung),
                    String.valueOf(totalHarga),
                    String.valueOf(idWisata),
                    String.valueOf(idCustomer)
            );

            Log.d("REQ_DEBUG",
                    "nama=" + nama +
                            ", telepon=" + telepon +
                            ", tanggal=" + tanggalDipilih +
                            ", jumlah=" + jumlahPengunjung +
                            ", total=" + totalHarga +
                            ", id_wisata=" + idWisata +
                            ", id_customer=" + idCustomer
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progress.dismiss();

                    if (response.isSuccessful()) {

                        Intent intent = new Intent(PemesananActivity.this, QrCodeActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("telepon", telepon);
                        intent.putExtra("tanggal", tanggalDipilih);
                        intent.putExtra("jumlah", jumlahPengunjung);
                        intent.putExtra("total", totalHarga);
                        intent.putExtra("idWisata", idWisata);
                        startActivity(intent);
                        finish();

                    } else {
                        try {
                            Log.e("Bayar", response.errorBody() != null ? response.errorBody().string() : "Unknown error");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(PemesananActivity.this, "Gagal simpan transaksi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progress.dismiss();
                    Log.e("Bayar", "Network error", t);
                    Toast.makeText(PemesananActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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

    private void loadHargaWisata() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getDetailWisataRaw(idWisata);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawStr = response.body().string();
                        rawStr = rawStr.substring(rawStr.indexOf("{"));
                        JSONObject jsonObject = new JSONObject(rawStr);

                        hargaDewasa = jsonObject.optInt("tiketDewasa", 0);
                        hargaAnak = jsonObject.optInt("tiketAnak", 0);
                        tarifAsuransi = jsonObject.optInt("asuransi", 1000);

                        hitungTotalHarga();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PemesananActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitungTotalHarga() {
        int totalTiket = (jumlahDewasa * hargaDewasa) + (jumlahAnak * hargaAnak);
        int totalAsuransi = (jumlahDewasa + jumlahAnak) * tarifAsuransi;
        int totalHarga = totalTiket + totalAsuransi;

        tvLabelDewasa.setText(jumlahDewasa + " x Rp " + hargaDewasa);
        tvLabelAnak.setText(jumlahAnak + " x Rp " + hargaAnak);
        tvLabelAsuransi.setText((jumlahDewasa + jumlahAnak) + " x Rp " + tarifAsuransi);

        tvHargaDewasa.setText("Rp " + totalTiket);
        tvHargaAnak.setText("Rp " + (jumlahAnak * hargaAnak));
        tvAsuransi.setText("Rp " + totalAsuransi);
        tvTotalHarga.setText("Rp " + totalHarga);
    }
}
