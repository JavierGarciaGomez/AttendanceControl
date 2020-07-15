package sample.model;

import java.sql.*;

public class ConnectionDB {

    private Connection connection;
    private final String host = "localhost";
    private final String dataBase = "exercises";
    private final String user= "test";
    private final String password= "secret";
    private final String stringConnection = "jdbc:mysql://"+host+"/"+dataBase;


    public ConnectionDB() throws SQLException {
        connection = DriverManager.getConnection(stringConnection, user, password);
        connection.setAutoCommit(true);
    }


    public Connection getConnection() {
        return connection;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    public int update(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}
