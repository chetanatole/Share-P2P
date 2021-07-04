import gui.*;
import jtorrent.Client.Peer;

public class App {
    public static void main(String[] args) {
        Peer peer = new Peer();
        Login login = new Login(peer);
        login.LoginFrame.setVisible(true);
    }
}