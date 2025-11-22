package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QrCodeActivity extends AppCompatActivity {
    private TextView totalAmountText;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_qr_pembayaran);

        totalAmountText = findViewById(R.id.total_amount);
        backArrow = findViewById(R.id.back_arrow);

        // Ambil total harga dari intent
        String total = getIntent().getStringExtra("total");

        // Tampilkan total harga
        if (total != null && !total.isEmpty()) {
            totalAmountText.setText("Total : Rp. " + total);
        } else {
            Toast.makeText(this, "Data total tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        // Tombol kembali
        if (backArrow != null) {
            backArrow.setOnClickListener(v -> {
                Intent intent = new Intent(QrCodeActivity.this, RiwayatActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}