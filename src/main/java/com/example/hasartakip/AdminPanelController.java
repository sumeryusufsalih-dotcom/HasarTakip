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
import java.util.List;

public class AdminPanelController {

    //kayıt listesi
    @FXML private TableView<Arac> tblKayitlar;
    @FXML private TableColumn<Arac, String> colPlaka;
    @FXML private TableColumn<Arac, String> colDosya;
    @FXML private TableColumn<Arac, String> colDurum;
    @FXML private TableColumn<Arac, String> colSahip;

    //Sağ panel
    @FXML private TextField txtPlaka;
    @FXML private TextField txtTc;
    @FXML private TextField txtSasi;
    @FXML private TextField txtSahip;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtMarka;
    @FXML private TextField txtModel;
    @FXML private TextField txtYil;
    @FXML private TextField txtTescil;
    @FXML private TextField txtDosya;
    @FXML private TextField txtIhbar;
    @FXML private TextField txtServis;
    @FXML private TextField txtEksper;
    @FXML private TextField txtDurum;
    @FXML private TextField txtHasar;
    @FXML private TextField txtMuafiyet;
    @FXML private TextArea txtAciklama;

    //Parça ekleme
    @FXML private TextField txtParcaAdi;
    @FXML private TextField txtParcaDurum;
    @FXML private TextField txtParcaTur;
    @FXML private TableView<Arac.Parca> tblParcalar;
    @FXML private TableColumn<Arac.Parca, String> colParcaAdi;
    @FXML private TableColumn<Arac.Parca, String> colParcaDurum;
    @FXML private TableColumn<Arac.Parca, String> colParcaTur;

