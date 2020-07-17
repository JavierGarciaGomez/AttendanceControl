package sample.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.model.User;
import sample.model.Utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class IncidencesController {
    public TextArea tarText;
    public Button btnSave;
    private User user;
    public void initData(User user) {
        this.user=user;
    }

    public void save(ActionEvent event) {
        try {
            String userName = user.getUser();
            String nowAsText= new Utilities().getNowAsText();
            String incident = tarText.getText();
            String incidentText = userName+" "+nowAsText+" "+incident+"\n";
            FileWriter fileWriter = new FileWriter("res\\incidences.txt", true);
            fileWriter.write(incidentText);
            fileWriter.close();
            new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "Incidencia guardada con éxito");
            Stage thisStage = (Stage) btnSave.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}