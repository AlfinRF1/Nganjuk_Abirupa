package com.example.nganjukabirupa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * LoginActivity menampilkan form login dengan animasi frame putih naik dari bawah.
 * Tampilan ini muncul setelah splash screen selesai.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 700; // durasi animasi dalam milidetik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Animasi frame login naik dari bawah
        View loginFrame = findViewById(R.id.loginFrame);
        loginFrame.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                loginFrame.getViewTreeObserver().removeOnPreDrawListener(this);
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                loginFrame.setTranslationY(screenHeight);
                Interpolator interpolator = new AccelerateDecelerateInterpolator();
                loginFrame.animate()
                        .translationY(0)
                        .setDuration(ANIMATION_DURATION)
                        .setInterpolator(interpolator)
                        .start();
                return true;
            }
        });

        // Styling teks "Belum memiliki akun? Registrasi"
        TextView registerLink = findViewById(R.id.registerLink);
        SpannableString ss = new SpannableString("Belum memiliki akun? Registrasi");

// Warna hitam untuk "Belum memiliki akun?"
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
        ss.setSpan(blackSpan, 0, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // hitam sampai sebelum "R"

// Warna biru untuk "Registrasi"
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(
                ContextCompat.getColor(getApplicationContext(), R.color.gradientEnd));
        ss.setSpan(blueSpan, 21, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // biru dari "R"

// Klikable span untuk "Registrasi"
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Arahkan ke activity registrasi
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.gradientEnd)); // pastikan tetap biru
                ds.setUnderlineText(false); // kalau mau underline, ubah jadi true
            }
        };
        ss.setSpan(clickableSpan, 21, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // klikable dari "R"

        registerLink.setText(ss);
        registerLink.setMovementMethod(LinkMovementMethod.getInstance());
        registerLink.setHighlightColor(Color.TRANSPARENT); // matikan highlight ungu
    }}// }