package com.example.pqbank.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final ObjectProperty<CheckingAccount> checkingAccount;
    private final ObjectProperty<SavingAccount> savingAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    public Client(String firstName, String lastName, String payeeAddress, CheckingAccount checkingAccount, SavingAccount savingAccount, LocalDate dateCreated){
        this.firstName = new SimpleStringProperty(this,"Fist Name", firstName);
        this.lastName = new SimpleStringProperty(this,"Last Name", lastName);
        this.payeeAddress = new SimpleStringProperty(this, "Payee Address", payeeAddress);
        this.checkingAccount = new SimpleObjectProperty<>(this,"Checking Account", checkingAccount);
        this.savingAccount = new SimpleObjectProperty<>(this,"Saving Account", savingAccount);
        this.dateCreated = new SimpleObjectProperty<>(this, "date", dateCreated);
    }


    public StringProperty firstNameProperty(){
        return this.firstName;
    }

    public StringProperty lastNameProperty(){
        return this.lastName;
    }

    public StringProperty payeeAddressProperty(){
        return this.payeeAddress;
    }

    public ObjectProperty<CheckingAccount> checkingAccountProperty(){return this.checkingAccount;}

    public ObjectProperty<SavingAccount> savingAccountProperty(){
        return this.savingAccount;
    }
    public ObjectProperty<LocalDate> dateCreatedProperty(){ return this.dateCreated; }


}


