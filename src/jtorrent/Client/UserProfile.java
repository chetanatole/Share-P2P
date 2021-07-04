package jtorrent.Client;

import java.util.Scanner;

/**
 * perform profile actions like logging in, signing up, loggin out, etc.
 */
public class UserProfile {
    private String username = new String("");
    private String password = new String("");
    private String nickName = new String("");
    Scanner sc = new Scanner(System.in);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    Boolean isLoggedIn = false;

    public void getCredentials() {
        System.out.println("Enter username");
        this.setUsername(sc.nextLine());
        System.out.println("Enter password");
        this.setPassword(sc.nextLine());
    }
}