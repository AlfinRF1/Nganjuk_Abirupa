package com.example.nganjukabirupa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 700;
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "123456";
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Animasi frame naik
        View loginFrame = findViewById(R.id.loginFrame);
        loginFrame.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                loginFrame.getViewTreeObserver().removeOnPreDrawListener(this);
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                loginFrame.setTranslationY(screenHeight);
                loginFrame.animate()
                        .translationY(0)
                        .setDuration(ANIMATION_DURATION)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
                return true;
            }
        });

        // Styling teks registrasi (AMAN!)
        TextView registerLink = findViewById(R.id.registerLink);
        String fullText = "Belum memiliki akun? Registrasi";
        String registrasiText = "Registrasi";
        int registrasiStart = fullText.indexOf(registrasiText);

        if (registrasiStart == -1) {
            registerLink.setText(fullText);
            registerLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        } else {
            SpannableString ss = new SpannableString(fullText);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, registrasiStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            int blueColor = ContextCompat.getColor(this, R.color.gradientEnd);
            ss.setSpan(new ForegroundColorSpan(blueColor), registrasiStart, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(blueColor);
                    ds.setUnderlineText(false);
                }
            }, registrasiStart, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            registerLink.setText(ss);
            registerLink.setMovementMethod(LinkMovementMethod.getInstance());
            registerLink.setHighlightColor(Color.TRANSPARENT);
        }

        // Login logic + Auto-login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Harap isi username dan password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
                Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                // 💾 Simpan ke SharedPreferences (AUTO-LOGIN)
                SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.putString(KEY_USERNAME, username);
                editor.apply();

                // Sembunyikan keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}