package com.example.pqbank.Views;

import com.example.pqbank.Controllers.Admin.AdminController;
import com.example.pqbank.Controllers.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewFactory {

    private AccountType loginAccountType;

    // client views
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionView;
    private AnchorPane accountView;
    private AnchorPane profileView;

    // Admin views
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane createClientView;
    private AnchorPane dashboardAdminView;
    private AnchorPane depositView;

    private AnchorPane clientsView;



    public ViewFactory(){
        this.loginAccountType = AccountType.CLIENT; // View Default In Login Scene.
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem  = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType(){
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType){
        this.loginAccountType = loginAccountType;
    }

    // Client get View
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem(){
        return clientSelectedMenuItem;
    }

    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getTransactionView(){
        if(transactionView == null){
            try{
                transactionView = new FXMLLoader(getClass().getResource("/Fxml/Client/Transaction.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return transactionView;

    }


    public AnchorPane getAccountView(){
        if(accountView == null){
            try {
                accountView = new FXMLLoader(getClass().getResource("/Fxml/Client/Account.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return accountView;
    }

    public AnchorPane getProfileView(){
        if(profileView == null){
            try{
                profileView = new FXMLLoader(getClass().getResource("/Fxml/Client/Profile.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return profileView;
    }


    // Admin Get View
    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem(){
        return adminSelectedMenuItem;
    }

    public AnchorPane getCreateClientView(){
        if(createClientView == null){
            try{
                createClientView = new FXMLLoader(getClass().getResource("/Fxml/Admin/CreateClient.fxml")).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return createClientView;
    }

    public AnchorPane getDashboardAdminView(){
        if(dashboardAdminView == null){
            try{
                dashboardAdminView = new FXMLLoader(getClass().getResource("/Fxml/Admin/DashboardAdmin.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardAdminView;
    }

    public AnchorPane getClientsView(){
        if(clientsView == null){
            try {
                clientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return clientsView;
    }

    public AnchorPane getDepositView(){
        if(depositView == null){
            try {
                depositView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Transaction.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return depositView;
    }


    // Window

    public void showMessageWindow(String messageText1, String messageText2, String...dimensions) {
        StackPane pane = new StackPane();
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        Label mt1 = new Label(messageText1);
        Label mt2 = new Label(messageText2);
        hBox.getChildren().addAll(mt1, mt2);
        pane.getChildren().add(hBox);
        Scene scene = new Scene(pane, 300, 100);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/appIcon.png"))));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Message");
        stage.setScene(scene);
        stage.show();
    }
    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }
    public void showClientWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }




    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/appIcon.png"))));
        stage.setResizable(false);
        stage.setTitle("PQBANK");
        stage.show();
    }
    public void closeStage(Stage stage){
        stage.close();
    }


}
