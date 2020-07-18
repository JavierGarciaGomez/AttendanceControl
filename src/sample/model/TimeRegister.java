package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeRegister {
    private int id;
    private String userName;
    private String branch;
    private String action;
    private LocalDateTime localDateTime;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    public TimeRegister() {
    }

    public TimeRegister(String userName, String branch, String action) {
        this.userName = userName;
        this.branch = branch;
        this.action = action;
    }

    public TimeRegister(int id, String userName, String branch, String action, LocalDateTime localDateTime) {
        this.id = id;
        this.userName = userName;
        this.branch = branch;
        this.action = action;
        this.localDateTime = localDateTime;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
    public String getBranch() {
        return branch;
    }
    public String getAction() {
        return action;
    }
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    // Other getters
    public String getDateAsString() {
        return dateTimeFormatter.format(this.localDateTime);
    }


    public TimeRegister getLastTimeRegister(String userName) throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE time = " +
                "(SELECT MAX(time) FROM attendanceRegister WHERE username=?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, userName);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int id = resultSet.getInt(1);
        userName = resultSet.getString(2);
        String branch = resultSet.getString(3);
        String action = resultSet.getString(4);
        LocalDateTime localDateTime = resultSet.getTimestamp(5).toLocalDateTime();
        return new TimeRegister(id, userName, branch, action, localDateTime);
    }


    public ObservableList<TimeRegister> getTimeRegisters() throws SQLException {
        ObservableList<TimeRegister> timeRegisters = FXCollections.observableArrayList();

        // SQL
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Loop the resultset
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            userName = resultSet.getString(2);
            String branch = resultSet.getString(3);
            String action = resultSet.getString(4);
            LocalDateTime localDateTime = resultSet.getTimestamp(5).toLocalDateTime();
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, localDateTime);
            timeRegisters.add(timeRegister);
        }
        return timeRegisters;
    }

    public ObservableList<TimeRegister> getTimeRegistersforUser() throws SQLException {
        ObservableList<TimeRegister> timeRegisters = FXCollections.observableArrayList();

        // SQL
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE username=?";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, userName);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Loop the resultset
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            userName = resultSet.getString(2);
            String branch = resultSet.getString(3);
            String action = resultSet.getString(4);
            LocalDateTime localDateTime = resultSet.getTimestamp(5).toLocalDateTime();
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, localDateTime);
            timeRegisters.add(timeRegister);
        }
        return timeRegisters;
    }



    /*
    CRUD
     */
    public void insertTimeRegister() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "INSERT INTO attendanceRegister (userName, branch, action, time) VALUES (?, ?, ?, current_timestamp())";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.branch);
        preparedStatement.setString(3, this.action);
        System.out.println(preparedStatement);
        boolean isSuccessful = preparedStatement.execute();
        connectionDB.closeConnection();
    }

    public void updateTimeRegister() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "UPDATE attendanceRegister SET username = ?, branch=?, action=?, time=? WHERE id=?";

        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.branch);
        preparedStatement.setString(3, this.action);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(this.localDateTime));
        preparedStatement.setInt(5, this.id);
        System.out.println(preparedStatement);
        boolean isSuccessful = preparedStatement.execute();
        System.out.println("IS SUCCESSFUL "+isSuccessful);
        connectionDB.closeConnection();
    }


    // Other methods

    public boolean isDateAndActionRegistered() throws SQLException {
        // Converting LocalDateTime as MySql date
        LocalDate localDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

        // Connecting to DATABASE
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE userName = ? " +
                "AND action = ? AND CAST(time AS DATE) = DATE(?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.action);
        preparedStatement.setDate(3, sqlDate);
        System.out.println(preparedStatement);

        // Getting the resultset
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isRegistered = resultSet.next();
        resultSet.close();
        connectionDB.closeConnection();

        return isRegistered;
    }

    // To String
    @Override
    public String toString() {
        return this.userName + " " + this.action + " en " + this.branch + ", el " + dateTimeFormatter.format(this.localDateTime);
    }

}
