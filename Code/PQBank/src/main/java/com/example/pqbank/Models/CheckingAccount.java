package com.example.pqbank.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CheckingAccount extends Account{
    private final IntegerProperty transactionLimit;

    public CheckingAccount(String Owner, String accNumber, double balance, int transactionLimit) {
        super(Owner, accNumber, balance);
        this.transactionLimit = new SimpleIntegerProperty(this, "Transaction Limit", transactionLimit);
    }

    public IntegerProperty transactionLimitProperty(){
        return transactionLimit;
    }


    @Override
    public String toString(){
        return accountNumberProperty().get();
    }
}
