package com.example.nganjukabirupa;

public class WisataModel {
    private String namaWisata;
    private String lokasi;
    private int tiketDewasa;
    private int tiketAnak;
    private String fasilitas;
    private String deskripsi;
    private int asuransi = 0; // default 0 kalau gak dikirim dari backend

    // Default constructor (wajib untuk Retrofit/Gson)
    public WisataModel() {}

    // Constructor manual (opsional)
    public WisataModel(String namaWisata, String lokasi, int tiketDewasa, int tiketAnak, String fasilitas, String deskripsi) {
        this.namaWisata = namaWisata;
        this.lokasi = lokasi;
        this.tiketDewasa = tiketDewasa;
        this.tiketAnak = tiketAnak;
        this.fasilitas = fasilitas;
        this.deskripsi = deskripsi;
    }

    // Getter & Setter
    public String getNamaWisata() {
        return namaWisata;
    }

    public void setNamaWisata(String namaWisata) {
        this.namaWisata = namaWisata;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public int getTiketDewasa() {
        return tiketDewasa;
    }

    public void setTiketDewasa(int tiketDewasa) {
        this.tiketDewasa = tiketDewasa;
    }

    public int getTiketAnak() {
        return tiketAnak;
    }
    public int getAsuransi() {
        return asuransi;
    }

    public void setTiketAnak(int tiketAnak) {
        this.tiketAnak = tiketAnak;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}