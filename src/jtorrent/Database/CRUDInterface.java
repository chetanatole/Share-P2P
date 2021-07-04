package jtorrent.Database;

import jtorrent.Communication.Requests.Request;
import java.sql.*;

public interface CRUDInterface {
    String password = "Prem@116";
    String username = "prem";
    String URL = "jdbc:mysql://localhost:3306/P2P";

    public Integer Create(Request request);

    public ResultSet Retrieve(Request request);

    public Integer Update(Request request);

    public void Delete(Request request);
}