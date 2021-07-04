package jtorrent.Communication.P2PMessages;

/**
 * This message is sent by the peer when it is going offline
 */
public class DisconnectMessage implements Message {

    private static final long serialVersionUID = 1L;
    private final String messageType = "DISCONNECT";

    @Override
    public String getMessageType() {
        return this.messageType;
    }

}