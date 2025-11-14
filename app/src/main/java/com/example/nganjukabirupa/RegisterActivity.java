package com.example.nganjukabirupa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID_CUSTOMER = "id_customer";
    private static final String TAG = "RegisterDebug";

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        findViewById(R.id.backArrow).setOnClickListener(v -> finish());

        btnCreateAccount.setOnClickListener(v -> {
            hideKeyboard();

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etName.setError("Harap isi Nama Lengkap");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Harap isi Email");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Format email tidak valid");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                etPhone.setError("Harap isi No.Telp");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Harap isi Kata Sandi");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Minimal 6 karakter");
                return;
            }
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Kata sandi tidak cocok");
                return;
            }

            RegisterRequest request = new RegisterRequest(name, email, phone, password);

            Gson gson = new GsonBuilder().setLenient().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://172.16.106.127/NganjukAbirupa/") // Ganti IP sesuai ipconfig
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            Call<RegisterResponse> call = apiService.register(request);

            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        try {
                            RegisterResponse body = response.body();
                            if (body != null) {
                                Log.d(TAG, "Response JSON: " + new Gson().toJson(body));
                                if (body.success) {
                                    String id_customer = body.idCustomer;

                                    SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                    prefs.edit().putString(KEY_ID_CUSTOMER, id_customer).apply();

                                    Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, body.message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                                Log.e(TAG, "Body null. Error: " + errorBody);
                                Toast.makeText(RegisterActivity.this, "Gagal parsing response", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Exception: " + e.getMessage());
                            Toast.makeText(RegisterActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                            Log.e(TAG, "Error response: " + errorBody);
                            Toast.makeText(RegisterActivity.this, "Gagal registrasi. Server error.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e(TAG, "Exception: " + e.getMessage());
                            Toast.makeText(RegisterActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.e(TAG, "Retrofit failure: " + t.getMessage());
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}