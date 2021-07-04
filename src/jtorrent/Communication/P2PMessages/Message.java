package jtorrent.Communication.P2PMessages;

import java.io.Serializable;

/**
 * Generalised message class for communication between peers
 */
public interface Message extends Serializable {
    public String getMessageType();
}