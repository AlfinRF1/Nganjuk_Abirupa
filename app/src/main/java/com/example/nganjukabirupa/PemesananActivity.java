package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PemesananActivity extends AppCompatActivity {

    private TextView tvSriTotal, tvTralTotal, tvSedudoTotal, tvGoaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // Ambil jumlah dari intent (default 0 kalau belum dikirim)
        int jumlahDewasa = getIntent().getIntExtra("jumlahDewasa", 0);
        int jumlahAnak = getIntent().getIntExtra("jumlahAnak", 0);

        // Data wisata
        WisataModel sriTanjung = new WisataModel("Sri Tanjung Wisata Tirta", "Loceret, Nganjuk", 10000, 7000, 500);
        WisataModel tral = new WisataModel("Taman Rekreasi Anjuk Ladang", "Ploso, Nganjuk", 8000, 5000, 500);
        WisataModel sedudo = new WisataModel("Air Terjun Sedudo", "Sawahan, Nganjuk", 10000, 8000, 1000);
        WisataModel goaMargo = new WisataModel("Goa Margo Tresno", "Ngluyu, Nganjuk", 9000, 7000, 1000);

        // Hitung total
        int totalSri = HargaCalculator.hitungTotal(sriTanjung, jumlahDewasa, jumlahAnak);
        int totalTral = HargaCalculator.hitungTotal(tral, jumlahDewasa, jumlahAnak);
        int totalSedudo = HargaCalculator.hitungTotal(sedudo, jumlahDewasa, jumlahAnak);
        int totalGoa = HargaCalculator.hitungTotal(goaMargo, jumlahDewasa, jumlahAnak);

        // Log untuk debug
        Log.d("HargaSriTanjung", "Total: Rp " + totalSri);
        Log.d("HargaTral", "Total: Rp " + totalTral);
        Log.d("HargaSedudo", "Total: Rp " + totalSedudo);
        Log.d("HargaGoa", "Total: Rp " + totalGoa);

        // Tampilkan ke UI


        tvSriTotal.setText("Rp " + String.format("%,d", totalSri));
        tvTralTotal.setText("Rp " + String.format("%,d", totalTral));
        tvSedudoTotal.setText("Rp " + String.format("%,d", totalSedudo));
        tvGoaTotal.setText("Rp " + String.format("%,d", totalGoa));
    }
}