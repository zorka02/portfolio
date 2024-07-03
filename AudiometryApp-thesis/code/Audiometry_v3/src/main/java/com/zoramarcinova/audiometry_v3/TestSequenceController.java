package com.zoramarcinova.audiometry_v3;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestSequenceController {


    private Test test;


    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }


    public void pressHeardButton(javafx.event.ActionEvent actionEvent) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
       test.markPressButton();

    }

    
}
