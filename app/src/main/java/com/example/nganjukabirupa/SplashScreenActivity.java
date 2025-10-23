package com.example.nganjukabirupa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashScreenActivity menampilkan logo branding dengan latar gradasi,
 * lalu transisi ke LoginActivity setelah delay 2 detik.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // Delay dalam milidetik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Jalankan transisi ke LoginActivity setelah delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.fade_out); // Animasi transisi
            finish(); // Tutup SplashScreen agar tidak bisa kembali
        }, SPLASH_DELAY);
    }
}