package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RiwayatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
    }

    public void onHomeClicked(View view) {
        startActivity(new Intent(RiwayatActivity.this, DashboardActivity.class));
        finish();
    }

    public void onRiwayatClicked(View view) {
        Toast.makeText(this, "Kamu sudah di halaman Riwayat", Toast.LENGTH_SHORT).show();
    }

    public void onProfileClicked(View view) {
        startActivity(new Intent(RiwayatActivity.this, ProfileActivity.class));
        finish();
    }
}