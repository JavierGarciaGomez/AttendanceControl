package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class TimeRegister {
    private int id;
    private String userName;
    private String branch;
    private String action;
    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public TimeRegister() {
    }

    public TimeRegister(String user, String branch, String action) {
        this.userName = user;
        this.branch = branch;
        this.action = action;
    }

    public TimeRegister(int id, String userName, String branch, String action, Date date) {
        this.id = id;
        this.userName = userName;
        this.branch = branch;
        this.action = action;
        this.date = date;
    }

    public TimeRegister(String userName, String branch, String action, Date date) {
        this(0, userName, branch, action, date);
    }


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

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        String dateAsString = sdf.format(this.date);
        return dateAsString;
    }

    public boolean insertTimeRegister() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "INSERT INTO attendanceRegister (userName, branch, action, time) VALUES (?, ?, ?, current_timestamp())";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.branch);
        preparedStatement.setString(3, this.action);
        System.out.println(preparedStatement);
        boolean isSuccessful = preparedStatement.execute();
        connectionDB.closeConnection();
        return isSuccessful;
    }

    public boolean updateTimeRegister() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "UPDATE attendanceRegister SET username = ?, branch=?, action=?, time=? WHERE id=?";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.branch);
        preparedStatement.setString(3, this.action);
        preparedStatement.setDate(4, (java.sql.Date) this.date);
        System.out.println(preparedStatement);
        boolean isSuccessful = preparedStatement.execute();
        connectionDB.closeConnection();
        return isSuccessful;
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
        Date date = resultSet.getTimestamp(5);
        TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, date);
        return timeRegister;
    }

    // pruebas
    public static void main(String[] args) throws SQLException, ParseException {
        String userName = "Mat";
        String branch = "Urban";
        String action = "Entrada";

        String originalDateAsString = "2020-07-16";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(originalDateAsString));
        calendar.add(Calendar.DATE, 1);
        String maxDateAsString = sdf.format(calendar.getTime());

        System.out.println(originalDateAsString + " " + maxDateAsString);

        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE userName = ? " +
                "AND time > ? AND time < ?";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, originalDateAsString);
        preparedStatement.setString(3, maxDateAsString);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        int id = resultSet.getInt(1);
        userName = resultSet.getString(2);
        branch = resultSet.getString(3);
        action = resultSet.getString(4);
        Date date = resultSet.getTimestamp(5);
        TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, date);
        System.out.println(timeRegister);
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return userName + " " + action + " en " + branch + ", el " + dtf.format(localDateTime);
    }

    public boolean isDateAndActionRegistered() throws ParseException, SQLException {
        // Getting dates as string
        this.date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        this.date = calendar.getTime();
        String originalDateAsString = sdf.format(this.date);
        calendar.setTime(sdf.parse(originalDateAsString));
        calendar.add(Calendar.DATE, 1);
        String maxDateAsString = sdf.format(calendar.getTime());
        System.out.println(originalDateAsString + " " + maxDateAsString);

        // Connecting to DATABASE
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE userName = ? " +
                "AND action = ? AND time > ? AND time < ?";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.action);
        preparedStatement.setString(3, originalDateAsString);
        preparedStatement.setString(4, maxDateAsString);
        System.out.println(preparedStatement);

        // Getting the resultset
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isRegistered = resultSet.next();
        resultSet.close();
        connectionDB.closeConnection();

        return isRegistered;
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
            Date date = resultSet.getTimestamp(5);
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, date);
            timeRegisters.add(timeRegister);
        }
        return timeRegisters;
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
            Date date = resultSet.getTimestamp(5);
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, date);
            timeRegisters.add(timeRegister);
        }
        return timeRegisters;
    }
}
