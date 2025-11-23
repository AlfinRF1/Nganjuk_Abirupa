package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class RiwayatModel {

    @SerializedName("id_transaksi")
    private String idTransaksi;

    @SerializedName("nama_wisata")
    private String namaWisata;

    @SerializedName("lokasi")
    private String lokasi;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("total_harga")
    private int totalHarga;

    @SerializedName("status")
    private String status;

    @SerializedName("metode_pembayaran")
    private String metodePembayaran = "QRIS"; // default tetap boleh

    // Getter
    public String getIdTransaksi() { return idTransaksi; }
    public String getNamaWisata() { return namaWisata; }
    public String getLokasi() { return lokasi; }
    public String getTanggal() { return tanggal; }
    public int getTotalHarga() { return totalHarga; }
    public String getStatus() { return status != null ? status : "Selesai"; }
    public String getMetodePembayaran() { return metodePembayaran; }

    // Setter opsional
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran != null ? metodePembayaran : "QRIS";
    }
}
