package jtorrent.Encoding;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;

public class Decode {
    private HashMap<String, String> metaDataHash;
    private String merkleRoot;
    private String fileName, metaFileName;
    private String rootDirectory, currentDirectory;
    private File metaFile, file;

    public Decode(String metaFileName, String rootDirectory) {
        try {
            this.metaFileName = metaFileName;
            this.metaFile = new File(this.metaFileName);
            this.currentDirectory = metaFile.getParent();
            System.out.println(this.currentDirectory);
            ObjectInputStream readMetaFile = new ObjectInputStream(new FileInputStream(this.metaFile));
            try {
                this.metaDataHash = (HashMap<String, String>) readMetaFile.readObject();
            } catch (Exception e) {
                System.out.println("Corrupted metadata!\nUse valid metadata file");
                System.exit(-1);
            }
            this.merkleRoot = this.metaDataHash.get("merkleRoot");
            this.fileName = this.metaDataHash.get("Name");
            System.out.println(this.fileName);
            this.file = Paths.get(this.currentDirectory, this.fileName).toFile();
            System.out.println(this.file.getName());
            this.rootDirectory = Paths.get(rootDirectory, this.merkleRoot).toString();
            readMetaFile.close();
        } catch (IOException e) {
            System.out.println("Error reading metadata file");
            e.printStackTrace();
        }
    }

    public void Merge() {
        try {
            OutputStream outputStream = new FileOutputStream(this.file);
            for (int i = 0; i < metaDataHash.size(); i++) 
            {
                byte[] content = read(this.metaDataHash.get(Integer.toString(i)));
                outputStream.write(content);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] read(String fileName) {
    	File piece = Paths.get(this.rootDirectory, fileName).toFile();
    	System.out.println(fileName);
        byte[] pieceContent = new byte[(int)piece.length()];
        System.out.println(""+piece.length());
    	InputStream inputStream;
        try {
            inputStream = new FileInputStream(piece);
            inputStream.read(pieceContent);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pieceContent;
    }
    public String getMerkleRoot() {
        return this.merkleRoot;
    }

    public String getFileName() {
        return fileName;
    }

    public HashMap<String, String> getMetaDataHash() {
        return this.metaDataHash;
    }

    public static void main(String[] args) {
        Decode decode = new Decode("/home/prem/.P2P/prem/cfcefc9fb9c080029d319eeb9085ba0edcfe4deaafe43783c54ba0f470fb746c/C1.java.metadata", "/home/prem/.P2P/prem");
        decode.Merge();
    }
}