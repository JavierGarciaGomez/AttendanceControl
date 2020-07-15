package sample.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public TimeRegister() {
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
        userName=resultSet.getString(2);
        String branch = resultSet.getString(3);
        String action = resultSet.getString(4);
        Date date = resultSet.getTimestamp(5);
        TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, date);
        return timeRegister;
    }

    public static void main(String[] args) throws SQLException {

    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return userName +" "+action+" en "+branch+", el "+dtf.format(localDateTime);
    }
}
