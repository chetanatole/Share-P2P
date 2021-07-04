package jtorrent.Communication.Requests;

public class SeedRequest implements Request {

    /**
     *
     */
    private static final long serialVersionUID = -2422420751877105917L;
    private final String requestType = "SEED";
    private String hostname = null;
    private Integer port = null;
    private String merkleRoot;

    public SeedRequest(String hostname, Integer port, String merkleRoot) {
        this.hostname = hostname;
        this.port = port;
        this.merkleRoot = merkleRoot;
    }

    @Override
    public String getRequestType() {
        return requestType;
    }

    @Override
    public String getHostName() {
        return hostname;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostname = hostName.substring(1);
    }

}