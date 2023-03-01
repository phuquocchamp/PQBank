package com.example.pqbank.Controllers;



import javafx.scene.control.Label;
public class AlertController{
    public Label Inform_lbl;
    public Label description_lbl;

    public void setMessageSection(String text){
        Inform_lbl.setText(text);
    }
    public void setMessageDescription(String text){ description_lbl.setText(text);}

}
