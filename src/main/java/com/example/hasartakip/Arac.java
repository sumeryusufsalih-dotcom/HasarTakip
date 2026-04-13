package com.example.hasartakip;

import java.util.ArrayList;
import java.util.List;

public class Arac {
    private String plaka;
    private String tcNo;
    private String sasiNo;
    private String sahipAdSoyad;
    private String telefon;
    private String marka;
    private String ticariAdi;
    private int modelYili;
    private String tescilTarihi;

    // Dosya Detayları
    private String dosyaNo;
    private String ihbarTarihi;
    private String servisBilgisi;
    private String eksperBilgisi;

    private String durum;
    private String aciklama;

    // Finansal
    private String toplamHasar;
    private String muafiyet;

    // Parçalar
    private List<Parca> parcaListesi = new ArrayList<>();

    public Arac() {}

    // GETTER - SETTER METODLARI
    public String getPlaka() { return plaka; }
    public void setPlaka(String plaka) { this.plaka = plaka; }

    public String getTcNo() { return tcNo; }
    public void setTcNo(String tcNo) { this.tcNo = tcNo; }

    public String getSasiNo() { return sasiNo; }
    public void setSasiNo(String sasiNo) { this.sasiNo = sasiNo; }

    public String getSahipAdSoyad() { return sahipAdSoyad; }
    public void setSahipAdSoyad(String sahipAdSoyad) { this.sahipAdSoyad = sahipAdSoyad; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getMarka() { return marka; }
    public void setMarka(String marka) { this.marka = marka; }

    public String getTicariAdi() { return ticariAdi; }
    public void setTicariAdi(String ticariAdi) { this.ticariAdi = ticariAdi; }

    public int getModelYili() { return modelYili; }
    public void setModelYili(int modelYili) { this.modelYili = modelYili; }

    public String getTescilTarihi() { return tescilTarihi; }
    public void setTescilTarihi(String tescilTarihi) { this.tescilTarihi = tescilTarihi; }

    public String getDosyaNo() { return dosyaNo; }
    public void setDosyaNo(String dosyaNo) { this.dosyaNo = dosyaNo; }

    public String getIhbarTarihi() { return ihbarTarihi; }
    public void setIhbarTarihi(String ihbarTarihi) { this.ihbarTarihi = ihbarTarihi; }

    public String getServisBilgisi() { return servisBilgisi; }
    public void setServisBilgisi(String servisBilgisi) { this.servisBilgisi = servisBilgisi; }

    public String getEksperBilgisi() { return eksperBilgisi; }
    public void setEksperBilgisi(String eksperBilgisi) { this.eksperBilgisi = eksperBilgisi; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public String getToplamHasar() { return toplamHasar; }
    public void setToplamHasar(String toplamHasar) { this.toplamHasar = toplamHasar; }

    public String getMuafiyet() { return muafiyet; }
    public void setMuafiyet(String muafiyet) { this.muafiyet = muafiyet; }

    public List<Parca> getParcaListesi() { return parcaListesi; }
    public void parcaEkle(String ad, String durum, String tur) {
        this.parcaListesi.add(new Parca(ad, durum, tur));
    }

    public static class Parca {
        private String parcaAdi;
        private String parcaDurum;
        private String parcaTur;

        public Parca(String parcaAdi, String parcaDurum, String parcaTur) {
            this.parcaAdi = parcaAdi;
            this.parcaDurum = parcaDurum;
            this.parcaTur = parcaTur;
        }
        public String getParcaAdi() { return parcaAdi; }
        public String getParcaDurum() { return parcaDurum; }
        public String getParcaTur() { return parcaTur; }
    }
}