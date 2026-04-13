package com.example.hasartakip;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminGirisSayfasiController {

    private static final String ADMIN_KULLANICI = "admin";
    private static final String ADMIN_SIFRE = "1234";

    @FXML private TextField txtKullanici;
    @FXML private PasswordField txtSifre;

    @FXML
    void handleGiris(ActionEvent event) {
        String k = txtKullanici.getText() == null ? "" : txtKullanici.getText().trim();
        String s = txtSifre.getText() == null ? "" : txtSifre.getText().trim();

        if (k.isEmpty() || s.isEmpty()) {
            uyari(Alert.AlertType.WARNING, "Eksik Bilgi", "Kullanıcı adı ve şifre giriniz.");
            return;
        }

        if (!ADMIN_KULLANICI.equals(k) || !ADMIN_SIFRE.equals(s)) {
            uyari(Alert.AlertType.ERROR, "Hatalı Giriş", "Kullanıcı adı veya şifre yanlış.");
            return;
        }

        // Admin paneline geçme
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPanel.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Admin Paneli");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            uyari(Alert.AlertType.ERROR, "Hata", "Admin paneli açılamadı.");
        }
    }

    @FXML
    void handleGeriDon(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GirisSayfasi.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Araç Hasar Takip Sistemi");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uyari(Alert.AlertType tur, String baslik, String mesaj) {
        Alert alert = new Alert(tur);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
