module com.example.hasartakip {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hasartakip to javafx.fxml;
    exports com.example.hasartakip;
}