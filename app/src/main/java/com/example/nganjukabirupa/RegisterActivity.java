package com.example.nganjukabirupa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterDebug";

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnCreateAccount;

    // State untuk ketersediaan nama
    private boolean isNamaAvailable = false;

    // Retrofit instance (bisa dipakai berulang)
    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Setup view
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // Setup Retrofit sekali
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://nganjukabirupa.pbltifnganjuk.com/nganjukabirupa/apimobile/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);

        // Back arrow
        findViewById(R.id.backArrow).setOnClickListener(v -> finish());

        // Real-time name availability check
        setupNamaValidation();

        // Submit register
        btnCreateAccount.setOnClickListener(v -> handleRegister());
    }

    private void setupNamaValidation() {
        etName.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler();
            private Runnable debounceRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Batalkan pengecekan sebelumnya
                if (debounceRunnable != null) {
                    handler.removeCallbacks(debounceRunnable);
                }
                // Reset state & error
                isNamaAvailable = false;
                etName.setError(null);
                btnCreateAccount.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String rawNama = s.toString();
                // Normalisasi: trim + ganti spasi ganda jadi satu
                String normalizedNama = rawNama.trim().replaceAll("\\s+", " ");
                if (!normalizedNama.equals(rawNama)) {
                    // Hindari infinite loop
                    etName.removeTextChangedListener(this);
                    etName.setText(normalizedNama);
                    etName.setSelection(normalizedNama.length());
                    etName.addTextChangedListener(this);
                    return;
                }

                if (normalizedNama.length() < 3) {
                    if (!normalizedNama.isEmpty()) {
                        etName.setError("Minimal 3 karakter");
                    }
                    return;
                }

                // Cek ketersediaan nama (debounced)
                debounceRunnable = () -> checkNamaAvailability(normalizedNama);
                handler.postDelayed(debounceRunnable, 500);
            }
        });
    }

    private void checkNamaAvailability(String nama) {
        apiService.checkNama(nama).enqueue(new Callback<CheckNamaResponse>() {
            @Override
            public void onResponse(Call<CheckNamaResponse> call, Response<CheckNamaResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        isNamaAvailable = response.body().available;
                        if (!isNamaAvailable) {
                            etName.setError("⚠ Nama ini telah digunakan");
                            btnCreateAccount.setEnabled(false);
                        } else {
                            etName.setError(null);
                        }
                    } else {
                        etName.setError("Gagal memeriksa nama");
                        isNamaAvailable = false;
                        btnCreateAccount.setEnabled(true); // biar bisa coba lagi
                    }
                });
            }

            @Override
            public void onFailure(Call<CheckNamaResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    etName.setError("Koneksi error");
                    isNamaAvailable = false;
                    btnCreateAccount.setEnabled(true);
                    Log.e(TAG, "Nama check failed: " + t.getMessage());
                });
            }
        });
    }

    private void handleRegister() {
        hideKeyboard();

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // ✅ Validasi: nama harus tersedia
        if (!isNamaAvailable) {
            etName.setError("Nama ini telah digunakan");
            Toast.makeText(this, "Silakan pilih nama lain", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi wajib isi
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            return;
        }

        // Validasi nomor telepon: hanya angka, 11–13 digit
        if (!phone.matches("\\d+")) {
            etPhone.setError("Nomor hanya boleh angka");
            return;
        }
        if (phone.length() < 11 || phone.length() > 13) {
            etPhone.setError("Nomor telepon harus 11–13 digit");
            return;
        }

        // Validasi password
        if (password.length() < 6) {
            etPassword.setError("Minimal 6 karakter");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Kata sandi tidak cocok");
            return;
        }

        // Semua valid → kirim
        RegisterRequest request = new RegisterRequest(name, email, phone, password);

        Call<RegisterResponse> call = apiService.register(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse body = response.body();
                    if (body.success) {
                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("fromRegister", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, body.message, Toast.LENGTH_SHORT).show();
                        // Jika error karena duplikat nama (misal: race condition), refresh state
                        if (body.message != null && body.message.toLowerCase().contains("nama")) {
                            etName.setError("Nama telah digunakan");
                            isNamaAvailable = false;
                        }
                    }
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Server error";
                        Log.e(TAG, "Register failed: " + error);
                        Toast.makeText(RegisterActivity.this, "Gagal: " + error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading errorBody", e);
                        Toast.makeText(RegisterActivity.this, "Registrasi gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                Toast.makeText(RegisterActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}