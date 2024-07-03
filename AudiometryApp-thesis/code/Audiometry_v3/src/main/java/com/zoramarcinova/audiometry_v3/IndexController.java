package com.zoramarcinova.audiometry_v3;

import com.zoramarcinova.audiometry_v3.AudiometryApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class IndexController implements Initializable {


    private Stage stage;
//    @FXML
//    private Button indexTestButton;
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }

    @FXML
    private Button exitButton;



    public void exitApp(javafx.event.ActionEvent actionEvent) throws IOException {
        Platform.exit();
    }

    public void startTest(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AudiometryApp.class.getResource("test-name-age.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 670, 500);
        TestNameController testNameController = fxmlLoader.getController();

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("./zvukove_urovne.config")) {
            properties.load(fileInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String levelsString = properties.getProperty("levels");
        if (levelsString == null) {
            throw new RuntimeException("Property 'levels' not found in the configuration file");
        }
        String[] levelsStringArray = levelsString.split(",");
        int[] levels = new int[levelsStringArray.length];
        for (int i = 0; i < levelsStringArray.length; i++) {
            levels[i] = Integer.parseInt(levelsStringArray[i].trim());
        }

        //int[] levels = { 20, 30, 40, 50};
        try {
            testNameController.setTest(new Test(1,5, levels));
        } catch (UnsupportedAudioFileException | LineUnavailableException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //exitButton.setFocusTraversalKeysEnabled(false);
    }
}
