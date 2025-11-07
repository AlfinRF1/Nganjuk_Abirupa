package com.example.nganjukabirupa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashScreenActivity menampilkan logo branding dengan latar gradasi,
 * lalu transisi ke LoginActivity atau MainActivity berdasarkan status login.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler(Looper.getMainLooper());
        runnable = () -> {
            // 🔍 Cek apakah user sudah login
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

            Intent intent;
            if (isLoggedIn) {
                // ✅ Sudah login → langsung ke MainActivity
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            } else {
                // ❌ Belum login → ke LoginActivity
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.fade_out);
            finish();
        };

        handler.postDelayed(runnable, SPLASH_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Batalkan handler jika activity dihancurkan
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}