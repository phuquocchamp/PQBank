package com.example.pqbank.Controllers.Client;

import com.example.pqbank.Controllers.AlertBox;
import com.example.pqbank.Models.Model;
import com.example.pqbank.Models.Transaction;
import com.example.pqbank.Views.TransactionCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    public ListView<Transaction> transaction_listview;
    public TextField depo_address;
    public TextField depo_amount;
    public TextField depo_message;
    public Button depo_btn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllTransactionsList();
        transaction_listview.setItems(Model.getInstance().getAllTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        // onSendingDeposit
        depo_btn.setOnAction(e -> onSendingMoney());
    }

    public void initAllTransactionsList(){
        if(Model.getInstance().getAllTransactions().isEmpty()){
            Model.getInstance().setAllTransactions();
        }
    }


    public void onSendingMoney(){
        String Sender = Model.getInstance().getClient().payeeAddressProperty().get();
        String Receiver = depo_address.getText();
        double Amount = Double.parseDouble(depo_amount.getText());
        String Message = depo_message.getText();
        double currentSenderAmount = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().get();
        if(currentSenderAmount >=  Amount){
            ResultSet rs = Model.getInstance().getDatabaseDriver().searchClient(Receiver);
            try{
                if(rs.isBeforeFirst()){
                    Model.getInstance().getDatabaseDriver().updateBalance(Receiver, Amount,"ADD");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            // Subtract form sender checking Account.
            Model.getInstance().getDatabaseDriver().updateBalance(Sender, Amount, "SUB");
            // Update Checking Account From Client Object.
            Model.getInstance().getClient().checkingAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getCheckingAccountBalance(Sender));
            // Record new Transaction
            Model.getInstance().getDatabaseDriver().newTransactions(Sender, Receiver, Amount, Message);
            // Update From Transaction ListView

            onUpdateTransaction();


            AlertBox.display("Successful!", "Deposited " + Amount + " to " + Receiver);
        }else{
            AlertBox.display("Failed!", "Has Error Input.");
        }


        // Clear Empty Fields
        depo_address.setText("");
        depo_amount.setText("");
        depo_message.setText("");
    }


    private void onUpdateTransaction() {
//        System.out.println(Model.getInstance().getDatabaseDriver().getTotalTransactions());
        ObservableList<Transaction> listTrans = FXCollections.observableArrayList();
        Model.getInstance().prepareTransactions(listTrans, -1);
        transaction_listview.setItems(listTrans);
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());

      }
}
