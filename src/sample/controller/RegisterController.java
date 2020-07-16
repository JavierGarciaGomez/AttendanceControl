package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.model.TimeRegister;
import sample.model.User;
import sample.model.Utilities;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public Button btnRegister;
    public Button btnCancel;
    public Label lblUser;
    public ComboBox cboBranch;
    public ComboBox cboAction;
    public Label lblDateHour;
    public Label lblLastRegister;
    private User user;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private String lastDateRegistered;
    private String lastActionRegistered;
    private String newDateToRegister;
    private String newActiontoRegister;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cboBranch.getSelectionModel().select(0);
        cboAction.getSelectionModel().select(0);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        lblDateHour.setText(dtf.format(now));
    }

    public void initData(User user) {
        this.user = user;
        lblUser.setText(user.getUser());

        try {
            TimeRegister lastTimeRegister = new TimeRegister().getLastTimeRegister(user.getUser());
            lblLastRegister.setText("Último registro: " + lastTimeRegister.toString());
            cboBranch.getSelectionModel().select(lastTimeRegister.getBranch());
            String action = lastTimeRegister.getAction();
            action = action.equalsIgnoreCase("Entrada") ? "Salida" : "Entrada";
            cboAction.getSelectionModel().select(action);

            // Save lastDay and lastAction
            lastDateRegistered=sdf.format(lastTimeRegister.getDate());
            lastActionRegistered=lastTimeRegister.getAction();
            System.out.println(lastDateRegistered+" "+lastActionRegistered);




        } catch (SQLException throwables) {
            lblLastRegister.setText("Último registro: " + "No se tienen registros previos");
        }


    }

    public void register(ActionEvent event) {
        try {
            // Fields
            boolean isValid=true;
            String errorList ="No se ha podido registrar el usuario, porque se encontraron los siguientes errores:\n";
            // Getting the data
            String user = lblUser.getText();
            String branch = cboBranch.getSelectionModel().getSelectedItem().toString();
            String action = cboAction.getSelectionModel().getSelectedItem().toString();
            TimeRegister timeRegister = new TimeRegister(user, branch, action);
            if(timeRegister.isDateAndActionRegistered()){
                errorList+="Ya se cuenta con un registro de "+action+" de "+user+" con fecha de hoy";
                isValid=false;
            }

            // Check if the action is correct
            if(action.equals(lastActionRegistered)){
                String error = "Tú última acción registrada fue también una "+action+". ¿Estás seguro que quieres " +
                        "registrarlo?";
                boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de querer continuar?", error);
                if(!answer) return;
            } else{
                System.out.println("No coincide la fecha y acción");
            }

            if(isValid){
                timeRegister.insertTimeRegister();
                new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "Información guardada con éxito");
                Stage thisStage = (Stage) btnCancel.getScene().getWindow();
                thisStage.close();

            }            else{
                new Utilities().showAlert(Alert.AlertType.ERROR, "Error de registro", errorList);
            }


        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

    }


    public void cancel(ActionEvent event) {
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        thisStage.close();

    }
}
