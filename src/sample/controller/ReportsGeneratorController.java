package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.TimeRegister;
import sample.model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsGeneratorController implements Initializable {
    public TableView tblTable;
    public TableColumn<TimeRegister, Integer> colId;
    public TableColumn <TimeRegister, String> colUserName;
    public TableColumn <TimeRegister, String> colBranch;
    public TableColumn <TimeRegister, String> colAction;
    public TableColumn <TimeRegister, String> colTime;
    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(User user) {
        this.user = user;
        this.colId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colUserName.setCellValueFactory(new PropertyValueFactory("userName"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory("branch"));
        this.colAction.setCellValueFactory(new PropertyValueFactory("action"));
        this.colTime.setCellValueFactory(new PropertyValueFactory("dateAsString"));

        loadTable();
    }

    private void loadTable() {
        try {
            TimeRegister timeRegister = new TimeRegister(user.getUser(), "", "");

            ObservableList<TimeRegister> timeRegisters = timeRegister.getTimeRegisters();
            this.tblTable.setItems(timeRegisters);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
