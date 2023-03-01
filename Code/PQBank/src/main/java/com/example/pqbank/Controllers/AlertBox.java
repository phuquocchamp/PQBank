package com.example.pqbank.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String messageSection, String messageDescription) {
        try {
            FXMLLoader loader = new FXMLLoader(AlertBox.class.getResource("/Fxml/Alert.fxml"));
            Parent root = loader.load();
            AlertController controller = loader.getController();
            controller.setMessageSection(messageSection);
            controller.setMessageDescription(messageDescription);

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(new Scene(root));
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
