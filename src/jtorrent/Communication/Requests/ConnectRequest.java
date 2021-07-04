package jtorrent.Communication.Requests;

public class ConnectRequest implements Request {

    /**
     *
     */
    private static final long serialVersionUID = -1600213516728812074L;
    private final String requestType = "CONNECT";
    private String hostName = null;
    private Integer port = null;
    private String connectionType = null;
    private String username = null;
    private String password = null;
    private Boolean active = null;
    private String nickname = null;

    public ConnectRequest(Integer port, String connectionType, String username, String password, String nickname) {
        this.port = port;
        this.connectionType = connectionType;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String getRequestType() {
        return requestType;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getUsername() {
        return username;
    }

    public void setHostName(String hostName) {
        if (hostName != null) {
            this.hostName = hostName.substring(1);
        } else {
            this.hostName = hostName;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return this.active;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}