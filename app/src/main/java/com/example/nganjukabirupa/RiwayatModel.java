package com.example.nganjukabirupa;

import com.google.gson.annotations.SerializedName;

public class RiwayatModel {

    @SerializedName("id_transaksi")  // Mapping dari JSON
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
    private String metodePembayaran = "QRIS"; // default QRIS

    @SerializedName("image_name")
    private String imageName;

    @SerializedName("deskripsi")
    private String deskripsi;

    // Constructor lengkap
    public RiwayatModel(String idTransaksi, String namaWisata, String lokasi,
                        String tanggal, int totalHarga, String status,
                        String metodePembayaran, String imageName, String deskripsi) {
        this.idTransaksi = idTransaksi;
        this.namaWisata = namaWisata;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.totalHarga = totalHarga;
        this.status = status;
        this.metodePembayaran = metodePembayaran != null ? metodePembayaran : "QRIS";
        this.imageName = imageName;
        this.deskripsi = deskripsi;
    }

    // Getter & Setter
    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }

    public String getNamaWisata() { return namaWisata; }
    public void setNamaWisata(String namaWisata) { this.namaWisata = namaWisata; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public int getTotalHarga() { return totalHarga; }
    public void setTotalHarga(int totalHarga) { this.totalHarga = totalHarga; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran != null ? metodePembayaran : "QRIS";
    }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}
