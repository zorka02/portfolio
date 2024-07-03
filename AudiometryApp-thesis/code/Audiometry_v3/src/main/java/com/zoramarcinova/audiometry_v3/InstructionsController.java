package com.zoramarcinova.audiometry_v3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class InstructionsController {

    private Test test;
    private Stage stage;

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }


    public void continueInstruction(javafx.event.ActionEvent actionEvent) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        //System.out.println("Username: " + test.getUser().getUsername() + " Age: " + test.getUser().getAge());

        FXMLLoader fxmlLoader = new FXMLLoader(AudiometryApp.class.getResource("instructions2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 670, 500);
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Instructions2Controller controller = fxmlLoader.getController();
        controller.setTest(test);


        stage.setScene(scene);
        stage.show();


    }

}
