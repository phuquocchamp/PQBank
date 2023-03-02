package com.example.pqbank.Controllers.Admin;

import com.example.pqbank.Controllers.AlertBox;
import com.example.pqbank.Models.Client;
import com.example.pqbank.Models.Model;
import com.example.pqbank.Views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listView;
    public TextField firstName_fld;
    public TextField lastName_fld;
    public TextField password_fld;
    public Button edit_btn;
    public TextField searchField_fld;
    public Button search_btn;

    public Button delete_btn;

    private Client client;

//    private ClientCellFactory clientCellFactory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientsList();
        clients_listView.setItems(Model.getInstance().getClients());
        clients_listView.setCellFactory(e -> new ClientCellFactory());

        search_btn.setOnAction(e -> onSearchClient());
        edit_btn.setOnAction(event ->onEditClient());
        delete_btn.setOnAction(event -> onDeleteClient());
    }


    public void initClientsList(){
        if(Model.getInstance().getClients().isEmpty()){
            Model.getInstance().setClients();
        }
    }

    public void onSearchClient(){
        ResultSet rs = Model.getInstance().getDatabaseDriver().searchClient(searchField_fld.getText());
        try {
            if(rs.isBeforeFirst()){
                if(rs.getString("payeeAddress").equals(searchField_fld.getText())){
                    ObservableList<Client> searchResults = Model.getInstance().searchClient(searchField_fld.getText());
                    clients_listView.setItems(searchResults);
                    clients_listView.setCellFactory(e -> new ClientCellFactory());
                    client = searchResults.get(0);
                    firstName_fld.setText(client.firstNameProperty().get());
                    lastName_fld.setText(client.lastNameProperty().get());
                    password_fld.setText(Model.getInstance().getDatabaseDriver().getClientPassword(client.payeeAddressProperty().get()));
                }
            }else{
                AlertBox.display("Failed", "Wrong Payee Address's Client. Please Enter Valid Account!");
                searchField_fld.setText("");
                firstName_fld.setText("");
                lastName_fld.setText("");
                password_fld.setText("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void onEditClient(){
        String newFirstName = firstName_fld.getText();
        String newLastName = lastName_fld.getText();
        String newPassword = password_fld.getText();
        if(newFirstName == null || newLastName == null || newPassword == null){
            AlertBox.display("Failed", "Please choose client account first !");

        }else{
            // Change Client GUI cell.
            client.firstNameProperty().set(newFirstName);
            client.lastNameProperty().set(newLastName);

            // Change Client on Database.
            Model.getInstance().getDatabaseDriver().changeClient(client.payeeAddressProperty().get(), newFirstName, newLastName, newPassword);
            AlertBox.display("Successful!", "Changed Account Successful.");

            // On update listview form
            onUpdateClientListView();
            // Empty Field.
           onEmpty();
        }

    }

    public void onDeleteClient(){
        Model.getInstance().getDatabaseDriver().deleteClient(client.payeeAddressProperty().get());
        onUpdateClientListView();
    }

    public void onUpdateClientListView(){
        ObservableList<Client> listClients = FXCollections.observableArrayList();
        Model.getInstance().prepareClients(listClients);
        clients_listView.setItems(listClients);
        clients_listView.setCellFactory(e -> new ClientCellFactory());
    }

    public void onEmpty(){
        firstName_fld.setText("");
        lastName_fld.setText("");
        password_fld.setText("");
    }



}
