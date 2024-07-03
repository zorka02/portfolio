package com.zoramarcinova.audiometry_v3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBox {



    public static void display(String filename) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(AudiometryApp.class.getResource("appicon.png").openStream()));
        stage.setTitle("!");

        FXMLLoader fxmlLoader = new FXMLLoader(AudiometryApp.class.getResource("alertbox.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 150);

        AlertBoxController alertBoxController = fxmlLoader.getController();

        alertBoxController.getNotifText().setText("Upozornenie: Súbor " + filename + " je otvorený inou aplikáciou. Pred testovaním zavrite súbor.");
        alertBoxController.getOkButton().setOnAction(actionEvent -> stage.close());

        stage.setScene(scene);
        stage.showAndWait();


    }
}
