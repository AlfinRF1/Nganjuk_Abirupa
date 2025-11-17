package com.example.nganjukabirupa;

public class HargaCalculator {

    public static int hitungHargaDewasa(WisataModel wisata, int jumlahDewasa) {
        return jumlahDewasa * wisata.getTiketDewasa();
    }

    public static int hitungHargaAnak(WisataModel wisata, int jumlahAnak) {
        return jumlahAnak * wisata.getTiketAnak();
    }

    public static int hitungAsuransi(WisataModel wisata, int jumlahDewasa, int jumlahAnak) {
        int totalOrang = jumlahDewasa + jumlahAnak;
        return totalOrang * wisata.getAsuransi();
    }

    public static int hitungTotal(WisataModel wisata, int jumlahDewasa, int jumlahAnak) {
        return hitungHargaDewasa(wisata, jumlahDewasa)
                + hitungHargaAnak(wisata, jumlahAnak)
                + hitungAsuransi(wisata, jumlahDewasa, jumlahAnak);
    }
}