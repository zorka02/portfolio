package com.zoramarcinova.audiometry_v3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class AlertBoxController {


    @FXML
    private Text notifText;

    @FXML
    private Button okButton;


    public Text getNotifText() {
        return notifText;
    }

    public void setNotifText(Text notifText) {
        this.notifText = notifText;
    }

    public Button getOkButton() {
        return okButton;
    }

    public void setOkButton(Button okButton) {
        this.okButton = okButton;
    }
}
