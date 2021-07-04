package jtorrent.Tracker;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;

import jtorrent.Communication.Requests.*;
import jtorrent.Database.*;

public class PeerThread implements Runnable {
    private Socket socket;
    private Tracker tracker;
    private ObjectInputStream readFromPeer;
    private ObjectOutputStream writeToPeer;
    private String username, password;
    private UserTable userTable = new UserTable();
    private FilesTable filesTable = new FilesTable();

    private ExecutorService executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    PeerThread(Socket socket, Tracker tracker) {
        try {
            this.socket = socket;
            this.tracker = tracker;
            this.readFromPeer = new ObjectInputStream(this.socket.getInputStream());
            this.writeToPeer = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(SeedRequest seedRequest) {
        try {
            System.out.println(seedRequest.getMerkleRoot());
            this.writeToPeer.writeObject(seedRequest);
            System.out.println("sent seed request to " + this.socket.getInetAddress().toString());
        } catch (IOException e) {
            System.out.println("could not send request to peer!");
            e.printStackTrace();
        }
    }

    public void processUpdateRequest(UpdateRequest updateRequest) {
        updateRequest.setHostName(this.socket.getInetAddress().toString());
        // System.out.println(updateRequest.getAddedFileNames()[0]);
        // System.out.println(updateRequest.getAddedMerkleRoots()[0]);
        filesTable.Create(updateRequest);
        filesTable.Delete(updateRequest);
    }

    /**
     * 
     * @param leechRequest incoming leechRequest which contains all the required
     *                     data to connnect with the socket
     * 
     */
    public void processLeechRequest(LeechRequest leechRequest) {
        leechRequest.setHostName(this.socket.getInetAddress().toString());
        ArrayList<String> peerIps = new ArrayList<String>();
        ResultSet rs = filesTable.Retrieve(leechRequest);
        try {
            while (rs.next()) {
                peerIps.add(rs.getString("currentIP"));
                System.out.println(rs.getString("currentIP") + "is potential seeder");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.tracker.requestSeed(peerIps, leechRequest.getHostName(), leechRequest.getPort(),
                leechRequest.getMerkleRoot());
    }

    public void processConnectRequest(ConnectRequest connectRequest) throws IOException {
        connectRequest.setHostName(this.socket.getInetAddress().toString());
        Integer result = null;
        System.out.println("connection from " + connectRequest.getHostName());
        switch (connectRequest.getConnectionType()) {
        case "REGISTER":
            connectRequest.setActive(true);
            result = userTable.Create(connectRequest);
            this.writeToPeer.writeObject(result);
            if (result != 0) {
                this.tracker.addToPeerIndex(connectRequest.getHostName(), this);
                System.out.println(connectRequest.getHostName() + "added to peerIndex");
            }
            break;
        case "LOGIN":
            connectRequest.setActive(true);
            result = userTable.Update(connectRequest);
            this.writeToPeer.writeObject(result);
            if (result != 0) {
                this.tracker.addToPeerIndex(connectRequest.getHostName(), this);
                System.out.println(connectRequest.getHostName() + "added to peerIndex");
            }
            break;
        case "FORGOT PASSWORD":
            connectRequest.setActive(true);
            String password = userTable.RecoverPassword(connectRequest);
            this.writeToPeer.writeObject(password);
            break;
        case "LOGOUT":
            connectRequest.setActive(false);
            connectRequest.setHostName(connectRequest.getUsername());
            result = userTable.Update(connectRequest);
            this.removePeer();
            break;
        }
        if (!result.equals(0)) {
            this.setUsername(connectRequest.getUsername());
            this.setPassword(connectRequest.getPassword());
        }
    }

    public void removePeer() {
        this.tracker.removeConnection(this.username, this);
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Socket Connection with peer" + this.getUsername() + "closed");
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processRequest(Request request) throws IOException {
        switch (request.getRequestType()) {
        case "LEECH":
            processLeechRequest((LeechRequest) request);
            break;
        case "CONNECT":
            processConnectRequest((ConnectRequest) request);
            break;
        case "UPDATE":
            processUpdateRequest((UpdateRequest) request);
            break;
        }
    }

    /**
     * accepts requests coming from clientside peers, and processes them in a
     * threadpool of size 3
     */
    @Override
    public void run() {
        while (this.socket.isConnected()) {
            try {
                Request request = (Request) readFromPeer.readObject();
                System.out.println(request.getRequestType());
                executor.submit(() -> {
                    try {
                        processRequest(request);
                    } catch (IOException e) {
                        System.out.println("Error processing incoming request");
                    }
                });
            } catch (ClassNotFoundException | IOException e) {
                System.out.println(this.username + " has disconnected!");
                this.tracker.removeConnection(this.username, this);
                userTable.Update(this.username, this.username, false);
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /*
     * getter and setters may not be needed, but they're kept for now anyway just in
     * case
     */
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasssword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}