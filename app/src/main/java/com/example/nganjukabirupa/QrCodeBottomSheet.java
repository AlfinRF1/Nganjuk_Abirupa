package com.example.nganjukabirupa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QrCodeBottomSheet extends BottomSheetDialogFragment {

    private String totalAmount;

    public static QrCodeBottomSheet newInstance(String totalAmount) {
        QrCodeBottomSheet fragment = new QrCodeBottomSheet();
        Bundle args = new Bundle();
        args.putString("total_amount", totalAmount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_qr_pembayaran, container, false);

        if (getArguments() != null) {
            totalAmount = getArguments().getString("total_amount");
        }

        TextView tvTotal = view.findViewById(R.id.total_amount);
        tvTotal.setText(totalAmount != null ? totalAmount : "Rp. 0");

        return view;
    }
}

