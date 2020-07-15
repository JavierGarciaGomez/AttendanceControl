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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cboBranch.getSelectionModel().select(0);
        cboAction.getSelectionModel().select(0);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        lblDateHour.setText(dtf.format(now));


    }

    public void initData(User user) throws SQLException {

        this.user = user;
        System.out.println(user.getName());
        lblUser.setText(user.getUser());
        TimeRegister lastTimeRegister = new TimeRegister().getLastTimeRegister(user.getUser());
        lblLastRegister.setText("Último registro: " + lastTimeRegister.toString());



    }

    public void register(ActionEvent event) {

    }

}
