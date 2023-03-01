package com.example.pqbank.Controllers.Admin;

import com.example.pqbank.Models.Model;
import com.example.pqbank.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
  
    public Button logout_btn;
    public Button report_btn;
    public Button dashboard_btn;
    public Button createClient_btn;
    public Button deposit_btn;
    public Button clients_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }


    public void addListeners(){
        dashboard_btn.setOnAction(event -> onDashboard());
        createClient_btn.setOnAction(event -> onCreateClient());
        deposit_btn.setOnAction(event -> onDeposit());
        clients_btn.setOnAction(event -> onClients());
        logout_btn.setOnAction(event -> onLogout());
    }

    private void onClients() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    public void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }

    public void onDashboard(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }

    public void onDeposit(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.TRANSACTIONS);
    }
    private void onLogout(){
        // Get stage.
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        // Close the Client Window
        Model.getInstance().getViewFactory().closeStage(stage);
        // Show login
        Model.getInstance().getViewFactory().showLoginWindow();
        // Set Client Login to false;
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
