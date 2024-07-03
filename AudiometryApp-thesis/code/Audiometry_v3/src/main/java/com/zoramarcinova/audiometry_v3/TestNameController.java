package com.zoramarcinova.audiometry_v3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class TestNameController {

    private Test test;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField ageTextField;
    private Stage stage;


    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public void startTest(javafx.event.ActionEvent actionEvent) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (nameTextField.getText().length() != 0 && surnameTextField.getText().length() != 0 && ageTextField.getText() != null && isInteger(ageTextField.getText())){
//            float[] levels = {20, 30, 50};
//            test = new Test(1,5, levels);

            test.getUser().setName(nameTextField.getText().trim());
            test.getUser().setSurname(surnameTextField.getText().trim());
            test.getUser().setAge(Integer.parseInt(ageTextField.getText()));

            FXMLLoader fxmlLoader = new FXMLLoader(AudiometryApp.class.getResource("instructions.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 670, 500);
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            InstructionsController instructionsController = fxmlLoader.getController();
            instructionsController.setTest(test);

            stage.setScene(scene);
            stage.show();

        }


    }



    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
