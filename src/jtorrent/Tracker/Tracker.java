package jtorrent.Tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import jtorrent.Communication.Requests.*;

public class Tracker {

    private ArrayList<PeerThread> connectedPeers = new ArrayList<PeerThread>();
    private ServerSocket serverSocket;
    private HashMap<String, PeerThread> peerIndex = new HashMap<String, PeerThread>();

    public Tracker() {
        try {
            this.serverSocket = new ServerSocket(8080);
            System.out.println("Tracker listening at 8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptIncomingConnection() throws IOException, ClassNotFoundException, SQLException {
        Socket socket = this.serverSocket.accept();
        {
            PeerThread peerThread = new PeerThread(socket, this);
            this.connectedPeers.add(peerThread);
            Thread peer = new Thread(peerThread);
            peer.start();
        }
    }

    public void requestSeed(ArrayList<String> peers, String leecherIp, int port, String merkleRoot) {
        for (String peer : peers) {
            peerIndex.get(peer).sendRequest(new SeedRequest(leecherIp, port, merkleRoot));
        }
    }

    public void addToPeerIndex(String username, PeerThread peerThread) {
        this.peerIndex.put(username, peerThread);
    }

    public void removeConnection(String username, PeerThread peerThread) {
        this.peerIndex.remove(username);
        connectedPeers.remove(peerThread);
    }

    public static void main(String[] args) {
        Tracker tracker = new Tracker();
        while (true) {
            try {
                tracker.acceptIncomingConnection();
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void runTracker() {
        while (true) {
            try {
                this.acceptIncomingConnection();
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}