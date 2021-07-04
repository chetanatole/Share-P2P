package jtorrent.Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

import org.javatuples.Pair;

import jtorrent.Communication.Requests.*;
import jtorrent.Encoding.*;

public class Peer {

    Scanner sc = new Scanner(System.in);
    private Socket trackerEndpoint = null;
    public Boolean isLive = false;
    private ObjectOutputStream writeToTracker = null;
    private ObjectInputStream readFromTracker = null;
    private ArrayList<Pair<String, Integer>> myFiles = new ArrayList<>();
    // 2409:4042:229d:47ad:3d26:b9c8:3b79:335b
    // 2409:4042:2595:4b85:5b7e:d25c:51bd:c766
    //2409:4042:229d:47ad:91d7:28aa:6a6f:abdf
    private String trackerIP = new String("2409:4042:2595:4b85:5b7e:d25c:51bd:c766");
    private UserProfile userProfile = new UserProfile();
    public String rootDirectory = null;
    HashMap<Integer, String[]> changedFiles = new HashMap<Integer, String[]>();
    private ThreadPoolExecutor leechExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    private ThreadPoolExecutor seedExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    private ScheduledExecutorService updateExecutor = (ScheduledExecutorService) Executors.newScheduledThreadPool(1);

    public Peer() {
        try {
            this.trackerEndpoint = new Socket(trackerIP, 8080);
            isLive = true;
            this.writeToTracker = new ObjectOutputStream(trackerEndpoint.getOutputStream());
            this.readFromTracker = new ObjectInputStream(trackerEndpoint.getInputStream());
        } catch (IOException e) {
            System.out.println("Couldnt establish connection with tracker!");
        }
    }

    public String getRootDirectory() {
        return this.rootDirectory;
    }

    // /home/kushal/Downloads/paniyosa.mp3.metadata
    public String Connect(String type, String[] credentials) {
        String result = "Unknown Error\nPlease Try again";
        try {
            switch (type) {
            case "LOGIN":
                result = this.Login(credentials);
                break;
            case "REGISTER":
                result = this.Register(credentials);
                break;
            case "FORGOT PASSWORD":
                result = this.RecoverPassword(credentials);
                break;
            case "LOGOUT":
                result = this.Logout();
            default:
                break;
            }
        } catch (ClassNotFoundException e) {
            result = "Unexpected error from tracker!\nPlease try again";
        } catch (IOException e) {
            result = "Error connecting to tracker!";
        }
        return result;
    }

    public String Login(String[] credentials) throws IOException, ClassNotFoundException {
        this.userProfile.setUsername(credentials[0]);
        this.userProfile.setPassword(credentials[1]);

        ConnectRequest connectRequest = new ConnectRequest(8080, "LOGIN", this.userProfile.getUsername(),
                this.userProfile.getPassword(), this.userProfile.getNickName());
        this.writeToTracker.writeObject(connectRequest);

        Integer loginStatus = (Integer) this.readFromTracker.readObject();

        if (loginStatus.equals(0)) {
            return "Username or password incorrect!";
        } else {
            this.rootDirectory = Paths.get(System.getProperty("user.home"), ".P2P", this.userProfile.getUsername())
                    .toString();
            return "Welcome back " + this.userProfile.getUsername();
        }
    }

    public String Register(String[] credentials) throws IOException, ClassNotFoundException {
        this.userProfile.setUsername(credentials[0]);
        this.userProfile.setPassword(credentials[1]);
        this.userProfile.setNickName(credentials[2]);

        ConnectRequest connectRequest = new ConnectRequest(8080, "REGISTER", this.userProfile.getUsername(),
                this.userProfile.getPassword(), this.userProfile.getNickName());
        this.writeToTracker.writeObject(connectRequest);

        Integer registerStatus = (Integer) this.readFromTracker.readObject();
        if (registerStatus.equals(0)) {
            return "Username is taken!";
        } else {
            this.rootDirectory = Paths.get(System.getProperty("user.home"), ".P2P", this.userProfile.getUsername())
                    .toString();
            return "Welcome " + this.userProfile.getUsername();
        }
    }

    public String RecoverPassword(String[] credentials) throws IOException, ClassNotFoundException {
        this.userProfile.setUsername(credentials[0]);
        this.userProfile.setNickName(credentials[1]);

        ConnectRequest connectRequest = new ConnectRequest(8080, "FORGOT PASSWORD", this.userProfile.getUsername(),
                this.userProfile.getPassword(), this.userProfile.getNickName());
        writeToTracker.writeObject(connectRequest);

        String password = (String) this.readFromTracker.readObject();
        System.out.println(password);
        return password;
    }

    public String Logout() {
        try {
            ConnectRequest disconnectRequest = new ConnectRequest(8080, "LOGOUT", this.userProfile.getUsername(),
                    this.userProfile.getPassword(), this.userProfile.getNickName());
            writeToTracker.writeObject(disconnectRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Goodbye!";
    }

    public void Update() throws IOException {
        FileIndexManager fileIndexManager = new FileIndexManager(this.userProfile.getUsername());
        fileIndexManager.CheckForChanges();
        UpdateRequest updateRequest = new UpdateRequest(this.userProfile.getUsername(),
                fileIndexManager.getAddedMerkleRoots(), fileIndexManager.getRemovedMerkleRoots(),
                fileIndexManager.getAddedFileNames());
        this.writeToTracker.writeObject(updateRequest);
        this.myFiles = fileIndexManager.getDashboardData();
        System.out.println("myfiles size " + this.myFiles.size());
    }

    public FileLeecher leechFile(String metaFileName) {
        try {
            Decode decode = new Decode(metaFileName, this.rootDirectory);
            String merkleRoot = decode.getMerkleRoot();

            FileLeecher fileLeecher = new FileLeecher(merkleRoot, decode.getMetaDataHash(), rootDirectory, decode); // start
            LeechRequest leechRequest = new LeechRequest(fileLeecher.getPortNo(), merkleRoot); // ask for files on
            this.writeToTracker.writeObject(leechRequest);
            this.leechExecutor.submit(() -> {
                fileLeecher.run();
            });
            return fileLeecher;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SeederDaemon() {
        System.out.println("Seeder started");
        while (true) {
            try {
                SeedRequest seedRequest = (SeedRequest) this.readFromTracker.readObject();
                FileSeeder fileSeeder = new FileSeeder(seedRequest, this.rootDirectory);
                System.out.println(seedRequest.getMerkleRoot());
                this.seedExecutor.submit(fileSeeder);
            } catch (ClassNotFoundException | IOException e) {
                System.out.println("Client has been stopped.\nTerminating all seeds");
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void Publish(String fileName) {
        Encode encode = new Encode(fileName, this.rootDirectory, this.trackerEndpoint);
        encode.Split();
    }

    public void UpdateDaemon() {
        this.updateExecutor.scheduleAtFixedRate(new Thread(() -> {
            try {
                this.Update();
            } catch (IOException e) {
                System.out.println("error in sending updated files list to peer");
            }
        }), 0, 20, TimeUnit.SECONDS);
    }

    public ArrayList<Pair<String, Integer>> getMyFiles() {
        return myFiles;
    }

}