package com.example.pqbank.Views;

import com.example.pqbank.Controllers.Client.TransactionCellClientController;
import com.example.pqbank.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class TransactionCellFactory extends ListCell<Transaction> {
    @Override
    protected void updateItem(Transaction transaction, boolean empty){{
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/TransactionCellClient.fxml"));
            TransactionCellClientController controller = new TransactionCellClientController(transaction);
            fxmlLoader.setController(controller);
            setText(null);
            try{
                setGraphic(fxmlLoader.load());
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    }
}
