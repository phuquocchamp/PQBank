package com.example.pqbank.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Transaction {
    private final StringProperty sender;
    private final StringProperty receiver;
    private final DoubleProperty amountMoney;

    private final ObjectProperty<LocalDate> date;
    private final StringProperty message;


    public Transaction(String sender, String receiver, double amount, LocalDate date, String message){
        this.sender = new SimpleStringProperty(sender);
        this.receiver = new SimpleStringProperty(receiver);
        this.amountMoney = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.message = new SimpleStringProperty(message);
    }

    public StringProperty senderProperty(){
        return this.sender;
    }

    public StringProperty receiverProperty(){return this.receiver;}

    public StringProperty messageProperty(){
        return this.message;
    }
    public DoubleProperty amountMoneyProperty(){
        return this.amountMoney;
    }
    public ObjectProperty<LocalDate> dateProperty(){
        return this.date;
    }
}
