package com.example.nganjukabirupa;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PilihPengunjungDialog extends DialogFragment {

    private ImageButton btnMinDewasa, btnPlusDewasa, btnMinAnak, btnPlusAnak;
    private TextView tvCountDewasa, tvCountAnak;
    private Button btnSimpan;

    private int countDewasa = 1; // Default 1 Dewasa
    private int countAnak = 0;   // Default 0 Anak

    // Interface untuk mengirim data kembali ke Activity
    public interface PengunjungDialogListener {
        void onDataPengunjungDisimpan(int dewasa, int anak);
    }

    private PengunjungDialogListener listener;

    // Pastikan Activity yang memanggil dialog mengimplementasikan listener ini
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (PengunjungDialogListener) getTargetFragment();
            if (listener == null) {
                listener = (PengunjungDialogListener) getActivity();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Activity/Fragment must implement PengunjungDialogListener");
        }
        // Style dialog agar fullscreen/sesuai layar, jika diperlukan.
        // Anda bisa mengabaikan ini jika menggunakan style dialog default.
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Hubungkan dengan layout XML dialog_pilih_pengunjung.xml
        return inflater.inflate(R.layout.activity_pilihpengunjung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi Views
        btnMinDewasa = view.findViewById(R.id.btnMinDewasa);
        btnPlusDewasa = view.findViewById(R.id.btnPlusDewasa);
        tvCountDewasa = view.findViewById(R.id.tvCountDewasa);

        btnMinAnak = view.findViewById(R.id.btnMinAnak);
        btnPlusAnak = view.findViewById(R.id.btnPlusAnak);
        tvCountAnak = view.findViewById(R.id.tvCountAnak);

        btnSimpan = view.findViewById(R.id.btnSimpanPengunjung);

        // Perbarui tampilan awal
        updateCountViews();

        // Listener untuk tombol Dewasa
        btnPlusDewasa.setOnClickListener(v -> {
            countDewasa++;
            updateCountViews();
        });

        btnMinDewasa.setOnClickListener(v -> {
            if (countDewasa > 1) { // Batasan minimum 1 Dewasa
                countDewasa--;
                updateCountViews();
            }
        });

        // Listener untuk tombol Anak
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
            // Kirim data ke Activity
            listener.onDataPengunjungDisimpan(countDewasa, countAnak);
            dismiss(); // Tutup dialog
        });
    }

    private void updateCountViews() {
        // Menggunakan format string "01", "02", dst.
        tvCountDewasa.setText(String.format("%02d", countDewasa));
        tvCountAnak.setText(String.format("%02d", countAnak));

        // Opsional: Disabled tombol minus jika mencapai batas minimum
        btnMinDewasa.setEnabled(countDewasa > 1);
        btnMinAnak.setEnabled(countAnak > 0);
    }
}