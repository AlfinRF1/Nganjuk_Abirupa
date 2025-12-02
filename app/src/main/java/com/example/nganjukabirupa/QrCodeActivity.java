package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QrCodeActivity extends AppCompatActivity {
    private TextView totalAmountText;
    private ImageView backArrow, imgBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_qr_pembayaran);

        totalAmountText = findViewById(R.id.total_amount);
        backArrow = findViewById(R.id.back_arrow);
        imgBarcode = findViewById(R.id.qr_code_image);

        // Ambil data dari Intent
        int totalHarga = getIntent().getIntExtra("total", 0);
        int idWisata = getIntent().getIntExtra("idWisata", -1);

        // ✅ tampilkan total harga dari backend
        if (totalHarga > 0) {
            totalAmountText.setText("Total : Rp. " + String.format("%,d", totalHarga));
        } else {
            totalAmountText.setText("Total : Rp. -");
            Toast.makeText(this, "Data total tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        // ✅ set barcode sesuai idWisata, fallback default
        imgBarcode.setImageResource(getBarcodeDrawable(idWisata));

        // Tombol kembali → cukup finish()
        backArrow.setOnClickListener(v -> finish());
    }

    // ✅ mapping idWisata ke file drawable
    private int getBarcodeDrawable(int idWisata) {
        switch (idWisata) {
            case 12: return R.drawable.sedudo;      // sedudo.jpg
            case 13: return R.drawable.tral;        // tral.jpeg
            case 14: return R.drawable.goa;         // goa.jpeg
            case 15: return R.drawable.sritanjung;  // sritanjung.jpeg
            default: return R.drawable.barcode_default;
        }
    }
}