package com.example.pqbank.Controllers.Admin;

import com.example.pqbank.Controllers.AlertBox;

import com.example.pqbank.Models.Client;
import com.example.pqbank.Models.Model;
import com.example.pqbank.Views.ClientCellFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TransactionAdminController implements Initializable {
    public TextField search_fld;
    public Button search_btn;
    public TextField chAmountMoney_fld;
    public Button chDeposit_btn;
    public Label dateTime_lbl;
    public ListView<Client> result_listView;
    public Button saDeposit_btn;
    public TextField saAmountMoney_fld;


    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // be first.
        onShowClients();
        // Search Client
        search_btn.setOnAction(e -> onSearchClient());
        chDeposit_btn.setOnAction(e -> onCheckingDeposit());
        saDeposit_btn.setOnAction(e -> onSavingDeposit());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateTime_lbl.setText(LocalDate.now().format(formatter));



    }
    public void onShowClients(){
        // Show all Clients first
        initClientsList();
        result_listView.setItems(Model.getInstance().getClients());
        result_listView.setCellFactory(e -> new ClientCellFactory());
    }
    public void initClientsList(){
        if(Model.getInstance().getClients().isEmpty()){
            Model.getInstance().setClients();
        }
    }
//    public void onSearchClient(){
//        ObservableList<Client> searchResults = Model.getInstance().searchClient(search_fld.getText());
//        result_listView.setItems(searchResults);
//        result_listView.setCellFactory(e -> new ClientCellFactory());
//        client = searchResults.get(0);
//        System.out.println(result_listView.getSelectionModel().getSelectedIndex());
//    }

    public void onSearchClient(){
        ResultSet rs = Model.getInstance().getDatabaseDriver().searchClient(search_fld.getText());
        try {
            if(rs.isBeforeFirst()){
                if(rs.getString("payeeAddress").equals(search_fld.getText())){
                    ObservableList<Client> searchResults = Model.getInstance().searchClient(search_fld.getText());
                    result_listView.setItems(searchResults);
                    result_listView.setCellFactory(e -> new ClientCellFactory());
                    client = searchResults.get(0);
                }
            }else{
                AlertBox.display("Failed", "Wrong Payee Address's Client. Please Enter Valid Account!");
                search_fld.setText("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void onCheckingDeposit() {

        try {
            double amount = Double.parseDouble(chAmountMoney_fld.getText());
            try {
                if(amount > 0){
                    double newBalance = amount + client.checkingAccountProperty().get().balanceProperty().get();
                    Model.getInstance().getDatabaseDriver().depositChecking(client.payeeAddressProperty().get(), newBalance);
                    AlertBox.display("Successful!", "Deposited " + amount + "$ to " + search_fld.getText() + " Successful!");
                }else{
                    AlertBox.display("Failed !", "Please Input Valid Amount.");
                }
            }catch (NullPointerException e){
                AlertBox.display("Failed!", "Please Enter Payee Address Client Before Deposit !");
            }
        } catch (NumberFormatException e) {
            AlertBox.display("Failed!", "Input is not a valid number!");
        }
        emptyField();
        onShowClients();
    }
    private  void onSavingDeposit(){
        try{
            double amount = Double.parseDouble(saAmountMoney_fld.getText());
            try {
                if(amount > 0){
                    double newBalance = amount + client.savingAccountProperty().get().balanceProperty().get();
                    Model.getInstance().getDatabaseDriver().depositSaving(client.payeeAddressProperty().get(), newBalance);
                    AlertBox.display("Successful!", "Deposited " + amount + " to " + search_fld.getText() + " Successful!");
                }else{
                    AlertBox.display("Failed !", "Please Input Valid Amount.");

                }

            }catch (NullPointerException e){
                AlertBox.display("Failed!", "Please Enter Payee Address Client Before Deposit !");
            }
        }catch(NumberFormatException e){
            AlertBox.display("Failed!", "Input is not a valid number!");
        }
        emptyField();
        onShowClients();
    }


    private void emptyField(){
        search_fld.setText("");
        chAmountMoney_fld.setText("");
        saAmountMoney_fld.setText("");
        result_listView.setItems(null);
    }
}
