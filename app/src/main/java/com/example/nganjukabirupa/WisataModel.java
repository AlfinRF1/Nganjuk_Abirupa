package com.example.nganjukabirupa;

public class WisataModel {
    private String namaWisata;
    private String lokasi;
    private int tiketDewasa;
    private int tiketAnak;
    private int asuransi;

    public WisataModel(String namaWisata, String lokasi, int tiketDewasa, int tiketAnak, int asuransi) {
        this.namaWisata = namaWisata;
        this.lokasi = lokasi;
        this.tiketDewasa = tiketDewasa;
        this.tiketAnak = tiketAnak;
        this.asuransi = asuransi;
    }

    public String getNamaWisata() { return namaWisata; }
    public String getLokasi() { return lokasi; }
    public int getTiketDewasa() { return tiketDewasa; }
    public int getTiketAnak() { return tiketAnak; }
    public int getAsuransi() { return asuransi; }
}