package jtorrent.Database;

import jtorrent.Communication.Requests.*;
import java.sql.*;

public class UserTable implements CRUDInterface {
    private Connection connection = null;
    private PreparedStatement stmt = null;

    public UserTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to database");
        }
    }

    @Override
    public Integer Create(Request request) {
        ConnectRequest connectRequest = (ConnectRequest) request;
        try {
            stmt = connection.prepareStatement(
                    "INSERT INTO Users(username, password, currentIP, active, nickname) VALUES(?,?,?,?,?)");
            stmt.setString(1, connectRequest.getUsername());
            stmt.setString(2, connectRequest.getPassword());

            stmt.setString(3, connectRequest.getHostName());
            stmt.setBoolean(4, true);
            stmt.setString(5, connectRequest.getNickname());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Most Probably an integrity constraint was broken");
            return 0;
        }
    }

    @Override
    public ResultSet Retrieve(Request request) {
        return null;
    }

    @Override
    public Integer Update(Request request) {
        ConnectRequest connectRequest = (ConnectRequest) request;
        try {
            stmt = connection.prepareStatement("UPDATE Users SET currentIP=?,active=? WHERE username=? AND password=?");
            stmt.setString(1, connectRequest.getHostName());
            stmt.setBoolean(2, connectRequest.getActive());
            stmt.setString(3, connectRequest.getUsername());
            stmt.setString(4, connectRequest.getPassword());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in userTable update operation!");
            return 0;
        }
    }

    public void Update(String username, String hostname, Boolean active) {
        try {
            stmt = connection.prepareStatement("UPDATE Users SET currentIP=?,active=? WHERE username=?");
            stmt.setString(1, hostname);
            stmt.setBoolean(2, active);
            stmt.setString(3, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in userTable update operation!");
        }
    }

    @Override
    public void Delete(Request request) {

    }

    public String RecoverPassword(Request request) {
        ConnectRequest connectRequest = (ConnectRequest) request;
        try {
            stmt = connection.prepareStatement("SELECT password FROM Users WHERE username=? AND nickname=?");
            stmt.setString(1, connectRequest.getUsername());
            stmt.setString(2, connectRequest.getNickname());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            } else {
                return "No such user!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving password from database";
        }
    }

}