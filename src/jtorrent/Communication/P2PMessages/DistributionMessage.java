package jtorrent.Communication.P2PMessages;

/**
 * This message is sent by leecher to seeders to inform them what parts of the
 * file they're supposed to send
 * 
 * @param pieceHashes Array of pieceIds that are assigned to the peer
 */
public class DistributionMessage implements Message {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final String messageType = "DISTRIBUTION";
    private Integer numPeers;
    private Integer assignedIndex;
    private Integer numFilesRecieved;

    public DistributionMessage(Integer numPeers, Integer assignedIndex, Integer currentLatestIndex) {
        this.assignedIndex = assignedIndex;
        this.numPeers = numPeers;
        this.numFilesRecieved = currentLatestIndex;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    public Integer getNumFilesRecieved() {
        return this.numFilesRecieved;
    }

    public Integer getNumPeers() {
        return numPeers;
    }

    public Integer getAssignedIndex() {
        return assignedIndex;
    }

}