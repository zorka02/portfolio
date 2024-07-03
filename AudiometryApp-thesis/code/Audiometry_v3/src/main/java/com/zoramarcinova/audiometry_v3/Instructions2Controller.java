package com.zoramarcinova.audiometry_v3;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystemException;
import java.nio.file.StandardOpenOption;

public class Instructions2Controller {

    private Test test;
    private Stage stage;

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }


    public void startTest(javafx.event.ActionEvent actionEvent) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        //System.out.println("Username: " + test.getUser().getUsername() + " Age: " + test.getUser().getAge());

        File file = new File("vysledky_tabulky.csv");
        if (file.exists()) {
            try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
                 FileLock lock = channel.tryLock()) {
                if (lock != null) {
                    //System.out.println("File not used.");
                    lock.release();
                }
                else {
                    System.out.println("File used.");
                    return;
                }
            } catch (FileSystemException e) {
                // File is already locked
                //System.out.println("Exception");
                AlertBox.display("vysledky_tabulky.csv");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }



//        File file2 = new File("vysledky_priebeh_testu.txt");
//        try (FileChannel channel = FileChannel.open(file2.toPath(), StandardOpenOption.WRITE);
//             FileLock lock = channel.tryLock()) {
//            if (lock != null) {
//                //System.out.println("File not used.");
//                lock.release();
//            }
//            else {
//                System.out.println("File used.");
//                return;
//            }
//        } catch (FileSystemException e) {
//            // File is already locked
//            //System.out.println("Exception");
//            AlertBox.display("vysledky_priebeh_testu.txt");
//            return;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }


        FXMLLoader fxmlLoader = new FXMLLoader(AudiometryApp.class.getResource("test-sequence.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 670, 500);
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        TestSequenceController testSequenceController = fxmlLoader.getController();
        testSequenceController.setTest(test);
        testSequenceController.getTest().test();

        stage.setOnCloseRequest(event -> {
            event.consume();
            //test.getClip().close();

            test.stopPlaying();
            stage.close();
        });

        FXMLLoader fxmlLoader2 = new FXMLLoader(AudiometryApp.class.getResource("test-end.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load(), 670, 500);

        test.isTestFinished().addListener((v, oldValue, newValue) -> {


            try {
                test.recordResults();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Platform.runLater(() -> {
                stage.setOnCloseRequest(null);
                stage.setScene(scene2);
                stage.show();
            });

        });

        stage.setScene(scene);
        stage.show();


    }

}
