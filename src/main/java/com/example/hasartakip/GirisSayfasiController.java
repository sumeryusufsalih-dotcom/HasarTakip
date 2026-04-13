package com.example.hasartakip;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GirisSayfasiController {

    @FXML private TextField txtPlaka;
    @FXML private TextField txtTC;
    @FXML private TextField txtHasarNo;
    @FXML private Button btnSorgula;
    @FXML private Hyperlink linkAdmin;

    @FXML
    void handleSorgulaBtn(ActionEvent event) {
        String plaka = txtPlaka.getText().trim();
        String tc = txtTC.getText().trim();
        String hasarNo = txtHasarNo.getText().trim();

        // 1. Boş Alan Kontrolü
        if (plaka.isEmpty() || tc.isEmpty() || hasarNo.isEmpty()) {
            uyariGoster(Alert.AlertType.WARNING, "Eksik Bilgi", "Lütfen tüm alanları doldurunuz.");
            return;
        }

        // 2. TC Format Kontrolü
        if (!tc.matches("\\d+") || tc.length() != 11) {
            uyariGoster(Alert.AlertType.ERROR, "Hatalı Giriş", "T.C. Kimlik No 11 haneli ve rakam olmalıdır.");
            return;
        }

        // 3. Veritabanından Sorgulama
        Arac bulunanArac = VeriYoneticisi.veritabanindanAracBul(plaka);

        if (bulunanArac != null) {
            // Araç bulundu, şimdi TC ve Dosya No eşleşiyor mu diye bakıyoruz
            if (bulunanArac.getTcNo().equals(tc) && bulunanArac.getDosyaNo().equalsIgnoreCase(hasarNo)) {
                // GİRİŞ BAŞARILI -> Yeni sayfaya git
                sayfaDegistir(event, bulunanArac);
            } else {
                uyariGoster(Alert.AlertType.ERROR, "Hatalı Bilgi", "Plaka sistemde var fakat T.C. veya Dosya No uyuşmuyor.");
            }
        } else {
            uyariGoster(Alert.AlertType.ERROR, "Bulunamadı", "Bu plakaya ait bir hasar kaydı bulunamadı.");
        }
    }

    private void sayfaDegistir(ActionEvent event, Arac arac) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("KullaniciSayfasi.fxml"));
            Scene scene = new Scene(loader.load());

            KullaniciSayfasiController controller = loader.getController();
            controller.verileriYukle(arac); // Aracı direkt gönderiyoruz

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Kullanıcı Paneli - " + arac.getPlaka());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAdminLink(ActionEvent event) {
        // Admin giriş ekranına geç
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminGirisSayfasi.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Admin Girişi");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            uyariGoster(Alert.AlertType.ERROR, "Hata", "Admin ekranı açılamadı.");
        }
    }

    private void uyariGoster(Alert.AlertType tur, String baslik, String mesaj) {
        Alert alert = new Alert(tur);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}