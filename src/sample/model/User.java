package sample.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private int id;
    private String name;
    private String lastName;
    private String user;
    private String pass;
    private boolean isActive;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }


    public User(String user) {
        this.user = user;
    }

    public User(String userName, String pass) {
        this.user = userName;
        this.pass = pass;
    }

    public User(int id, String name, String lastName, String user, String pass, boolean isActive) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.user = user;
        this.pass = pass;
        this.isActive = isActive;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean checkLogin() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM users WHERE user = '" + user + "' AND pass ='" + pass + "'";
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isUser = resultSet.next();
        connectionDB.closeConnection();
        return isUser;
    }

    public boolean addUser() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.name);
        preparedStatement.setString(3, this.lastName);
        preparedStatement.setString(4, this.user);
        preparedStatement.setString(5, this.pass);
        preparedStatement.setBoolean(6, this.isActive);

        System.out.println(preparedStatement);
        boolean isSuccessful = preparedStatement.execute();


        connectionDB.closeConnection();

        return isSuccessful;
    }

    public int getMaxID() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT MAX(ID) FROM USERS";
        ResultSet resultSet = connectionDB.executeQuery(sql);
        resultSet.next();
        int maxId=resultSet.getInt(1);
        connectionDB.closeConnection();
        return maxId;
    }

    public boolean checkAvailableId() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM users WHERE id = " + this.id;
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isAvailable = !resultSet.next();
        connectionDB.closeConnection();
        return isAvailable;
    }

    public boolean checkAvailableUser() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM users WHERE user = '" + this.user+"'";
        System.out.println(sql);
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isAvailable = !resultSet.next();
        connectionDB.closeConnection();
        return isAvailable;
    }
}