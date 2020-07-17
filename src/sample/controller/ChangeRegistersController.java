package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.ConnectionDB;
import sample.model.TimeRegister;
import sample.model.User;
import sample.model.Utilities;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class ChangeRegistersController implements Initializable {
    public TableView<TimeRegister> tblTable;
    public TableColumn<TimeRegister, Integer> colId;
    public TableColumn<TimeRegister, String> colUserName;
    public TableColumn<TimeRegister, String> colBranch;
    public TableColumn<TimeRegister, String> colAction;
    public TableColumn<TimeRegister, String> colTime;
    public TextField txtId;
    public ComboBox<String> cboUser;
    public ComboBox<String> cboBranch;
    public ComboBox<String> cboAction;
    public Spinner<Integer> spinHour;
    public Spinner<Integer> spinMin;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public DatePicker dtpDatePicker;
    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(User user) {
        this.user = user;
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        this.colTime.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));

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

    @FXML
    private void selectRegister() {
        int index = tblTable.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }
        txtId.setText(colId.getCellData(index).toString());
        cboUser.getSelectionModel().select(colUserName.getCellData(index));
        cboBranch.getSelectionModel().select(colBranch.getCellData(index));
        cboAction.getSelectionModel().select(colAction.getCellData(index));

        // Getting the date and time
        System.out.println(colTime.getCellData(index));
        String mystring = colTime.getCellData(index);
        String[] arr = mystring.split(" ", 2);
        String date = arr[0];   //the
        String time = arr[1];     //quick brown fox

        // Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);

        String[] hourMinutes = time.split(":");
        int hour = Integer.parseInt(hourMinutes[0].trim());
        int min = Integer.parseInt(hourMinutes[1].trim());
        //
        dtpDatePicker.setValue(localDate);
        spinHour.getValueFactory().setValue(hour);
        spinMin.getValueFactory().setValue(min);
    }

    @FXML
    private void save() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String userName = cboUser.getSelectionModel().getSelectedItem();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();
            LocalDate localDate = dtpDatePicker.getValue();
            int hour = spinHour.getValue();
            int min = spinMin.getValue();
            Date date = new Utilities().StringToDate(localDate.toString()+" "+hour+":"+min);
            TimeRegister timeRegister = new TimeRegister(id,userName,branch,action,date);
            timeRegister.updateTimeRegister();
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }



    }

    @FXML
    private void addNew() {
    }

    @FXML
    private void Delete() {
    }
}
