package com.example.nganjukabirupa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvNama, tvEmail;
    private ImageView imgPhoto;
    private Button btnLogout;

    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID_CUSTOMER = "id_customer";
    private static final String KEY_EMAIL_CUSTOMER = "email_customer";
    private static final String KEY_NAMA_CUSTOMER = "nama_customer";
    private static final String KEY_PHOTO_URL = "photo_url";

    private ActivityResultLauncher<Intent> galleryLauncher;
    private String id_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvNama = findViewById(R.id.tv_user_name);
        tvEmail = findViewById(R.id.tv_user_email);
        imgPhoto = findViewById(R.id.iv_profile_photo);
        btnLogout = findViewById(R.id.btn_logout);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        id_customer = prefs.getString(KEY_ID_CUSTOMER, null);
        String email_customer = prefs.getString(KEY_EMAIL_CUSTOMER, null);
        String nama_customer = prefs.getString(KEY_NAMA_CUSTOMER, null);
        String photo_url = prefs.getString(KEY_PHOTO_URL, null);

        tvNama.setText(nama_customer != null ? nama_customer : "User");
        tvEmail.setText(email_customer != null ? email_customer : "-");

        if (photo_url != null && !photo_url.isEmpty()) {
            Glide.with(this)
                    .load(photo_url)
                    .placeholder(R.drawable.default_profile_placeholder)
                    .error(R.drawable.default_profile_placeholder)
                    .into(imgPhoto);
        } else {
            imgPhoto.setImageResource(R.drawable.default_profile_placeholder);
        }

        // Launcher galeri
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
                            UCrop.of(imageUri, destinationUri)
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(500, 500)
                                    .start(ProfileActivity.this);
                        }
                    }
                }
        );

        imgPhoto.setOnClickListener(v -> showFotoOptionsDialog());

        imgPhoto.setOnLongClickListener(v -> {
            showPreviewDialog();
            return true;
        });

        if (id_customer != null) {
            ambilDataProfilById(id_customer);
        }

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void showFotoOptionsDialog() {
        String[] options = {"Ganti Foto", "Hapus Foto"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan Foto Profil");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) openGallery();
            else if (which == 1) deleteFotoProfile();
        });
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private String getRealPathFromUri(Context context, Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) { e.printStackTrace();
            } finally { if (cursor != null) cursor.close(); }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    private void uploadImageToServer(Uri imageUri, String idCustomer) {
        String filePath = getRealPathFromUri(this, imageUri);
        if (filePath == null) {
            Toast.makeText(this, "Gagal mendapatkan file path", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);

        // 1. RequestBody untuk id_customer
        RequestBody idBody = RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), idCustomer
        );

        // 2. RequestBody untuk foto
        RequestBody requestFile = RequestBody.create(
                okhttp3.MediaType.parse("image/*"), file
        );
        MultipartBody.Part fotoPart = MultipartBody.Part.createFormData(
                "foto", file.getName(), requestFile
        );

        // 3. Retrofit call
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = api.updateFoto(idBody, fotoPart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Foto berhasil diupdate", Toast.LENGTH_SHORT).show();
                    ambilDataProfilById(idCustomer);
                } else {
                    Toast.makeText(ProfileActivity.this, "Gagal update foto", Toast.LENGTH_SHORT).show();
                    try { Log.e("UPLOAD_ERROR", response.errorBody().string()); } catch (Exception e){ e.printStackTrace();}
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFotoProfile() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.deleteFoto(Integer.parseInt(id_customer)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        if (json.optString("status").equals("success")) {
                            Toast.makeText(ProfileActivity.this, "Foto berhasil dihapus", Toast.LENGTH_SHORT).show();
                            imgPhoto.setImageResource(R.drawable.default_profile_placeholder);
                            ambilDataProfilById(id_customer);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Gagal hapus: " + json.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ambilDataProfilById(String idCustomer) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService api = retrofit.create(ApiService.class);
        ProfileRequest request = new ProfileRequest(idCustomer);

        api.getProfile(request).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse.Profile profile = response.body().getProfile();
                    if (profile != null) {
                        String nama = profile.getNamaCustomer() != null ? profile.getNamaCustomer() : "";
                        String email = profile.getEmailCustomer() != null ? profile.getEmailCustomer() : "";
                        tvNama.setText(nama);
                        tvEmail.setText(email);

                        String fotoPath = profile.getFoto();
                        String fotoUrl = (fotoPath == null || fotoPath.isEmpty()) ? "" :
                                (fotoPath.startsWith("http") ? fotoPath : ApiClient.BASE_URL + fotoPath);

                        // ✅ cek lifecycle sebelum Glide
                        if (!isFinishing() && !isDestroyed()) {
                            Glide.with(ProfileActivity.this)
                                    .load(fotoUrl)
                                    .placeholder(R.drawable.default_profile_placeholder)
                                    .error(R.drawable.default_profile_placeholder)
                                    .into(imgPhoto);
                        }

                        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                        prefs.edit()
                                .putString(KEY_NAMA_CUSTOMER, nama)
                                .putString(KEY_EMAIL_CUSTOMER, email)
                                .putString(KEY_PHOTO_URL, fotoUrl)
                                .apply();
                    }
                } else {
                    try { Log.e("PROFILE_ERROR", response.errorBody().string()); } catch (Exception e){ e.printStackTrace();}
                    if (!isFinishing() && !isDestroyed()) {
                        Toast.makeText(ProfileActivity.this, "Response tidak valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(ProfileActivity.this, "Gagal ambil profil: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                imgPhoto.setImageURI(resultUri);
                uploadImageToServer(resultUri, id_customer);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Crop error: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPreviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View previewView = getLayoutInflater().inflate(R.layout.dialog_preview_photo, null);
        ImageView previewImage = previewView.findViewById(R.id.preview_image);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String photoUrl = prefs.getString(KEY_PHOTO_URL, null);

        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.default_profile_placeholder)
                    .error(R.drawable.default_profile_placeholder)
                    .into(previewImage);
        } else {
            previewImage.setImageResource(R.drawable.default_profile_placeholder);
        }

        builder.setView(previewView);
        builder.setPositiveButton("Tutup", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Footer navigation
    public void onHomeClicked(View view) {
        startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
        finish();
    }

    public void onRiwayatClicked(View view) {
        startActivity(new Intent(ProfileActivity.this, RiwayatActivity.class));
        finish();
    }

    public void onProfileClicked(View view) {
        // already in profile
    }
}
