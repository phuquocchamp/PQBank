package com.example.pqbank.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;
    public DatabaseDriver(){
        try{
            this.conn = DriverManager.getConnection("jdbc:sqlite:pqbank.db");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


//    Client Section

    public ResultSet getClientData(String payeeAddress, String Password){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Clients WHERE PayeeAddress = ? " + "AND Password = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            preparedStatement.setString(2, Password);
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    // Get all transaction data from Admin
    public ResultSet getTransactionsData(){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Transactions ORDER BY ID DESC";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getTransactions(String payeeAddress, int Limit){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ? ORDER BY ID DESC LIMIT ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            preparedStatement.setString(2, payeeAddress);
            preparedStatement.setInt(3, Limit);
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    // Method to return Checking Account's Balance

    public double getCheckingAccountBalance(String payeeAddress){
        double Balance = 0;
        try{
            String sql = "SELECT * From checkingAccounts where Owner = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            ResultSet  resultSet = preparedStatement.executeQuery();
            Balance = resultSet.getDouble("Balance");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Balance;
    }
    public void updateBalance(String payeeAddress, double Amount, String operation){
        try{
            String sql = "SELECT * FROM checkingAccounts where Owner = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            ResultSet resultSet = preparedStatement.executeQuery();
            double newBalance = 0;
            if(operation.equals("ADD")){
                newBalance = resultSet.getDouble("Balance") + Amount;
                String sqlUpdate = "UPDATE checkingAccounts set Balance = ? where Owner = ?";
                preparedStatement = this.conn.prepareStatement(sqlUpdate);
                preparedStatement.setDouble(1, newBalance);
                preparedStatement.setString(2, payeeAddress);
                preparedStatement.executeUpdate();
            }
            else{
                if(resultSet.getDouble("Balance") >= Amount){
                    newBalance = resultSet.getDouble("Balance") - Amount;
                    String sqlUpdate = "UPDATE checkingAccounts set Balance = ? where Owner = ?";
                    preparedStatement = this.conn.prepareStatement(sqlUpdate);
                    preparedStatement.setDouble(1, newBalance);
                    preparedStatement.setString(2, payeeAddress);
                    preparedStatement.executeUpdate();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Create and Record new transactions.
    public void newTransactions(String Sender, String Receiver, Double Amount, String Message){
        try{
            String sql = "INSERT INTO Transactions(Sender, Receiver, Amount, Date, Message) VALUES (?, ?, ?, ?, ? )";
            LocalDate dateTrans = LocalDate.now();
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, Sender);
            preparedStatement.setString(2, Receiver);
            preparedStatement.setDouble(3, Amount);
            preparedStatement.setString(4, dateTrans.toString());
            preparedStatement.setString(5, Message);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

//    Admin Section

    public ResultSet getAdminData(String Username, String Password){
       ResultSet resultSet = null;
       try{
           String sql = "SELECT * FROM Admin where Username = ? AND Password = ?";
           PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
           preparedStatement.setString(1, Username);
           preparedStatement.setString(2, Password);
           resultSet = preparedStatement.executeQuery();
       }catch (SQLException e){
           e.printStackTrace();
       }
       return resultSet;
    }

    public void createClient(String fName, String lastName, String pAddress, String password, LocalDate date){
        try{
            String sql = "INSERT INTO " +
                    " Clients (firstName, lastName, payeeAddress, Password, dateCreated)" +
                    " VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, pAddress);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, date.toString());

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void changeClient(String payeeAddress, String firstName, String lastName, String Password){
        try{
            String sql = "UPDATE Clients SET firstName = ?, lastName = ?, Password = ? WHERE payeeAddress = ? ";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, Password);
            preparedStatement.setString(4, payeeAddress);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public String getClientPassword(String payeeAddress){
        String password = "";
        try{
            String sql = "SELECT * FROM Clients WHERE payeeAddress = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            ResultSet resultSet = preparedStatement.executeQuery();
            password = resultSet.getString("Password");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return password;
    }

    public void deleteClient(String payeeAddress){
        try{
            String sql = "DELETE FROM Clients WHERE payeeAddress = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            preparedStatement.executeUpdate();
            String sql1 = "DELETE FROM checkingAccounts WHERE Owner = ?";
            PreparedStatement preparedStatement1 = this.conn.prepareStatement(sql1);
            preparedStatement1.setString(1, payeeAddress);
            preparedStatement1.executeUpdate();
            String sql2 = "DELETE FROM savingAccounts WHERE Owner = ?";
            PreparedStatement preparedStatement2 = this.conn.prepareStatement(sql2);
            preparedStatement2.setString(1, payeeAddress);
            preparedStatement2.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, int tLimit, double balance){
        try{
            String sql = "INSERT INTO " +
                    " checkingAccounts (Owner, accountNumber, transactionLimit, Balance)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, owner);
            preparedStatement.setString(2, number);
            preparedStatement.setInt(3, tLimit);
            preparedStatement.setDouble(4, balance);

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createSavingAccount(String Owner, String Number, double wLimit, double Balance){
        try{
            String sql = "INSERT INTO " +
                    "savingAccounts (Owner, accountNumber, withdrawalLimit, Balance)" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, Owner);
            preparedStatement.setString(2, Number);
            preparedStatement.setDouble(3, wLimit);
            preparedStatement.setDouble(4, Balance);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getAllClientData(){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Clients";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            resultSet = ps.executeQuery();
//            System.out.println(resultSet.getString("dateCreated"));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet searchClient(String payeeAddress){
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM Clients WHERE payeeAddress LIKE '%' || ? || '%'";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, payeeAddress);
            rs = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public void depositChecking(String payeeAddress, double amount){
        try{
            String sql = "UPDATE checkingAccounts SET Balance = ? WHERE Owner = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, payeeAddress);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void depositSaving(String payeeAddress, double amount){
        try{
            String sql = "UPDATE savingAccounts SET Balance = ? WHERE Owner = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, payeeAddress);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


//    Utility Methods
    public int getLastClientId(){
        int id = 0;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM sqlite_sequence where name = ?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, "Clients");
            rs = ps.executeQuery();
            id = rs.getInt("seq");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet getCheckingAccountData(String payeeAddress){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM CheckingAccounts WHERE Owner = ?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, payeeAddress);
            resultSet = ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getSavingAccountData(String payeeAddress){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM SavingAccounts WHERE Owner = ?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, payeeAddress);
            resultSet = ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }


    public int getTotalTransactions(){
        int count = 0;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM sqlite_sequence WHERE name = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, "Transactions");
            resultSet = preparedStatement.executeQuery();
            count = resultSet.getInt("seq");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return count;
    }

    public int getNumberAccount(){
        int count = 0;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM sqlite_sequence WHERE name = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, "Clients");
            resultSet = preparedStatement.executeQuery();
            count = resultSet.getInt("seq");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return count;
    }

}
