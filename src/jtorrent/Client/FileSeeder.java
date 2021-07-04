package jtorrent.Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.HashMap;

import jtorrent.Communication.P2PMessages.*;
import jtorrent.Communication.Requests.SeedRequest;

public class FileSeeder implements Runnable {

    private String merkleRoot;
    private File rootDirectory;
    private Socket peerEndPoint;
    private ObjectOutputStream writeToPeer = null;
    private ObjectInputStream readFromPeer = null;
    File metaDataFile = null;
    HashMap<String, String> metaDataHash = null;
    private Integer assignedIndex = null, numPeers = null, numfileRecieved = null;
    private String[] totalPieces = null;

    public FileSeeder(SeedRequest seedRequest, String rootDirectory) {
        try {
            this.merkleRoot = seedRequest.getMerkleRoot();
            this.rootDirectory = Paths.get(rootDirectory, this.merkleRoot).toFile();
            this.totalPieces = this.rootDirectory.list();
            System.out.println(seedRequest.getHostName() + " " + seedRequest.getPort());
            this.peerEndPoint = new Socket(seedRequest.getHostName(), seedRequest.getPort());
            System.out.println("Leecher at " + seedRequest.getHostName() + seedRequest.getPort());
            this.writeToPeer = new ObjectOutputStream(this.peerEndPoint.getOutputStream());
            this.readFromPeer = new ObjectInputStream(this.peerEndPoint.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listener() {
        while (true) {
            try {
                Message message = (Message) this.readFromPeer.readObject();
                if (message.getMessageType().equals("DISTRIBUTION")) {
                    DistributionMessage distributionMessage = (DistributionMessage) message;
                    this.assignedIndex = distributionMessage.getAssignedIndex();
                    this.numPeers = distributionMessage.getNumPeers();
                    this.numfileRecieved=distributionMessage.getNumFilesRecieved();
                    System.out.println("total num peers:" + this.numPeers);
                    System.out.println("i am number " + this.assignedIndex);
                } else {
                    System.out.println("Completion message received from peer\nStopping transmission");
                    Thread.currentThread().join();
                }
            } catch (ClassNotFoundException | IOException | InterruptedException e) {
                System.out.println("Connection with peer interrupted\nStopping transmission!");
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public void sendPieces() {
        int i = this.numfileRecieved;
        for (; i < this.totalPieces.length; i++) {
            if (i % this.numPeers == this.assignedIndex) {
                byte[] content;
                String fileName = totalPieces[i];
                InputStream fileReader;
                try {
                    File pieceFile = Paths.get(this.rootDirectory.toPath().toString(), fileName).toFile();
                    fileReader = new FileInputStream(pieceFile);
                    content = new byte[(int) pieceFile.length()];
                    fileReader.read(content);
                    Piece piece = new Piece(fileName, content);
                    writeToPeer.writeObject(piece);
                    System.out.println("Peer no " + this.assignedIndex + " sent file" + fileName);
                    System.out.println("this was piece no" + i);
                    fileReader.close();
                } catch (IOException e) {
                    System.out.println("Connection with leecher broken, stopping transmission");
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
            if (i >= this.totalPieces.length - 1) {
                i = -1;
            }
        }
    }

    public void disconnect() {
        DisconnectMessage disconnectMessage = new DisconnectMessage();
        try {
            this.writeToPeer.writeObject(disconnectMessage);
        } catch (IOException e) {
            System.out.println("Connection with leecher broken, stopping transmission");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
//        try {
//            DistributionMessage distributionMessage = (DistributionMessage) this.readFromPeer.readObject();
//            this.assignedIndex = distributionMessage.getAssignedIndex();
//            this.numPeers = distributionMessage.getNumPeers();
//            this.numfileRecieved = distributionMessage.getNumFilesRecieved();
//            System.out.println("total num of peers" + this.numPeers);
//            System.out.println("i AM " + this.assignedIndex);
//        } 
//        catch (ClassNotFoundException | IOException e) {
//            e.printStackTrace();
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("about to call listener");
                listener();
            }
        }).start();
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("about to call sendpieces");
                sendPieces();
            }
        }).start();
    }

    protected void finalize() {
        this.disconnect();
    }
}