    private final ObservableList<Arac> kayitlar = FXCollections.observableArrayList();
    private final ObservableList<Arac.Parca> parcalar = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // kayıt listesi
        colPlaka.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPlaka()));
        colDosya.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDosyaNo()));
        colDurum.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDurum()));
        colSahip.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSahipAdSoyad()));

        tblKayitlar.setItems(kayitlar);

        // Parçalar
        colParcaAdi.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getParcaAdi()));
        colParcaDurum.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getParcaDurum()));
        colParcaTur.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getParcaTur()));
        tblParcalar.setItems(parcalar);
        tblParcalar.setPlaceholder(new Label("Parça/işlem yok."));

        //oto  form doldurma
        tblKayitlar.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                formuDoldur(newV);
            }
        });

        yenile();
    }

    @FXML
    void handleYenile(ActionEvent e) {
        yenile();
    }

    @FXML
    void handleYeniKayit(ActionEvent e) {
        tblKayitlar.getSelectionModel().clearSelection();
        temizle();
    }

    @FXML
    void handleKaydetGuncelle(ActionEvent e) {
        Arac arac = formdanAracOlustur();
        if (arac == null) return;

        boolean ok = VeriYoneticisi.kayitEkleVeyaGuncelle(arac);
        if (ok) {
            uyari(Alert.AlertType.INFORMATION, "Başarılı", "Kayıt kaydedildi/güncellendi.");
            yenile();
            sec(arac.getPlaka());
        } else {
            uyari(Alert.AlertType.ERROR, "Hata", "Kayıt kaydedilemedi.");
        }
    }

    @FXML
    void handleSil(ActionEvent e) {
        String plaka = (txtPlaka.getText() == null) ? "" : txtPlaka.getText().trim().toUpperCase();
        if (plaka.isEmpty()) {
            uyari(Alert.AlertType.WARNING, "Uyarı", "Silmek için plaka giriniz veya listeden seçiniz.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Onay");
        confirm.setHeaderText(null);
        confirm.setContentText(plaka + " plakalı kayıt silinsin mi?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        boolean ok = VeriYoneticisi.kayitSil(plaka);
        if (ok) {
            uyari(Alert.AlertType.INFORMATION, "Silindi", "Kayıt silindi.");
            yenile();
            temizle();
        } else {
            uyari(Alert.AlertType.ERROR, "Hata", "Kayıt silinemedi (plaka bulunamadı olabilir).");
        }
    }

    @FXML
    void handleParcaEkle(ActionEvent e) {
        String ad = val(txtParcaAdi);
        String durum = val(txtParcaDurum);
        String tur = val(txtParcaTur);

        if (ad.isEmpty() || durum.isEmpty() || tur.isEmpty()) {
            uyari(Alert.AlertType.WARNING, "Eksik Bilgi", "Parça adı / durum / tür alanlarını doldur.");
            return;
        }
        parcalar.add(new Arac.Parca(ad, durum, tur));
        txtParcaAdi.clear();
        txtParcaDurum.clear();
        txtParcaTur.clear();
    }

    @FXML
    void handleParcaSil(ActionEvent e) {
        Arac.Parca secili = tblParcalar.getSelectionModel().getSelectedItem();
        if (secili != null) {
            parcalar.remove(secili);
        }
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //fonksiyonlar

    private void yenile() {
        List<Arac> list = VeriYoneticisi.tumKayitlariGetir();
        kayitlar.setAll(list);
        tblKayitlar.refresh();
    }

    private void formuDoldur(Arac a) {
        txtPlaka.setText(nvl(a.getPlaka()));
        txtTc.setText(nvl(a.getTcNo()));
        txtSasi.setText(nvl(a.getSasiNo()));
        txtSahip.setText(nvl(a.getSahipAdSoyad()));
        txtTelefon.setText(nvl(a.getTelefon()));
        txtMarka.setText(nvl(a.getMarka()));
        txtModel.setText(nvl(a.getTicariAdi()));
        txtYil.setText(a.getModelYili() == 0 ? "" : String.valueOf(a.getModelYili()));
        txtTescil.setText(nvl(a.getTescilTarihi()));
        txtDosya.setText(nvl(a.getDosyaNo()));
        txtIhbar.setText(nvl(a.getIhbarTarihi()));
        txtServis.setText(nvl(a.getServisBilgisi()));
        txtEksper.setText(nvl(a.getEksperBilgisi()));
        txtDurum.setText(nvl(a.getDurum()));
        txtHasar.setText(nvl(a.getToplamHasar()));
        txtMuafiyet.setText(nvl(a.getMuafiyet()));
        txtAciklama.setText(nvl(a.getAciklama()));

        parcalar.setAll(a.getParcaListesi());
        tblParcalar.refresh();
    }

    private void temizle() {//herşeyi temizler
        txtPlaka.clear();
        txtTc.clear();
        txtSasi.clear();
        txtSahip.clear();
        txtTelefon.clear();
        txtMarka.clear();
        txtModel.clear();
        txtYil.clear();
        txtTescil.clear();
        txtDosya.clear();
        txtIhbar.clear();
        txtServis.clear();
        txtEksper.clear();
        txtDurum.clear();
        txtHasar.clear();
        txtMuafiyet.clear();
        txtAciklama.clear();
        parcalar.clear();
    }

    private Arac formdanAracOlustur() {
        String plaka = val(txtPlaka).toUpperCase();
        String tc = val(txtTc);
        String dosya = val(txtDosya);

        if (plaka.isEmpty() || tc.isEmpty() || dosya.isEmpty()) {
            uyari(Alert.AlertType.WARNING, "Eksik Bilgi", "Plaka, T.C ve Dosya No zorunludur.");
            return null;
        }
        if (!tc.matches("\\d+") || tc.length() != 11) {
            uyari(Alert.AlertType.ERROR, "Hatalı Giriş", "T.C. Kimlik No 11 haneli ve rakam olmalıdır.");
            return null;
        }

        int yil = 0;
        String yilStr = val(txtYil);
        if (!yilStr.isEmpty()) {
            try {
                yil = Integer.parseInt(yilStr);
            } catch (NumberFormatException ex) {
                uyari(Alert.AlertType.WARNING, "Uyarı", "Model yılı sayı olmalıdır. (Boş bırakabilirsin)");
                return null;
            }
        }

        Arac a = new Arac();
        a.setPlaka(plaka);
        a.setTcNo(tc);
        a.setDosyaNo(dosya);
        a.setSasiNo(val(txtSasi));
        a.setSahipAdSoyad(val(txtSahip));
        a.setTelefon(val(txtTelefon));
        a.setMarka(val(txtMarka));
        a.setTicariAdi(val(txtModel));
        a.setModelYili(yil);
        a.setTescilTarihi(val(txtTescil));
        a.setIhbarTarihi(val(txtIhbar));
        a.setServisBilgisi(val(txtServis));
        a.setEksperBilgisi(val(txtEksper));
        a.setDurum(val(txtDurum));
        a.setAciklama(val(txtAciklama));
        a.setToplamHasar(val(txtHasar));
        a.setMuafiyet(val(txtMuafiyet));


        for (Arac.Parca p : parcalar) {
            a.parcaEkle(p.getParcaAdi(), p.getParcaDurum(), p.getParcaTur());
        }
        return a;
    }

    private void sec(String plaka) {
        for (Arac a : kayitlar) {
            if (a.getPlaka() != null && a.getPlaka().equalsIgnoreCase(plaka)) {
                tblKayitlar.getSelectionModel().select(a);
                tblKayitlar.scrollTo(a);
                break;
            }
        }
    }

    private static String val(TextInputControl c) {
        return c.getText() == null ? "" : c.getText().trim();
    }
    private static String nvl(String s) { return s == null ? "" : s; }

    private void uyari(Alert.AlertType tur, String baslik, String mesaj) {
        Alert alert = new Alert(tur);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
