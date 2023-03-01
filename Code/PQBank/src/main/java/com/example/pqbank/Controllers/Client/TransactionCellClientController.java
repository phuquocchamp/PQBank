package com.example.pqbank.Controllers.Client;

import com.example.pqbank.Models.Model;
import com.example.pqbank.Models.Transaction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellClientController implements Initializable {


    public FontAwesomeIconView getIn_icon;
    public FontAwesomeIconView getOut_icon;
    public Label dateTime;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;
    public Label message_lbl;
    private final Transaction transaction;
    public TransactionCellClientController(Transaction transaction) {
        this.transaction = transaction;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateTime.textProperty().bind(transaction.dateProperty().asString());
        sender_lbl.textProperty().bind(transaction.senderProperty());
        receiver_lbl.textProperty().bind(transaction.receiverProperty());
        message_lbl.textProperty().bind(transaction.messageProperty());
        amount_lbl.textProperty().bind(transaction.amountMoneyProperty().asString());
        onTransactionIcons();

    }



    private void onTransactionIcons(){
        if(transaction.senderProperty().get().equals(Model.getInstance().getClient().payeeAddressProperty().get())){
            getIn_icon.setFill(Color.rgb(240,240,240));
            getOut_icon.setFill(Color.RED);
            sender_lbl.setStyle("-fx-text-fill: orange");
            receiver_lbl.setStyle("-fx-text-fill: #99CC66");

        }
        else{
            getIn_icon.setFill(Color.GREEN);
            getOut_icon.setFill(Color.rgb(240,240,240));
            sender_lbl.setStyle("-fx-text-fill: #99CC66");
            receiver_lbl.setStyle("-fx-text-fill: orange");
        }
    }
}
