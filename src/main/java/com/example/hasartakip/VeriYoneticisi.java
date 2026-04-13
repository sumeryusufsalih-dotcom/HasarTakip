package com.example.hasartakip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VeriYoneticisi {

    private static final String DB_DOSYA_ADI = "hasar_veritabani.txt";


    public static Arac veritabanindanAracBul(String arananPlaka) { //araç bul
        if (arananPlaka == null) return null;
        String p = arananPlaka.trim().toUpperCase();
        for (Arac a : tumKayitlariGetir()) {
            if (a.getPlaka() != null && a.getPlaka().trim().toUpperCase().equals(p)) {
                return a;
            }
        }
        return null;
    }


    public static List<Arac> tumKayitlariGetir() { //kayıt getirme
        try {
            Path db = dbPathHazirla();
            try (BufferedReader br = Files.newBufferedReader(db, StandardCharsets.UTF_8)) {
                return parseKayitlar(br);
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public static boolean kayitEkleVeyaGuncelle(Arac yeni) { //kayıt ekle
        if (yeni == null || yeni.getPlaka() == null) return false;
        String plaka = yeni.getPlaka().trim().toUpperCase();

        List<Arac> list = tumKayitlariGetir();
        boolean guncellendi = false;
        for (int i = 0; i < list.size(); i++) {
            Arac a = list.get(i);
            if (a.getPlaka() != null && a.getPlaka().trim().toUpperCase().equals(plaka)) {
                list.set(i, yeni);
                guncellendi = true;
                break;
            }
        }
        if (!guncellendi) list.add(yeni);

        return dosyayaYaz(list);
    }


    public static boolean kayitSil(String plaka) { //kayıt sil
        if (plaka == null) return false;
        String p = plaka.trim().toUpperCase();

        List<Arac> list = tumKayitlariGetir();
        boolean silindi = list.removeIf(a -> a.getPlaka() != null && a.getPlaka().trim().toUpperCase().equals(p));
        if (!silindi) return false;
        return dosyayaYaz(list);
    }

    //DOSYA YÖNETİMİ

    private static Path dbPathHazirla() throws IOException {
        Path work = Paths.get(DB_DOSYA_ADI);
        if (Files.exists(work)) return work;

        try (InputStream in = VeriYoneticisi.class.getResourceAsStream("/" + DB_DOSYA_ADI)) {
            if (in == null) {
                // resources da yoksa boş dosya oluştur
                Files.writeString(work, "", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                return work;
            }
            Files.copy(in, work, StandardCopyOption.REPLACE_EXISTING);
            return work;
        }
    }

    private static boolean dosyayaYaz(List<Arac> list) {
        try {
            Path db = dbPathHazirla();
            StringBuilder sb = new StringBuilder();
            for (Arac a : list) {
                sb.append("PLAKA:").append(nvl(a.getPlaka())).append("\n");
                sb.append("TC:").append(nvl(a.getTcNo())).append("\n");
                sb.append("SASI:").append(nvl(a.getSasiNo())).append("\n");
                sb.append("SAHIP:").append(nvl(a.getSahipAdSoyad())).append("\n");
                sb.append("TELEFON:").append(nvl(a.getTelefon())).append("\n");
                sb.append("MARKA:").append(nvl(a.getMarka())).append("\n");
                sb.append("MODEL:").append(nvl(a.getTicariAdi())).append("\n");
                sb.append("YIL:").append(a.getModelYili() == 0 ? "" : a.getModelYili()).append("\n");
                sb.append("TESCIL:").append(nvl(a.getTescilTarihi())).append("\n");
                sb.append("DOSYA:").append(nvl(a.getDosyaNo())).append("\n");
                sb.append("IHBAR:").append(nvl(a.getIhbarTarihi())).append("\n");
                sb.append("SERVIS:").append(nvl(a.getServisBilgisi())).append("\n");
                sb.append("EKSPER:").append(nvl(a.getEksperBilgisi())).append("\n");
                sb.append("DURUM:").append(nvl(a.getDurum())).append("\n");
                sb.append("ACIKLAMA:").append(nvl(a.getAciklama())).append("\n");
                sb.append("HASAR:").append(nvl(a.getToplamHasar())).append("\n");
                sb.append("MUAFIYET:").append(nvl(a.getMuafiyet())).append("\n");
                for (Arac.Parca p : a.getParcaListesi()) {
                    sb.append("PARCA:")
                            .append(nvl(p.getParcaAdi())).append(";")
                            .append(nvl(p.getParcaDurum())).append(";")
                            .append(nvl(p.getParcaTur()))
                            .append("\n");
                }
                sb.append("---\n");
            }
            Files.writeString(db, sb.toString(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
            return false;
        }
    }

    //kategorileme

    private static List<Arac> parseKayitlar(BufferedReader br) throws IOException {
        List<Arac> list = new ArrayList<>();
        Arac temp = null;
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("PLAKA:")) {
                temp = new Arac();
                temp.setPlaka(degerAl(line).toUpperCase());
                continue;
            }

            if (temp == null) continue;

            if (line.startsWith("TC:")) temp.setTcNo(degerAl(line));
            else if (line.startsWith("SASI:")) temp.setSasiNo(degerAl(line));
            else if (line.startsWith("SAHIP:")) temp.setSahipAdSoyad(degerAl(line));
            else if (line.startsWith("TELEFON:")) temp.setTelefon(degerAl(line));
            else if (line.startsWith("MARKA:")) temp.setMarka(degerAl(line));
            else if (line.startsWith("MODEL:")) temp.setTicariAdi(degerAl(line));
            else if (line.startsWith("YIL:")) {
                String yil = degerAl(line);
                try {
                    temp.setModelYili(yil.isEmpty() ? 0 : Integer.parseInt(yil));
                } catch (NumberFormatException e) {
                    temp.setModelYili(0);
                }
            }
            else if (line.startsWith("TESCIL:")) temp.setTescilTarihi(degerAl(line));
            else if (line.startsWith("DOSYA:")) temp.setDosyaNo(degerAl(line));
            else if (line.startsWith("IHBAR:")) temp.setIhbarTarihi(degerAl(line));
            else if (line.startsWith("SERVIS:")) temp.setServisBilgisi(degerAl(line));
            else if (line.startsWith("EKSPER:")) temp.setEksperBilgisi(degerAl(line));
            else if (line.startsWith("DURUM:")) temp.setDurum(degerAl(line));
            else if (line.startsWith("ACIKLAMA:")) temp.setAciklama(degerAl(line));
            else if (line.startsWith("HASAR:")) temp.setToplamHasar(degerAl(line));
            else if (line.startsWith("MUAFIYET:")) temp.setMuafiyet(degerAl(line));
            else if (line.startsWith("PARCA:")) {
                String[] parca = degerAl(line).split(";");
                if (parca.length >= 3) temp.parcaEkle(parca[0], parca[1], parca[2]);
            }
            else if (line.equals("---")) {
                list.add(temp);
                temp = null;
            }
        }
        if (temp != null) list.add(temp);
        return list;
    }

    private static String degerAl(String satir) {
        int ikiNoktaIndex = satir.indexOf(":");
        if (ikiNoktaIndex != -1 && ikiNoktaIndex < satir.length() - 1) {
            return satir.substring(ikiNoktaIndex + 1).trim();
        }
        return "";
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
