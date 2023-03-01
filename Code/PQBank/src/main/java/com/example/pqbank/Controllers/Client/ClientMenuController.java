package com.example.pqbank.Controllers.Client;

import com.example.pqbank.Models.Model;
import com.example.pqbank.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button account_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;


    // This interface has been superseded by automatic injection of location and resources properties into the controller FXMLLoader
    // Hàm Initialize tự động thay thế tài nguyên của (locatin, resources) vào trình điều khiển FXMLLoader.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }

    public void addListeners(){
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransaction());
        account_btn.setOnAction(event -> onAccount());
//        profile_btn.setOnAction(event -> onProfile());
        logout_btn.setOnAction(event -> onLogout());
    }
    private void onDashboard(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }


    private void onTransaction() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }


    private void onAccount() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNT);
    }

//    public void onProfile(){
//        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
//    }
    private void onLogout(){
        // Get stage.
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        // Close the Client Window
        Model.getInstance().getViewFactory().closeStage(stage);
        // Show login
        Model.getInstance().getViewFactory().showLoginWindow();
        // Set Client Login to false;
        Model.getInstance().setClientLoginSuccessFlag(false);
    }

}
