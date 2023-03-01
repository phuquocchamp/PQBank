module com.example.pqbank {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.pqbank to javafx.fxml;
    exports com.example.pqbank;
    exports com.example.pqbank.Controllers;
    exports com.example.pqbank.Controllers.Client;
    exports com.example.pqbank.Controllers.Admin; // ??????
    exports com.example.pqbank.Models;
    exports com.example.pqbank.Views;
}