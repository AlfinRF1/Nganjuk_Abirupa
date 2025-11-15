package com.example.nganjukabirupa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_GOOGLE_SIGN_IN = 101;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID_CUSTOMER = "id_customer";
    private static final String KEY_NAMA_CUSTOMER = "nama_customer";
    private static final String KEY_EMAIL_CUSTOMER = "email_customer";
    private static final String KEY_PHOTO_URL = "photo_url";

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private SignInButton googleSignInButton;
    private TextView tvRegisterLink;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        ImageView ivTogglePassword = findViewById(R.id.iv_toggle_password);
        final boolean[] isPasswordVisible = {false};

        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                // Sembunyikan password
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye);
            } else {
                // Tampilkan password
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            // Geser kursor ke akhir teks
            etPassword.setSelection(etPassword.getText().length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });
        btnLogin = findViewById(R.id.btn_login);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(KEY_ID_CUSTOMER) || prefs.contains(KEY_NAMA_CUSTOMER)) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            String nama = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nama.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama dan password wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(nama, password);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().success) {
                        saveSession(
                                response.body().id_customer,
                                response.body().nama_customer,
                                response.body().email_customer,
                                null
                        );
                        Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        String msg = response.body() != null ? response.body().message : "Login gagal";
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Gagal koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        googleSignInButton.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            });
        });

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In gagal: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail() != null ? user.getEmail() : "user@example.com";
                            String name = user.getDisplayName() != null ? user.getDisplayName() : "User";
                            String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;

                            sendUserToBackend(email, name, photoUrl);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Autentikasi gagal.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendUserToBackend(String email, String name, String photoUrl) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        GoogleLoginRequest request = new GoogleLoginRequest(name, email);
        apiService.googleLogin(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    saveSession(
                            response.body().id_customer,
                            response.body().nama_customer != null ? response.body().nama_customer : name,
                            response.body().email_customer != null ? response.body().email_customer : email,
                            photoUrl
                    );
                    Toast.makeText(LoginActivity.this, "Login Google berhasil!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    String msg = response.body() != null ? response.body().message : "Login gagal";
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSession(String id_customer, String nama, String email, String photoUrl) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (id_customer != null) editor.putString(KEY_ID_CUSTOMER, id_customer);
        if (nama != null) editor.putString(KEY_NAMA_CUSTOMER, nama);
        if (email != null) editor.putString(KEY_EMAIL_CUSTOMER, email);
        if (photoUrl != null) editor.putString(KEY_PHOTO_URL, photoUrl);
        editor.apply();
    }
}