package com.example.yourapp; // Ganti dengan package Anda

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Import yang penting: Ubah dari DialogFragment menjadi BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PilihPengunjungBottomSheet extends BottomSheetDialogFragment {

    private ImageButton btnMinDewasa, btnPlusDewasa, btnMinAnak, btnPlusAnak;
    private TextView tvCountDewasa, tvCountAnak;
    private Button btnSimpan;

    private int countDewasa = 1;
    private int countAnak = 0;

    // Interface untuk mengirim data kembali ke Activity
    public interface PengunjungDialogListener {
        void onDataPengunjungDisimpan(int dewasa, int anak);
    }

    private PengunjungDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Mengatur listener (Activity utama Anda)
            listener = (PengunjungDialogListener) getTargetFragment();
            if (listener == null) {
                listener = (PengunjungDialogListener) getActivity();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Activity/Fragment must implement PengunjungDialogListener");
        }
        // Jika Anda ingin Bottom Sheet memiliki sudut melengkung di atas (default)
        // Anda tidak perlu memanggil setStyle di sini.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Layout yang digunakan tetap sama: dialog_pilih_pengunjung.xml
        return inflater.inflate(R.layout.dialog_pilih_pengunjung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Logika Inisialisasi Views dan Counter tetap sama ---
        btnMinDewasa = view.findViewById(R.id.btnMinDewasa);
        btnPlusDewasa = view.findViewById(R.id.btnPlusDewasa);
        tvCountDewasa = view.findViewById(R.id.tvCountDewasa);
        btnMinAnak = view.findViewById(R.id.btnMinAnak);
        btnPlusAnak = view.findViewById(R.id.btnPlusAnak);
        tvCountAnak = view.findViewById(R.id.tvCountAnak);
        btnSimpan = view.findViewById(R.id.btnSimpanPengunjung);

        updateCountViews();

        // Listener untuk tombol Dewasa (Sama seperti sebelumnya)
        btnPlusDewasa.setOnClickListener(v -> {
            countDewasa++;
            updateCountViews();
        });
        btnMinDewasa.setOnClickListener(v -> {
            if (countDewasa > 1) {
                countDewasa--;
                updateCountViews();
            }
        });

        // Listener untuk tombol Anak (Sama seperti sebelumnya)
        btnPlusAnak.setOnClickListener(v -> {
            countAnak++;
            updateCountViews();
        });
        btnMinAnak.setOnClickListener(v -> {
            if (countAnak > 0) {
                countAnak--;
                updateCountViews();
            }
        });

        // Listener tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            listener.onDataPengunjungDisimpan(countDewasa, countAnak);
            dismiss(); // Tutup bottom sheet
        });
    }

    private void updateCountViews() {
        tvCountDewasa.setText(String.format("%02d", countDewasa));
        tvCountAnak.setText(String.format("%02d", countAnak));
        btnMinDewasa.setEnabled(countDewasa > 1);
        btnMinAnak.setEnabled(countAnak > 0);
    }
}