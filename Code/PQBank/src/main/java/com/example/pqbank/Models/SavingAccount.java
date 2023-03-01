package com.example.pqbank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingAccount extends Account{
    private final DoubleProperty withdrawalLimit;

    public SavingAccount(String Owner, String accNumber, double balance, double withdrawalLimit) {
        super(Owner, accNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawalLimit);
    }

    public DoubleProperty withdrawalLimitProperty(){
        return withdrawalLimit;
    }

    @Override
    public String toString(){
        return accountNumberProperty().get();
    }
}
