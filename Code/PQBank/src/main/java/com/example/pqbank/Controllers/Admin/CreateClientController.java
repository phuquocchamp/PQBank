package com.example.pqbank.Controllers.Admin;

import com.example.pqbank.Controllers.AlertBox;
import com.example.pqbank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {


    public TextField password_fld;
    public TextField firstName_fld;
    public Button create_btn;
    public TextField lastName_fld;

    public Label error_lbl;
    public Label payeeAddress_lbl;
    public CheckBox chAccount_box;
    public CheckBox svAccount_box;
    public TextField chAmount_fld;
    public TextField svAmount_fld;
    public CheckBox payeeAddress_box;
    public Label dateTime_lbl;


    private String payeeAddress;
    private boolean createCheckingAccountFlag = false;
    private boolean createSavingAccountFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateTime_lbl.setText(LocalDate.now().format(formatter));
        chAmount_fld.setEditable(false);
        svAmount_fld.setEditable(false);
        payeeAddress_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                if(firstName_fld.getText().isEmpty()){
                    AlertBox.display("Failed", "Please Enter A First Name!");
                }
                if(lastName_fld.getText().isEmpty()){
                    AlertBox.display("Failed", "Please Enter A Last Name!");

                }
                if(!firstName_fld.getText().isEmpty() && !lastName_fld.getText().isEmpty()){
                    createPayeeAddress();
                    payeeAddress_lbl.setText(payeeAddress);
                }

            }
        });
        chAccount_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                createCheckingAccountFlag = true;
                chAmount_fld.setEditable(true);
                chAmount_fld.setText("0");
            }
        });
        svAccount_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                createSavingAccountFlag = true;
                svAmount_fld.setEditable(true);
                svAmount_fld.setText("0");
            }
        });
        create_btn.setOnAction(event -> createClient());

    }

    private void createClient(){
        // Check not empty text field.
        if(firstName_fld.getText().isEmpty() || lastName_fld.getText().isEmpty() || password_fld.getText().isEmpty()){
            AlertBox.display("Failed", "Please Input Enough Text Field !");
            return;
        }else{
            // Create Checking Account
            if(createCheckingAccountFlag){
                createAccount("Checking");
            }else{
                AlertBox.display("Failed", "Please Create At Least 1 Account (Checking Account)!");
                return;
            }
            // Create Saving Account
            if(createSavingAccountFlag){
                createAccount("Saving");
            }
            // Create Client

            String fName = firstName_fld.getText();
            String lName = lastName_fld.getText();
            String Password = password_fld.getText();
            Model.getInstance().getDatabaseDriver().createClient(fName, lName, payeeAddress, Password, LocalDate.now());
            // update Client on Client Window
            Model.getInstance().setClients();

            // error css
            error_lbl.setStyle("-fx-text-fill: green");
            error_lbl.setText("Client Created Successfully !");
            emptyField();
        }

    }

    private void emptyField() {
        firstName_fld.setText("");
        lastName_fld.setText("");
        payeeAddress_box.setSelected(false);
        payeeAddress_lbl.setText("");
        password_fld.setText("");
        chAccount_box.setSelected(false);
        chAmount_fld.setText("");
        svAccount_box.setSelected(false);
        svAmount_fld.setText("");
    }


    // Automatically Generate payeeAddress
    public void createPayeeAddress(){
        String namePattern = "^[A-Z][a-z]*(\\s[A-Z][a-z]*)*$";
        if(firstName_fld.getText().matches(namePattern) && lastName_fld.getText().matches(namePattern)){
            String pAddress = (firstName_fld.getText().replaceAll("\\s+", "")).toLowerCase();
            String[] lStr = (lastName_fld.getText()).toLowerCase().split(" ");
            // Get id of the Clients.
            int id = Model.getInstance().getDatabaseDriver().getLastClientId() + 1;
            int index = 0;
            while(index  < lStr.length){
                pAddress += (String.valueOf(lStr[index].charAt(0)));
                index++;
            }
            payeeAddress =  "#" + pAddress + "@pqbank.vn";
        }else if(!firstName_fld.getText().matches(namePattern)){
            AlertBox.display("Failed", "Please enter valid first name !");
        }else if(!lastName_fld.getText().matches(namePattern)){
            AlertBox.display("Failed", "Please enter valid last name !");
        }
    }

    public void createAccount(String accountType){
        double checkingBalance = Double.parseDouble(chAmount_fld.getText());
        double savingBalance = Double.parseDouble(svAmount_fld.getText());
        // Generate Account Number
        String firstSection = "4506";
        String lastSection = Integer.toString(new Random().nextInt(9999) + 1000);
        String accountNumber = firstSection + " " + lastSection;
        // Create the checking Account.
        if(accountType.equals("Checking")){
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 10, checkingBalance);
        }else{
            Model.getInstance().getDatabaseDriver().createSavingAccount(payeeAddress, accountNumber, 2000, savingBalance);
        }
    }
}
