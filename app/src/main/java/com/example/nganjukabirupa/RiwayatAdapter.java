package com.example.nganjukabirupa;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    private final List<RiwayatModel> riwayatList;

    public RiwayatAdapter(List<RiwayatModel> riwayatList) {
        this.riwayatList = riwayatList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWisataImage;
        TextView tvTanggal, tvNamaWisata, tvLokasiWisata, tvTotalHarga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivWisataImage = itemView.findViewById(R.id.iv_wisata_image);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvNamaWisata = itemView.findViewById(R.id.tv_wisata_nama);
            tvLokasiWisata = itemView.findViewById(R.id.tv_wisata_lokasi);
            tvTotalHarga = itemView.findViewById(R.id.tv_total_harga);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatModel model = riwayatList.get(position);
        Context context = holder.itemView.getContext();

        String namaWisata = model.getNamaWisata() != null ? model.getNamaWisata() : "Wisata";
        int imageResId = getDrawableForWisata(context, namaWisata);
        holder.ivWisataImage.setImageResource(imageResId);

        String tanggal = model.getTanggal() != null ? model.getTanggal().split(" ")[0] : "-";
        holder.tvTanggal.setText(tanggal);
        holder.tvNamaWisata.setText(namaWisata);
        holder.tvLokasiWisata.setText(model.getLokasi() != null ? model.getLokasi() : "-");
        holder.tvTotalHarga.setText("Total Tiket : Rp. " + model.getTotalHarga());

        Log.d("RiwayatAdapter", "Wisata: " + namaWisata + " → Image: " + imageResId);

        // ✅ Klik card → buka DetailRiwayatActivity
        holder.itemView.setOnClickListener(v -> {
            DetailRiwayatBottomSheet bottomSheet = DetailRiwayatBottomSheet.newInstance(
                    model.getNamaWisata(),
                    model.getLokasi(),
                    model.getIdTransaksi(),
                    model.getTanggal(),
                    model.getStatus(),
                    "QRIS",
                    "Rp. " + model.getTotalHarga()
            );
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "detail_riwayat");
        });

    }

    @Override
    public int getItemCount() {
        int count = riwayatList != null ? riwayatList.size() : 0;
        Log.d("RiwayatAdapter", "Jumlah item: " + count);
        return count;
    }

    private int getDrawableForWisata(Context context, String namaWisata) {
        if (namaWisata == null) return R.drawable.default_wisata;

        String lowerNama = namaWisata.toLowerCase();

        if (lowerNama.contains("sedudo")) return R.drawable.wisata_air_terjun_sedudo;
        else if (lowerNama.contains("roro kuning")) return R.drawable.wisata_roro_kuning;
        else if (lowerNama.contains("margo tresno")) return R.drawable.wisata_goa_margotresno;
        else if (lowerNama.contains("sri tanjung")) return R.drawable.wisata_sritanjung;
        else if (lowerNama.contains("anjuk ladang") || lowerNama.contains("tral")) return R.drawable.wisata_tral;
        else return R.drawable.default_wisata;
    }
}