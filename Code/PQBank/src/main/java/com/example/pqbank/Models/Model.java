package com.example.pqbank.Models;


import com.example.pqbank.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;

    // Utility Methods.

    // Client Data Section.
    private final ObservableList<Transaction> latestTransaction;
    private final ObservableList<Transaction> allTransactions;
    private final Client client;
    private boolean clientLoginSuccessFlag;
    // Admin Data Section.
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;
    private final ObservableList<Transaction> allTransactionAdmin;

    private Model(){
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        // Client Data Section.
        this.clientLoginSuccessFlag = false;
        this.client = new Client("", "", "", null,null,null );
        this.latestTransaction = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();

        // Admin Data Section.
        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
        this.allTransactionAdmin = FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory(){
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver(){
        return databaseDriver;
    }

    // Client Method Section.
    public boolean getClientLoginSuccessFlag(){
        return this.clientLoginSuccessFlag;
    }
    public void setClientLoginSuccessFlag(boolean flag){
        this.clientLoginSuccessFlag = flag;
    }

    public Client getClient(){
        return client;
    }

    public void evaluateClientCred(String payeeAddress, String password){
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;

        ResultSet resultSet = databaseDriver.getClientData(payeeAddress, password);
        try{
            if(resultSet.isBeforeFirst()){
                this.client.firstNameProperty().set(resultSet.getString("firstName"));
                this.client.lastNameProperty().set(resultSet.getString("lastName"));
                this.client.payeeAddressProperty().set(resultSet.getString("payeeAddress"));
                String[] dateTrans = resultSet.getString("dateCreated").split("-");
                LocalDate localDate = LocalDate.of(Integer.parseInt(dateTrans[0]), Integer.parseInt(dateTrans[1]), Integer.parseInt(dateTrans[2]));
                this.client.dateCreatedProperty().set(localDate);
                checkingAccount = getCheckingAccount(payeeAddress);
                savingAccount = getSavingAccount(payeeAddress);
                this.client.checkingAccountProperty().set(checkingAccount);
                this.client.savingAccountProperty().set(savingAccount);
                this.clientLoginSuccessFlag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void prepareTransactions(ObservableList<Transaction> Transactions, int Limit){
        ResultSet resultSet = databaseDriver.getTransactions(this.client.payeeAddressProperty().get(), Limit);
        try{
            while (resultSet.next()){
                String Sender = resultSet.getString("Sender");
                String Receiver = resultSet.getString("Receiver");
                double Amount = resultSet.getDouble("Amount");
                String date = resultSet.getString("Date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateTrans = LocalDate.parse(date, formatter);
                String Message = resultSet.getString("Message");
                Transactions.add(new Transaction(Sender, Receiver, Amount, dateTrans, Message));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setLatestTransaction(){
        prepareTransactions(this.latestTransaction, 8);

    }

    public ObservableList<Transaction> getLatestTransaction(){
        return latestTransaction;
    }

    public void setAllTransactions(){
        prepareTransactions(this.allTransactions, -1);
    }

    public ObservableList<Transaction> getAllTransactions(){
        return allTransactions;
    }

    // Admin Method Section.

    public ObservableList<Transaction> getAllTransactionAdmin(){
        return allTransactionAdmin;
    }
    public void setAllTransactionAdmin(){
        prepareTransactionsAdmin(this.allTransactionAdmin);
    }

    public boolean getAdminLoginSuccessFlag(){
        return adminLoginSuccessFlag;
    }
    public void setAdminLoginSuccessFlag(boolean Flag){
        this.adminLoginSuccessFlag =Flag;
    }
    public void evaluateAdminCred(String Username, String Password){
        ResultSet resultSet = this.databaseDriver.getAdminData(Username, Password);
        try{
            if(resultSet.isBeforeFirst()){
                this.adminLoginSuccessFlag = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public ObservableList<Client> getClients(){
        return clients;
    }
    public void setClients(){
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet = databaseDriver.getAllClientData();
        try{
            while(resultSet.next()){
                String fName = resultSet.getString("firstName");
                String lName = resultSet.getString("lastName");
                String payeeAddress = resultSet.getString("payeeAddress");
                String date = resultSet.getString("dateCreated");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateCreated = LocalDate.parse(date, formatter);
                checkingAccount = getCheckingAccount(payeeAddress);
                savingAccount = getSavingAccount(payeeAddress);
                clients.add(new Client(fName, lName, payeeAddress, checkingAccount, savingAccount, dateCreated));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Client> searchClient(String payeeAddress) {
        ObservableList<Client> searchResult = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchClient(payeeAddress);
        try{
            String fName = resultSet.getString("firstName");
            String lName = resultSet.getString("lastName");
            CheckingAccount checkingAccount = getCheckingAccount(payeeAddress);
            SavingAccount savingAccount = getSavingAccount(payeeAddress);
            String[] dateTrans = resultSet.getString("dateCreated").split("-");
            LocalDate dateCreated = LocalDate.of(Integer.parseInt(dateTrans[0]), Integer.parseInt(dateTrans[1]), Integer.parseInt(dateTrans[2]));
            searchResult.add(new Client(fName, lName,payeeAddress, checkingAccount, savingAccount, dateCreated));

        }catch (SQLException e){
            e.printStackTrace();
        }
        return searchResult;
    }
    // Prepare Client ObservableList
    public void prepareClients(ObservableList<Client> clientsList){
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet = databaseDriver.getAllClientData();
        try{
            while(resultSet.next()){
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String payeeAddress = resultSet.getString("payeeAddress");
                String date = resultSet.getString("dateCreated");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateCreated = LocalDate.parse(date, formatter);
                checkingAccount = getCheckingAccount(payeeAddress);
                savingAccount = getSavingAccount(payeeAddress);
                clientsList.add(new Client(firstName, lastName, payeeAddress, checkingAccount, savingAccount, dateCreated));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // Prepare Transaction to get all.
    public void prepareTransactionsAdmin(ObservableList<Transaction> Transactions){
        ResultSet resultSet = databaseDriver.getTransactionsData();
        try{
            while (resultSet.next()){
                String Sender = resultSet.getString("Sender");
                String Receiver = resultSet.getString("Receiver");
                double Amount = resultSet.getDouble("Amount");
                String date = resultSet.getString("Date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateTrans = LocalDate.parse(date, formatter);
                String Message = resultSet.getString("Message");
                Transactions.add(new Transaction(Sender, Receiver, Amount, dateTrans, Message));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public CheckingAccount getCheckingAccount(String payeeAddress){
        CheckingAccount checkingAccount = null;
        ResultSet resultSet = databaseDriver.getCheckingAccountData(payeeAddress);
        try{
            String accountNumber = resultSet.getString("accountNumber");
            int transactionLimit = resultSet.getInt("transactionLimit");
            double Balance = resultSet.getDouble("Balance");
            checkingAccount = new CheckingAccount(payeeAddress, accountNumber, Balance, transactionLimit);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return checkingAccount;
    }
    public SavingAccount getSavingAccount(String payeeAddress){
        SavingAccount savingAccount = null;
        ResultSet resultSet = databaseDriver.getSavingAccountData(payeeAddress);
        try{
            String accountNumber = resultSet.getString("accountNumber");
            double withdrawalLimit = resultSet.getDouble("withdrawalLimit");
            double Balance = resultSet.getDouble("Balance");
            savingAccount = new SavingAccount(payeeAddress, accountNumber, Balance, withdrawalLimit);
        }catch (SQLException e){
            e.printStackTrace();;
        }
        return savingAccount;
    }

}
