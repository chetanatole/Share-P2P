package jtorrent.Database;

import jtorrent.Communication.Requests.*;

import java.sql.*;
import java.util.ArrayList;

public class FilesTable implements CRUDInterface {
    private Connection connection = null;
    PreparedStatement stmt = null;

    public FilesTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to database");
        }
    }

    @Override
    public Integer Create(Request request) {
        UpdateRequest updateRequest = (UpdateRequest) request;
        try {
            stmt = connection.prepareStatement(
                    "INSERT INTO Files(username,currentIP,merkleRoot,active,filename) values(?,?,?,?,?)");
            stmt.setString(1, updateRequest.getUsername());
            stmt.setString(2, updateRequest.getHostName());
            stmt.setBoolean(4, true);
        } catch (SQLException e) {
            System.out.println(updateRequest.getUsername() + "@" + updateRequest.getHostName()
                    + " tried to add a file entry that broke sql contraints");
        }
        ArrayList<String> addedMerkleRoots = updateRequest.getAddedMerkleRoots();
        ArrayList<String> addedFileNames = updateRequest.getAddedFileNames();
        for (int i = 0; i < addedMerkleRoots.size(); i++) {
            try {
                stmt.setString(3, addedMerkleRoots.get(i));
                stmt.setString(5, addedFileNames.get(i));
                System.out.println("Inside db package");
                System.out.println(stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(updateRequest.getUsername() + "@" + updateRequest.getHostName()
                        + " tried to add a file entry that broke sql contraints");
            }
        }
        return 1;
    }

    @Override
    public ResultSet Retrieve(Request request) {
        LeechRequest leechRequest = (LeechRequest) request;
        try {
            stmt = connection.prepareStatement("SELECT username, currentIP FROM Files WHERE merkleRoot=? AND active=?");
            stmt.setString(1, leechRequest.getMerkleRoot());
            stmt.setBoolean(2, true);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer Update(Request request) {
        return null;
    }

    @Override
    public void Delete(Request request) {
        UpdateRequest updateRequest = (UpdateRequest) request;
        try {
            stmt = connection.prepareStatement("DELETE FROM Files WHERE username=? AND merkleRoot=?");
            stmt.setString(1, updateRequest.getUsername());
        } catch (SQLException e) {
            System.out.println(updateRequest.getUsername() + "@" + updateRequest.getHostName()
                    + " tried to add a file entry that broke sql contraints");
        }
        for (String merkleRoot : updateRequest.getRemovedMerkleRoots()) {
            try {
                stmt.setString(2, merkleRoot);
                System.out.println("Inside db package");
                System.out.println(stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(updateRequest.getUsername() + "@" + updateRequest.getHostName()
                        + " tried to add a file entry that broke sql contraints");
            }
        }
    }
}