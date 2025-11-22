package com.example.nganjukabirupa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailRiwayatActivity extends AppCompatActivity {

    private View cardHistory;

    // TextView references
    private TextView tvNamaWisata, tvLokasiWisata, tvIdTransaksi, tvTanggal, tvStatus, tvMetode, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_detailriwayat);

        // Animasi card
        cardHistory = findViewById(R.id.card_main_history);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        cardHistory.startAnimation(slideUp);

        // Inisialisasi TextView
        tvNamaWisata = findViewById(R.id.tv_wisata_name);
        tvLokasiWisata = findViewById(R.id.tv_wisata_location);
        tvIdTransaksi = findViewById(R.id.tv_transaction_id);
        tvTanggal = findViewById(R.id.tv_transaction_date);
        tvStatus = findViewById(R.id.tv_transaction_status);
        tvMetode = findViewById(R.id.tv_payment_method);
        tvTotal = findViewById(R.id.tv_transaction_total);

        // Ambil data dari Intent
        String namaWisata = getIntent().getStringExtra("nama_wisata");
        String lokasiWisata = getIntent().getStringExtra("lokasi");
        String idTransaksi = getIntent().getStringExtra("id_transaksi");
        String tanggal = getIntent().getStringExtra("tanggal");
        String status = getIntent().getStringExtra("status");
        String metode = getIntent().getStringExtra("metode_pembayaran");
        int totalHarga = getIntent().getIntExtra("total_harga", 0);

        // Tampilkan ke UI
        tvNamaWisata.setText(namaWisata);
        tvLokasiWisata.setText(lokasiWisata);
        tvIdTransaksi.setText(idTransaksi);
        tvTanggal.setText(tanggal);
        tvStatus.setText(status);
        tvMetode.setText(metode);
        tvTotal.setText("Rp. " + totalHarga + ",00");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        cardHistory.startAnimation(slideDown);

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
        });
    }
}