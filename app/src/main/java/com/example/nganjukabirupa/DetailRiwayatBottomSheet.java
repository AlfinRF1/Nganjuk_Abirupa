package com.example.nganjukabirupa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DetailRiwayatBottomSheet extends BottomSheetDialogFragment {

    private String namaWisata, lokasi, idTransaksi, tanggal, status, metodePembayaran, totalHarga;

    public static DetailRiwayatBottomSheet newInstance(String namaWisata, String lokasi, String idTransaksi,
                                                       String tanggal, String status, String metodePembayaran, String totalHarga) {
        DetailRiwayatBottomSheet fragment = new DetailRiwayatBottomSheet();
        Bundle args = new Bundle();
        args.putString("nama_wisata", namaWisata);
        args.putString("lokasi", lokasi);
        args.putString("id_transaksi", idTransaksi);
        args.putString("tanggal", tanggal);
        args.putString("status", status);
        args.putString("metode_pembayaran", metodePembayaran);
        args.putString("total_harga", totalHarga);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_detailriwayat, container, false);

        if (getArguments() != null) {
            namaWisata = getArguments().getString("nama_wisata");
            lokasi = getArguments().getString("lokasi");
            idTransaksi = getArguments().getString("id_transaksi");
            tanggal = getArguments().getString("tanggal");
            status = getArguments().getString("status");
            metodePembayaran = getArguments().getString("metode_pembayaran");
            totalHarga = getArguments().getString("total_harga");
        }

        ((TextView) view.findViewById(R.id.tv_wisata_name)).setText(namaWisata);
        ((TextView) view.findViewById(R.id.tv_wisata_location)).setText(lokasi);
        ((TextView) view.findViewById(R.id.tv_transaction_id)).setText(idTransaksi);
        ((TextView) view.findViewById(R.id.tv_transaction_date)).setText(tanggal);
        ((TextView) view.findViewById(R.id.tv_transaction_status)).setText(status);
        ((TextView) view.findViewById(R.id.tv_payment_method)).setText(metodePembayaran);
        ((TextView) view.findViewById(R.id.tv_transaction_total)).setText(totalHarga);

        return view;
    }
}
