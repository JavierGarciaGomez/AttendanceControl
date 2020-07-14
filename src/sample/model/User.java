package sample.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String name;
    private String lastName;
    private String user;
    private String pass;

    public User(int id, String name, String lastName, String user, String pass) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.user = user;
        this.pass = pass;
    }

    public User(String userName, String pass) {
        this.user=userName;
        this.pass=pass;
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
        String sql="SELECT * FROM users WHERE user = '"+user+"' AND pass ='"+pass+"'";
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isUser = resultSet.next();
        connectionDB.closeConnection();
        return isUser;
    }
}
