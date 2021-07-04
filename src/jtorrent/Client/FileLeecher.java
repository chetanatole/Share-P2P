package jtorrent.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jtorrent.Communication.P2PMessages.*;
import jtorrent.Encoding.Decode;

/**
 * Class to handle file leeching creates a serversocket that listens for
 * incoming pieces of files balances load between connected peers
 * 
 * @param merkleRoot        merkleRoot of the file to be transferred
 * @param metadataHash      hashmap of the metadata for the file
 * @param rootDirectoryPath the root of the directory where users files are to
 *                          be stored
 */
public class FileLeecher implements Runnable {

    private String merkleRoot = null;
    private ServerSocket serverSocket = null;
    private ArrayList<Socket> peerSockets = null;
    private ArrayList<String> pendingPieces = new ArrayList<String>();
    private HashMap<String, String> metadataHash = null;
    private String rootDirectory = null;
    private Decode metaFileDecoder = null;
    private Integer numPiecesRecieved = null;
    private File rootDirectoryFolder = null;
    private ArrayList<ObjectOutputStream> peerWriters=new ArrayList<ObjectOutputStream>();
    
    
    public FileLeecher(String merkleRoot, HashMap<String, String> metadataHash, String rootDirectory,
            Decode metaFileDecoder) {
        this.peerSockets = new ArrayList<Socket>();
        this.merkleRoot = merkleRoot;
        this.metadataHash = metadataHash;
        this.rootDirectory = Paths.get(rootDirectory, this.merkleRoot).toString();
        this.rootDirectoryFolder = new File(this.rootDirectory);
        if (!this.rootDirectoryFolder.exists()) {
            this.rootDirectoryFolder.mkdirs();
        }
        this.metadataHash.remove("merkleRoot");
        String filename = this.metadataHash.get("Name");
        this.metadataHash.remove("Name");
        this.metadataHash.remove("Tracker");
        this.metadataHash.remove("fileSizeMB");
        Collection<String> hashvalues = metadataHash.values();
        this.pendingPieces = new ArrayList<String>(hashvalues);
        this.pendingPieces.add(filename+".metadata");
        this.metaFileDecoder = metaFileDecoder;
        for (int i = 0; i < pendingPieces.size(); i++) {
            System.out.println(pendingPieces.get(i));
        }
        this.numPiecesRecieved = 0;
        try {
            this.serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPendingPieces() {
        return this.pendingPieces;
    }
    
    public void updatePendingPieces(String pieceHash) {
        this.pendingPieces.remove(pieceHash);
        if (pendingPieces.size() == 0) {
            System.out.println("All hashes removed");
            disconnect();
            System.out.println("All piece received\nReconstructing file ....");
            this.metaFileDecoder.Merge();
        }
    }

    public Integer getPortNo() {
        return this.serverSocket.getLocalPort();
    }

    public void BalanceLoad() {
        Integer numPeers = this.peerSockets.size();
        Integer index = 0;
        for (ObjectOutputStream peerWriter : this.peerWriters) {
            System.out.println("Peers = " + numPeers + "\n index = " + index);
            DistributionMessage distributionMessage;
            distributionMessage = new DistributionMessage(numPeers, index, this.numPiecesRecieved);
            index++;
            try {
            	peerWriter.writeObject(distributionMessage);
            	peerWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void writePiecetoDisk(String pieceHash, byte[] content) {
        File newPiece = Paths.get(this.rootDirectory, pieceHash).toFile();
        try {
            if (!newPiece.exists()) {
                newPiece.createNewFile();
            }
            OutputStream writeToFile = new FileOutputStream(newPiece);
            writeToFile.write(content);
            updatePendingPieces(pieceHash);
            this.numPiecesRecieved++;
            writeToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Listener(Socket socket) {
        System.out.println("Inside new thread");
        try {
            ObjectInputStream receiveFromPeer = new ObjectInputStream(socket.getInputStream());
            while (!socket.isClosed() && !pendingPieces.isEmpty()) {
                Message message = (Message) receiveFromPeer.readObject();
                String messageType = message.getMessageType();
                if (messageType.equals("PIECE")) {
                    // peer is sending over a piece
                    Piece piece = (Piece) message;
                    String pieceHash = piece.getPieceHash();
                    System.out.println(pieceHash);
                    if (pendingPieces.contains(pieceHash)) {
                        writePiecetoDisk(pieceHash, piece.getContent());
                    }
                } else if (messageType.equals("DISCONNECT")) {
                    // peer is going offline
                    peerSockets.remove(socket);
                    BalanceLoad();
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("One peer has disconnected!\n");
            peerSockets.remove(socket);
            BalanceLoad();
            e.printStackTrace();
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        for (Socket socket : peerSockets) {
            DisconnectMessage disconnectMessage = new DisconnectMessage();
            try {
                ObjectOutputStream writeToSeeder = new ObjectOutputStream(socket.getOutputStream());
                writeToSeeder.writeObject(disconnectMessage);
                writeToSeeder.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void leech() {
        while (this.pendingPieces.size() != 0) {
            Socket socket;
            try {
                // if (this.peerSockets.size() <= 10) {
                System.out.println("Inside leacher");
                socket = this.serverSocket.accept();
                System.out.println("Seeder connected");
                ObjectOutputStream writeToPeer=new ObjectOutputStream(socket.getOutputStream());
                peerWriters.add(writeToPeer);
                peerSockets.add(socket);
                System.out.println("After peer socket");
                this.BalanceLoad();
                System.out.println("After load balance");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Listener(socket);
                    }
                }).start();
                // }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                leech();
            }
        }).start();
    }
}