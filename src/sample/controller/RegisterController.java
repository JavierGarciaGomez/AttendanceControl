package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.model.TimeRegister;
import sample.model.User;

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
            // Getting the data
            String user = lblUser.getText();
            String branch = cboBranch.getSelectionModel().getSelectedItem().toString();
            String action = cboAction.getSelectionModel().getSelectedItem().toString();
            TimeRegister timeRegister = new TimeRegister(user, branch, action);

            boolean isValid=true;

            if(timeRegister.isDateAndActionRegistered()){
                System.out.println("NO SE PUEDE REGISTRAR PORQUE YA HAY ALGO");
            } else{
                if (timeRegister.insertTimeRegister()) {
                }

            }



            // Test if the date is repeated
            // Register new data
            LocalDate now = LocalDate.now();
            newDateToRegister=now.toString();

            System.out.println("datos anteriores: "+lastActionRegistered+" "+lastDateRegistered);
            System.out.println("datos nuevos: "+action+" "+newDateToRegister);

            if(newDateToRegister.equals(lastDateRegistered)&&action.equals(lastActionRegistered)){
                System.out.println("Coinciden las fechas y acción");
            } else{
                System.out.println("No coincide la fecha y acción");
            }










        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }






    }

    public static void main(String[] args) {



    }



}
