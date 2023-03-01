package com.example.pqbank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Account {
    private final StringProperty Owner;
    private final StringProperty accNumber;
    private final DoubleProperty balance;

    public Account(String Owner, String accNumber, double balance){
        this.Owner = new SimpleStringProperty(this, "Owner", Owner);
        this.accNumber = new SimpleStringProperty(this, "Account Number", accNumber);
        this.balance = new SimpleDoubleProperty(this, "Balance", balance);
    }


    public StringProperty ownerProperty(){
        return Owner;
    }

    public StringProperty accountNumberProperty(){ return accNumber;
    }

    public DoubleProperty balanceProperty(){
        return balance;
    }

    public void setBalance(double Amount){
        this.balance.set(Amount);
    }
}
