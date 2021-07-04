package jtorrent.Communication.Requests;

public class LeechRequest implements Request {

    private static final long serialVersionUID = -4345093058309075093L;
    private final String requestType = "LEECH";
    String hostName = null;
    Integer port = null;
    String merkleRoot = null;

    public LeechRequest(Integer port, String merkleRoot) {
        this.port = port;
        this.merkleRoot = merkleRoot;
    }

    @Override
    public String getRequestType() {
        return requestType;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName.substring(1);
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

}