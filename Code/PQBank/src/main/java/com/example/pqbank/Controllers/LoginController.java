package com.example.pqbank.Controllers;

import com.example.pqbank.Models.Model;
import com.example.pqbank.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public ChoiceBox<AccountType> accountSelector;
    public TextField payeeAddress_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Label payeeAddress_lbl;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountSelector.setItems(FXCollections.observableArrayList(AccountType.ADMIN, AccountType.CLIENT));
        accountSelector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        accountSelector.valueProperty().addListener(observable -> setAcc_selector());
        login_btn.setOnAction(e -> onLogin());

    }

    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow(); // trick to get root stage at the runtime.
        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT){
            // Evaluate Client Credentials
            Model.getInstance().evaluateClientCred(payeeAddress_fld.getText(), password_fld.getText());
            if(Model.getInstance().getClientLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showClientWindow();
                // Close the stage.
                Model.getInstance().getViewFactory().closeStage(stage);

            }else{
                payeeAddress_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("No Such Login Credential");
            }
        }else{
            Model.getInstance().evaluateAdminCred(payeeAddress_fld.getText(), password_fld.getText());
            if(Model.getInstance().getAdminLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showAdminWindow();
                // Close the stage.
                Model.getInstance().getViewFactory().closeStage(stage);
            }else{
                payeeAddress_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("No Such Login Credential");
            }

        }
    }

    private void setAcc_selector(){
        Model.getInstance().getViewFactory().setLoginAccountType(accountSelector.getValue());
        if(accountSelector.getValue() == AccountType.ADMIN){
            payeeAddress_lbl.setText("Username:");
        }else{
            payeeAddress_lbl.setText("Payee Address:");
        }
    }
}
