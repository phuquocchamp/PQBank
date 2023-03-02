package com.example.pqbank.Controllers.Client;

import com.example.pqbank.Models.Model;
import com.example.pqbank.Models.Transaction;
import com.example.pqbank.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label total_balance;
    public Label login_date;
    public Label checking_bal;
    public Label ck_overdue_date;
    public Label checking_acc_num;
    public Label saving_bal;
    public Label sa_overdue_date;
    public Label saving_acc_num;
    public ListView<Transaction> transaction_listview;
    public Label income_lbl;
    public Label expenses_lbl;
    public Label acc_type;
    public Label acc_overdue;
    public Label acc_avail;
    public Label acc_limit;
    public Label acc_status;
    public Label acc_currency;
    public Label svFullName_lbl;
    public Label chFullName_lbl;
    public AnchorPane chCard_anchorPane;
    public AnchorPane saCard_anchorPane;
    public Label accNum2_lbl;
    public Label accNum1_lbl;
    public Label limitAccount_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindData();
        initLatestTransactionsList();
        transaction_listview.setItems(Model.getInstance().getLatestTransaction());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        onAccountSummary();
        onClickCheckingPane();
        onUpdateLatestTransaction();
        chCard_anchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onClickCheckingPane();
            }
        });
        saCard_anchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               onClickSavingPane();
            }
        });
    }
    public void onClickCheckingPane(){
        acc_type.setText("Checking Account");
        accNum1_lbl.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().asString());
        accNum2_lbl.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().asString());
        acc_avail.textProperty().bind(Bindings.concat("$ ").concat(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        acc_overdue.setText(Model.getInstance().getClient().dateCreatedProperty().get().format(formatter));
        limitAccount_lbl.setText("Transaction Limit Per Day:");
        acc_limit.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().transactionLimitProperty().asString());

    }

    public void onClickSavingPane(){
        acc_type.setText("Saving Account");
        accNum1_lbl.textProperty().bind(Model.getInstance().getClient().savingAccountProperty().asString());
        accNum2_lbl.textProperty().bind(Model.getInstance().getClient().savingAccountProperty().asString());
        acc_avail.textProperty().bind(Bindings.concat("$ ").concat(Model.getInstance().getClient().savingAccountProperty().get().balanceProperty().asString()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        acc_overdue.setText(Model.getInstance().getClient().dateCreatedProperty().get().format(formatter));
        limitAccount_lbl.setText("Withdrawal Limit :");
        acc_limit.textProperty().bind(Bindings.concat("$ ").concat(Model.getInstance().getClient().savingAccountProperty().get().withdrawalLimitProperty().asString()));
    }

    public void bindData(){
        double totalBalance = (Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().get() + Model.getInstance().getClient().savingAccountProperty().get().balanceProperty().get());
        total_balance.setText("$ " + String.valueOf(totalBalance));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        login_date.setText(LocalDate.now().format(formatter));
        checking_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        checking_bal.textProperty().bind(Bindings.concat("$ ").concat(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString()));
        chFullName_lbl.textProperty().bind(Bindings.concat(Model.getInstance().getClient().lastNameProperty()).concat(" ").concat(Model.getInstance().getClient().firstNameProperty()));
        ck_overdue_date.setText(Model.getInstance().getClient().dateCreatedProperty().get().format(formatter));
        saving_acc_num.textProperty().bind(Model.getInstance().getClient().savingAccountProperty().get().accountNumberProperty());
        svFullName_lbl.textProperty().bind(Bindings.concat(Model.getInstance().getClient().lastNameProperty()).concat(" ").concat(Model.getInstance().getClient().firstNameProperty()));
        saving_bal.textProperty().bind(Bindings.concat("$ ").concat(Model.getInstance().getClient().savingAccountProperty().get().balanceProperty().asString()));
        sa_overdue_date.setText(Model.getInstance().getClient().dateCreatedProperty().get().format(formatter));
    }


    public void initLatestTransactionsList(){
        if(Model.getInstance().getLatestTransaction().isEmpty()){
            Model.getInstance().setLatestTransaction();
        }
    }

    public void onUpdateLatestTransaction(){
        ObservableList<Transaction> listTrans = FXCollections.observableArrayList();
        Model.getInstance().prepareTransactions(listTrans, 10);
        transaction_listview.setItems(listTrans);
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
    }

    // Method to get Income and Expresses

    private void onAccountSummary(){
        double Income = 0;
        double Expresses = 0;
        if(Model.getInstance().getAllTransactions().isEmpty()){
            Model.getInstance().setAllTransactions();
        }
        for(Transaction transaction : Model.getInstance().getAllTransactions()){
            if(transaction.senderProperty().get().equals(Model.getInstance().getClient().payeeAddressProperty().get())){
                Expresses += transaction.amountMoneyProperty().get();
            }else{
                Income += transaction.amountMoneyProperty().get();
            }
        }
        income_lbl.setText("$ " + Income);
        expenses_lbl.setText("$ " + Expresses);
    }


}
