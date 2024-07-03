package com.zoramarcinova.audiometry_v3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AudiometryApp extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {


//        float[] levels = {20, 30, 50};
//        Test test = null;
//        try {
//            test = new Test(1,5, levels);
//        } catch (UnsupportedAudioFileException e) {
//            throw new RuntimeException(e);
//        } catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        }
//        //test.initiateSoundList();
//        //test.print();
//        test.test();
//        test.writeResults();


//
        FXMLLoader fxmlLoader = new FXMLLoader(AudiometryApp.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 670, 500);
        IndexController indexController = fxmlLoader.getController();

        //indexController.setStage(stage);
        stage.getIcons().add(new Image(AudiometryApp.class.getResource("appicon.png").openStream()));
        stage.setTitle("Audiometria");
        stage.setScene(scene);
        stage.show();
    }


}