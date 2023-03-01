package com.example.pqbank.Controllers.Client;

import com.example.pqbank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {


    public BorderPane client_parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue,oldVal, newVal) -> {
            switch (newVal){
                case TRANSACTIONS -> client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionView());
                case ACCOUNT -> client_parent.setCenter(Model.getInstance().getViewFactory().getAccountView());
//                case PROFILE -> client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            }
        });
    }
}
