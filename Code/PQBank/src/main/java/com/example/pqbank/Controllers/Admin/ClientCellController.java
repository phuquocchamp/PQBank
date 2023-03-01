package com.example.pqbank.Controllers.Admin;

import com.example.pqbank.Controllers.AlertBox;
import com.example.pqbank.Models.Client;
import com.example.pqbank.Models.Model;
import com.example.pqbank.Models.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    public Label lName_lbl;
    public Label fName_lbl;
    public Label payeeAddress_lbl;
    public Label ckAccount_lbl;
    public Label svAccount_lbl;
    public Label dateCreated;
    public Button Delete_btn;

    public Button info_btn;

    private final Client client;

    public ClientCellController(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fName_lbl.textProperty().bind(client.firstNameProperty());
        payeeAddress_lbl.textProperty().bind(client.payeeAddressProperty());
        ckAccount_lbl.textProperty().bind(client.checkingAccountProperty().asString());
        svAccount_lbl.textProperty().bind(client.savingAccountProperty().asString());

        info_btn.setOnAction(e -> Model.getInstance().getViewFactory().showMessageWindow(client.lastNameProperty().get(), client.firstNameProperty().get()));
        Delete_btn.setOnAction(e -> onDeleteClient());
    }

    private void onDeleteClient() {
        Model.getInstance().getDatabaseDriver().deleteClient(client.payeeAddressProperty().get());
        AlertBox.display("Successful", "Deleted Client.");
    }
}
