package com.example.pqbank.Views;

import com.example.pqbank.Controllers.Admin.ClientCellController;
import com.example.pqbank.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ClientCellFactory extends ListCell<Client> {


    @Override
    protected void updateItem(Client client, boolean empty){
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
            ClientCellController clientCellController = new ClientCellController(client);
            fxmlLoader.setController(clientCellController);
            setText(null);
            try{
                setGraphic(fxmlLoader.load());
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


}
