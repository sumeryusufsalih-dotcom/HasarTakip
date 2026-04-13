package com.example.hasartakip;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class KullaniciSayfasiController {

    @FXML private Label lblPlaka, lblSahip, lblSasiNo, lblMarka, lblTicariAdi, lblDosyaNo, lblTelefon, lblModelYili, lblTescilTarihi;
    @FXML private Label lblIhbarTarihi, lblServis, lblEksper;
    @FXML private Label lblDurum, lblToplamHasar, lblMuafiyet;
    @FXML private TextArea txtAciklama;

    // FXML'deki TableView bileşeni
    @FXML private TableView<Arac.Parca> tblParcalar;

    public void verileriYukle(Arac arac) {
        if (arac == null) return;

        //Araç Bilgileri
        lblPlaka.setText(arac.getPlaka());
        lblSahip.setText(arac.getSahipAdSoyad());
        lblSasiNo.setText(arac.getSasiNo());
        lblMarka.setText(arac.getMarka());
        lblTicariAdi.setText(arac.getTicariAdi());
        lblTelefon.setText(arac.getTelefon());
        lblModelYili.setText(String.valueOf(arac.getModelYili()));
        lblTescilTarihi.setText(arac.getTescilTarihi());

        //dosya detayları
        lblDosyaNo.setText(arac.getDosyaNo());
        lblIhbarTarihi.setText(arac.getIhbarTarihi());
        lblServis.setText(arac.getServisBilgisi());
        lblEksper.setText(arac.getEksperBilgisi());

        //Durum ve mali bilgiler
        lblDurum.setText(arac.getDurum());
        txtAciklama.setText(arac.getAciklama());
        lblToplamHasar.setText(arac.getToplamHasar());
        lblMuafiyet.setText(arac.getMuafiyet());

        //Parça listesi
        ObservableList<Arac.Parca> parcaVerileri = FXCollections.observableArrayList(arac.getParcaListesi());
        tblParcalar.setItems(parcaVerileri);

        // Tablo boşsa parça bilgisi yok yaz
        tblParcalar.setPlaceholder(new Label("Kayıtlı parça/işlem bulunamadı."));
    }

    @FXML
    void handleCikis(ActionEvent event) {
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
}