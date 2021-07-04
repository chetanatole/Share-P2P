package jtorrent.Encoding;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.math.BigInteger;  
import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;


public class Encode {
    private String filename;
    private File file, metadata, newFile, tempDirectory;
    private FileInputStream reader;
    private FileOutputStream writer;
    private HashMap<String, String> MetaDataHash = new HashMap<String, String>();
    private String tempDirectoryPath = null, rootDirectoryPath; // tempoarary directory name where encoded pieces will//
                                                                // be stored
    private Socket trackerEndpoint;
    private float fileSize;

    public Encode(String filename, String rootDirectory, Socket socket) {
        file = new File(filename);
        String tempArr[] = filename.split("/");
        filename = tempArr[tempArr.length - 1];
        this.filename = tempArr[tempArr.length - 1];
        this.trackerEndpoint = socket;
        this.rootDirectoryPath = rootDirectory;
        this.tempDirectoryPath = Paths.get(rootDirectory, this.filename).toString();
        this.tempDirectory = new File(this.tempDirectoryPath);
        if (!this.tempDirectory.exists()) {
            this.tempDirectory.mkdirs();
        }
        metadata = new File(Paths.get(this.tempDirectoryPath, this.filename + ".metadata").toString()); // metadatafile
        if (!metadata.exists()) {
            try {
                metadata.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // created
        try {
            reader = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Split() {
        try {
            long totalFileLength = file.length();
            this.fileSize = (float) totalFileLength / (1024 * 1024);
            long index = 0;
            long numPieces = totalFileLength / (1024 * 1024);
            int remainder = (int) totalFileLength % (1024 * 1024);
            while(numPieces>0)
            {
                byte[] piece = new byte[1024*1024];
                reader.read(piece);
                MessageDigest digest;
				try {
					digest = MessageDigest.getInstance("SHA-256");
	    			digest.update(piece, 0,piece.length);
	    			byte[] piecehash = digest.digest();
	    			String hash=toHexString(piecehash);
	                this.writePiece(hash, piece);
	                MetaDataHash.put(Long.toString(index), hash);
	                index++;
	                numPieces--;
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            byte[] lastPiece = new byte[remainder];
            reader.read(lastPiece);
            reader.close();
            MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-256");
				digest.update(lastPiece, 0,lastPiece.length);
				byte[] piecehash = digest.digest();
				String hash=toHexString(piecehash);
	            this.writePiece(hash, lastPiece);
	            MetaDataHash.put(Long.toString(index), hash);
	            index++;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            String merkleRoot = createMerkleRoot(MetaDataHash);

            writeMetaData(merkleRoot);

            this.tempDirectory.renameTo(new File(Paths.get(this.rootDirectoryPath, merkleRoot).toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTrackerInfo() {
        return this.trackerEndpoint.getInetAddress().toString();
    }

    private void writeMetaData(String merkleRoot) {
        try {
            ObjectOutputStream metaDataOutput = new ObjectOutputStream(new FileOutputStream(metadata));
            MetaDataHash.put("merkleRoot", merkleRoot);
            MetaDataHash.put("Tracker", getTrackerInfo());
            MetaDataHash.put("Name", file.getName());
            MetaDataHash.put("fileSizeMB", Float.toString(this.fileSize));
            metaDataOutput.writeObject(MetaDataHash);
            metaDataOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * accept name and content of individual piece as parameter, and write it out to
     * a file on local filesystem
     */
    private void writePiece(String name, byte[] piece) {
        File newFile = new File(Paths.get(this.tempDirectoryPath, name).toString());
        try {
            OutputStream pieceWriter = new FileOutputStream(newFile);
            pieceWriter.write(piece);
            pieceWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createMerkleRoot(HashMap<String, String> Index) {
        String MerkleRoot = "";
        for (Entry<String, String> entry : Index.entrySet()) {
        	MerkleRoot=MerkleRoot+entry.getValue();
        }
        
        byte[] merkleRootbyte=MerkleRoot.getBytes();
        MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(merkleRootbyte, 0,merkleRootbyte.length);
			byte[] piecehash = digest.digest();
			MerkleRoot=toHexString(piecehash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return MerkleRoot;
    }
	 public static String toHexString(byte[] hash) 
	    { 
	        // Convert byte array into signum representation  
	        BigInteger number = new BigInteger(1, hash);  
	  
	        // Convert message digest into hex value  
	        StringBuilder hexString = new StringBuilder(number.toString(16));  
	  
	        // Pad with leading zeros 
	        while (hexString.length() < 32)  
	        {  
	            hexString.insert(0, '0');  
	        }  
	  
	        return hexString.toString();  
	    } 

    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket("localhost", 8080);
            Encode encode = new Encode("/home/kushal/srgan.zip", "/home/kushal/.P2P/myuser", socket);
            encode.Split();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
